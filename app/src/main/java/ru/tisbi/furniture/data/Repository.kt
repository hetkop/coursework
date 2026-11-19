package ru.tisbi.furniture.data

import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException

/** Ошибка, текст которой можно показать пользователю. */
class UserError(message: String) : Exception(message)

/**
 * Все операции с БД: SQL-запросы, транзакции и перевод ошибок СУБД
 * (нарушение внешнего ключа, срабатывание триггера) в понятные сообщения.
 */
class Repository(private val db: Db) {

    private val w get() = db.writableDatabase

    // ---------- служебные функции ----------

    private fun <T> q(sql: String, vararg args: String, map: (Cursor) -> T): List<T> =
        w.rawQuery(sql, arrayOf(*args)).use { c ->
            val out = ArrayList<T>()
            while (c.moveToNext()) out.add(map(c))
            out
        }

    private fun exec(sql: String, vararg args: Any?) = w.execSQL(sql, arrayOf(*args))

    private fun insert(sql: String, vararg args: Any?): Long {
        exec(sql, *args)
        return q("SELECT last_insert_rowid()") { it.getLong(0) }.first()
    }

    /** Транзакция: при любом исключении все изменения откатываются. */
    private fun <T> tx(block: () -> T): T {
        w.beginTransaction()
        try {
            val r = block()
            w.setTransactionSuccessful()
            return r
        } finally {
            w.endTransaction()
        }
    }

    private fun <T> guard(fkMessage: String? = null, block: () -> T): T = try {
        block()
    } catch (e: SQLiteConstraintException) {
        val m = e.message ?: ""
        throw UserError(
            when {
                "TRIGGER" in m -> m.substringBefore(" (code")      // сообщение из RAISE триггера
                "FOREIGN KEY" in m -> fkMessage ?: "Запись связана с другими данными"
                "UNIQUE" in m -> "Такая запись уже существует"
                "CHECK" in m -> "Недопустимое значение"
                else -> m
            }
        )
    }

    // ---------- загрузка ----------

    fun load() = AppData(
        refs(RefKind.Category), refs(RefKind.Material), refs(RefKind.Style), refs(RefKind.Status),
        customers(), suppliers(), products(), orders(), supplies()
    )

    // ---------- справочники ----------

    fun refs(k: RefKind): List<Ref> {
        val order = if (k == RefKind.Status) k.idCol else "Name"
        return q("SELECT ${k.idCol}, Name FROM ${k.table} ORDER BY $order") { Ref(it.getLong(0), it.getString(1)) }
    }

    fun saveRef(k: RefKind, id: Long, name: String) = guard {
        if (id == 0L) exec("INSERT INTO ${k.table}(Name) VALUES(?)", name)
        else exec("UPDATE ${k.table} SET Name=? WHERE ${k.idCol}=?", name, id)
    }

    fun deleteRef(k: RefKind, id: Long) = guard(k.usedMsg) {
        exec("DELETE FROM ${k.table} WHERE ${k.idCol}=?", id)
    }

    // ---------- товары ----------

    fun products(): List<Product> = q(
        """SELECT p.ProductID, p.Name, IFNULL(p.Description,''), p.Price, p.InStock,
                  p.CategoryID, p.MaterialID, p.StyleID, c.Name, m.Name, s.Name
           FROM Products p
           JOIN Categories c ON c.CategoryID = p.CategoryID
           JOIN Materials m ON m.MaterialID = p.MaterialID
           JOIN Styles s ON s.StyleID = p.StyleID
           ORDER BY p.Name"""
    ) {
        Product(it.getLong(0), it.getString(1), it.getString(2), it.getDouble(3), it.getInt(4),
            it.getLong(5), it.getLong(6), it.getLong(7), it.getString(8), it.getString(9), it.getString(10))
    }

    fun saveProduct(p: Product) = guard {
        if (p.id == 0L) exec(
            "INSERT INTO Products(Name,Description,Price,InStock,CategoryID,MaterialID,StyleID) VALUES(?,?,?,?,?,?,?)",
            p.name, p.description, p.price, p.inStock, p.categoryId, p.materialId, p.styleId
        ) else exec(
            "UPDATE Products SET Name=?,Description=?,Price=?,InStock=?,CategoryID=?,MaterialID=?,StyleID=? WHERE ProductID=?",
            p.name, p.description, p.price, p.inStock, p.categoryId, p.materialId, p.styleId, p.id
        )
    }

    fun deleteProduct(id: Long) = guard("Невозможно удалить товар, который входит в состав заказов или поставок") {
        exec("DELETE FROM Products WHERE ProductID=?", id)
    }

    // ---------- клиенты ----------

    fun customers(): List<Customer> =
        q("SELECT CustomerID, FirstName, LastName, PhoneNumber, ShippingAddress FROM Customers ORDER BY LastName, FirstName") {
            Customer(it.getLong(0), it.getString(1), it.getString(2), it.getString(3), it.getString(4))
        }

    fun saveCustomer(c: Customer) = guard {
        if (c.id == 0L) exec("INSERT INTO Customers(FirstName,LastName,PhoneNumber,ShippingAddress) VALUES(?,?,?,?)",
            c.firstName, c.lastName, c.phone, c.address)
        else exec("UPDATE Customers SET FirstName=?,LastName=?,PhoneNumber=?,ShippingAddress=? WHERE CustomerID=?",
            c.firstName, c.lastName, c.phone, c.address, c.id)
    }

    fun deleteCustomer(id: Long) = guard("Невозможно удалить клиента, у которого есть заказы") {
        exec("DELETE FROM Customers WHERE CustomerID=?", id)
    }

    // ---------- поставщики ----------

    fun suppliers(): List<Supplier> =
        q("SELECT SupplierID, CompanyName, ContactPerson, PhoneNumber FROM Suppliers ORDER BY CompanyName") {
            Supplier(it.getLong(0), it.getString(1), it.getString(2), it.getString(3))
        }

    fun saveSupplier(s: Supplier) = guard {
        if (s.id == 0L) exec("INSERT INTO Suppliers(CompanyName,ContactPerson,PhoneNumber) VALUES(?,?,?)",
            s.company, s.contact, s.phone)
        else exec("UPDATE Suppliers SET CompanyName=?,ContactPerson=?,PhoneNumber=? WHERE SupplierID=?",
            s.company, s.contact, s.phone, s.id)
    }

    fun deleteSupplier(id: Long) = guard("Невозможно удалить поставщика, связанного с существующими поставками") {
        exec("DELETE FROM Suppliers WHERE SupplierID=?", id)
    }

    // ---------- заказы ----------

    /** Заказы; итоговая сумма вычисляется агрегатным запросом (в таблице не хранится). */
    fun orders(from: String? = null, to: String? = null): List<OrderRow> {
        val ranged = from != null && to != null
        val where = if (ranged) "WHERE o.OrderDate BETWEEN ? AND ?" else ""
        val args = if (ranged) arrayOf(from!!, to!!) else emptyArray<String>()
        return q(
            """SELECT o.OrderID, o.CustomerID, c.LastName || ' ' || c.FirstName, o.StatusID, s.Name, o.OrderDate,
                      IFNULL((SELECT SUM(p.Price * i.Quantity) FROM OrderItems i
                              JOIN Products p ON p.ProductID = i.ProductID WHERE i.OrderID = o.OrderID), 0),
                      IFNULL((SELECT SUM(i.Quantity) FROM OrderItems i WHERE i.OrderID = o.OrderID), 0)
               FROM Orders o
               JOIN Customers c ON c.CustomerID = o.CustomerID
               JOIN Statuses s ON s.StatusID = o.StatusID
               $where
               ORDER BY o.OrderDate DESC, o.OrderID DESC""",
            *args
        ) {
            OrderRow(it.getLong(0), it.getLong(1), it.getString(2), it.getLong(3), it.getString(4),
                it.getString(5), it.getDouble(6), it.getInt(7))
        }
    }

    fun orderLines(orderId: Long): List<OrderLine> = q(
        """SELECT p.ProductID, p.Name, p.Price, i.Quantity
           FROM OrderItems i JOIN Products p ON p.ProductID = i.ProductID
           WHERE i.OrderID = ? ORDER BY p.Name""", orderId.toString()
    ) { OrderLine(it.getLong(0), it.getString(1), it.getDouble(2), it.getInt(3)) }

    /** Оформление заказа: шапка и все позиции в одной транзакции; остатки списывают триггеры. */
    fun createOrder(customerId: Long, statusId: Long, date: String, items: List<Pair<Long, Int>>) = guard {
        tx {
            val id = insert("INSERT INTO Orders(CustomerID,StatusID,OrderDate) VALUES(?,?,?)", customerId, statusId, date)
            items.forEach { (productId, qty) ->
                exec("INSERT INTO OrderItems(OrderID,ProductID,Quantity) VALUES(?,?,?)", id, productId, qty)
            }
        }
    }

    fun setOrderStatus(orderId: Long, statusId: Long) = guard {
        exec("UPDATE Orders SET StatusID=? WHERE OrderID=?", statusId, orderId)
    }

    /** Удаление заказа: сначала позиции (товар возвращается на склад триггером), затем сам заказ. */
    fun deleteOrder(id: Long) = guard {
        tx {
            exec("DELETE FROM OrderItems WHERE OrderID=?", id)
            exec("DELETE FROM Orders WHERE OrderID=?", id)
        }
    }

    // ---------- поставки ----------

    fun supplies(): List<SupplyRow> = q(
        """SELECT s.SupplyID, s.SupplyDate, COUNT(i.SupplyItemID), IFNULL(SUM(i.Quantity), 0)
           FROM Supplies s LEFT JOIN SupplyItems i ON i.SupplyID = s.SupplyID
           GROUP BY s.SupplyID ORDER BY s.SupplyDate DESC, s.SupplyID DESC"""
    ) { SupplyRow(it.getLong(0), it.getString(1), it.getInt(2), it.getInt(3)) }

    fun supplyLines(supplyId: Long): List<SupplyLine> = q(
        """SELECT p.ProductID, p.Name, sp.SupplierID, sp.CompanyName, i.Quantity
           FROM SupplyItems i
           JOIN Products p ON p.ProductID = i.ProductID
           JOIN Suppliers sp ON sp.SupplierID = i.SupplierID
           WHERE i.SupplyID = ? ORDER BY p.Name""", supplyId.toString()
    ) { SupplyLine(it.getLong(0), it.getString(1), it.getLong(2), it.getString(3), it.getInt(4)) }

    /** Регистрация поставки: шапка и позиции в одной транзакции; остатки увеличивает триггер. */
    fun createSupply(date: String, lines: List<SupplyLine>) = guard {
        tx {
            val id = insert("INSERT INTO Supplies(SupplyDate) VALUES(?)", date)
            lines.forEach {
                exec("INSERT INTO SupplyItems(SupplyID,ProductID,SupplierID,Quantity) VALUES(?,?,?,?)",
                    id, it.productId, it.supplierId, it.qty)
            }
        }
    }

    fun deleteSupply(id: Long) = guard {
        tx {
            exec("DELETE FROM SupplyItems WHERE SupplyID=?", id)
            exec("DELETE FROM Supplies WHERE SupplyID=?", id)
        }
    }

    // ---------- отчёты ----------

    fun popularProducts(): List<PopularRow> = q(
        """SELECT p.Name, c.Name, SUM(i.Quantity), SUM(i.Quantity * p.Price)
           FROM OrderItems i
           JOIN Products p ON p.ProductID = i.ProductID
           JOIN Categories c ON c.CategoryID = p.CategoryID
           GROUP BY p.ProductID ORDER BY SUM(i.Quantity) DESC, p.Name"""
    ) { PopularRow(it.getString(0), it.getString(1), it.getInt(2), it.getDouble(3)) }
}
