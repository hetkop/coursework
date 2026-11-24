package ru.tisbi.furniture.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

private const val ORDERS_SELECT = """
    SELECT o.OrderID AS id, o.CustomerID AS customerId, c.LastName || ' ' || c.FirstName AS customer,
           o.StatusID AS statusId, s.Name AS status, o.OrderDate AS date,
           IFNULL((SELECT SUM(p.Price * i.Quantity) FROM OrderItems i
                   JOIN Products p ON p.ProductID = i.ProductID WHERE i.OrderID = o.OrderID), 0) AS total,
           IFNULL((SELECT SUM(i.Quantity) FROM OrderItems i WHERE i.OrderID = o.OrderID), 0) AS qty
    FROM Orders o
    JOIN Customers c ON c.CustomerID = o.CustomerID
    JOIN Statuses s ON s.StatusID = o.StatusID
"""

/**
 * Объект доступа к данным (DAO). SQL-запросы записаны в аннотациях @Query и проверяются
 * Room на этапе компиляции. Методы с @Transaction выполняются как единая транзакция:
 * при исключении (в том числе при срабатывании триггера) все изменения откатываются.
 */
@Dao
abstract class StoreDao {

    // ---------- справочники ----------

    @Query("SELECT CategoryID AS id, Name AS name FROM Categories ORDER BY Name")
    abstract fun categories(): List<Ref>
    @Query("SELECT MaterialID AS id, Name AS name FROM Materials ORDER BY Name")
    abstract fun materials(): List<Ref>
    @Query("SELECT StyleID AS id, Name AS name FROM Styles ORDER BY Name")
    abstract fun styles(): List<Ref>
    @Query("SELECT StatusID AS id, Name AS name FROM Statuses ORDER BY StatusID")
    abstract fun statuses(): List<Ref>

    @Insert abstract fun insertCategory(e: CategoryEntity): Long
    @Insert abstract fun insertMaterial(e: MaterialEntity): Long
    @Insert abstract fun insertStyle(e: StyleEntity): Long
    @Insert abstract fun insertStatus(e: StatusEntity): Long

    @Query("UPDATE Categories SET Name = :name WHERE CategoryID = :id")
    abstract fun updateCategory(id: Long, name: String)
    @Query("UPDATE Materials SET Name = :name WHERE MaterialID = :id")
    abstract fun updateMaterial(id: Long, name: String)
    @Query("UPDATE Styles SET Name = :name WHERE StyleID = :id")
    abstract fun updateStyle(id: Long, name: String)
    @Query("UPDATE Statuses SET Name = :name WHERE StatusID = :id")
    abstract fun updateStatus(id: Long, name: String)

    @Query("DELETE FROM Categories WHERE CategoryID = :id")
    abstract fun deleteCategory(id: Long)
    @Query("DELETE FROM Materials WHERE MaterialID = :id")
    abstract fun deleteMaterial(id: Long)
    @Query("DELETE FROM Styles WHERE StyleID = :id")
    abstract fun deleteStyle(id: Long)
    @Query("DELETE FROM Statuses WHERE StatusID = :id")
    abstract fun deleteStatus(id: Long)

    // ---------- товары ----------

    @Query(
        """SELECT p.ProductID AS id, p.Name AS name, IFNULL(p.Description, '') AS description, p.Price AS price,
                  p.InStock AS inStock, p.CategoryID AS categoryId, p.MaterialID AS materialId, p.StyleID AS styleId,
                  c.Name AS categoryName, m.Name AS materialName, s.Name AS styleName
           FROM Products p
           JOIN Categories c ON c.CategoryID = p.CategoryID
           JOIN Materials m ON m.MaterialID = p.MaterialID
           JOIN Styles s ON s.StyleID = p.StyleID
           ORDER BY p.Name"""
    )
    abstract fun products(): List<Product>

    @Insert abstract fun insertProduct(e: ProductEntity): Long

    @Query(
        """UPDATE Products SET Name = :name, Description = :description, Price = :price, InStock = :inStock,
                  CategoryID = :categoryId, MaterialID = :materialId, StyleID = :styleId WHERE ProductID = :id"""
    )
    abstract fun updateProduct(
        id: Long, name: String, description: String, price: Double, inStock: Int,
        categoryId: Long, materialId: Long, styleId: Long
    )

    @Query("DELETE FROM Products WHERE ProductID = :id")
    abstract fun deleteProduct(id: Long)

    // ---------- клиенты ----------

    @Query(
        """SELECT CustomerID AS id, FirstName AS firstName, LastName AS lastName, PhoneNumber AS phone,
                  ShippingAddress AS address FROM Customers ORDER BY LastName, FirstName"""
    )
    abstract fun customers(): List<Customer>

    @Insert abstract fun insertCustomer(e: CustomerEntity): Long

    @Query(
        """UPDATE Customers SET FirstName = :firstName, LastName = :lastName, PhoneNumber = :phone,
                  ShippingAddress = :address WHERE CustomerID = :id"""
    )
    abstract fun updateCustomer(id: Long, firstName: String, lastName: String, phone: String, address: String)

    @Query("DELETE FROM Customers WHERE CustomerID = :id")
    abstract fun deleteCustomer(id: Long)

    // ---------- поставщики ----------

    @Query("SELECT SupplierID AS id, CompanyName AS company, ContactPerson AS contact, PhoneNumber AS phone FROM Suppliers ORDER BY CompanyName")
    abstract fun suppliers(): List<Supplier>

    @Insert abstract fun insertSupplier(e: SupplierEntity): Long

    @Query("UPDATE Suppliers SET CompanyName = :company, ContactPerson = :contact, PhoneNumber = :phone WHERE SupplierID = :id")
    abstract fun updateSupplier(id: Long, company: String, contact: String, phone: String)

    @Query("DELETE FROM Suppliers WHERE SupplierID = :id")
    abstract fun deleteSupplier(id: Long)

    // ---------- заказы ----------

    @Query(ORDERS_SELECT + " ORDER BY o.OrderDate DESC, o.OrderID DESC")
    abstract fun orders(): List<OrderRow>

    @Query(ORDERS_SELECT + " WHERE o.OrderDate BETWEEN :dateFrom AND :dateTo ORDER BY o.OrderDate DESC, o.OrderID DESC")
    abstract fun ordersBetween(dateFrom: String, dateTo: String): List<OrderRow>

    @Query(
        """SELECT p.ProductID AS productId, p.Name AS product, p.Price AS price, i.Quantity AS qty
           FROM OrderItems i JOIN Products p ON p.ProductID = i.ProductID
           WHERE i.OrderID = :orderId ORDER BY p.Name"""
    )
    abstract fun orderLines(orderId: Long): List<OrderLine>

    @Insert abstract fun insertOrder(e: OrderEntity): Long
    @Insert abstract fun insertOrderItem(e: OrderItemEntity): Long

    @Query("UPDATE Orders SET StatusID = :statusId WHERE OrderID = :id")
    abstract fun setOrderStatus(id: Long, statusId: Long)

    @Query("DELETE FROM OrderItems WHERE OrderID = :orderId")
    abstract fun deleteOrderItems(orderId: Long)
    @Query("DELETE FROM Orders WHERE OrderID = :id")
    abstract fun deleteOrderRow(id: Long)

    /** Оформление заказа: шапка и все позиции в одной транзакции; остатки списывают триггеры. */
    @Transaction
    open fun createOrder(customerId: Long, statusId: Long, date: String, items: List<Pair<Long, Int>>) {
        val orderId = insertOrder(OrderEntity(customerId = customerId, statusId = statusId, date = date))
        for ((productId, qty) in items) {
            insertOrderItem(OrderItemEntity(orderId = orderId, productId = productId, quantity = qty))
        }
    }

    /** Удаление заказа: сначала позиции (товар возвращается на склад триггером), затем заказ. */
    @Transaction
    open fun deleteOrder(id: Long) {
        deleteOrderItems(id)
        deleteOrderRow(id)
    }

    // ---------- поставки ----------

    @Query(
        """SELECT s.SupplyID AS id, s.SupplyDate AS date, COUNT(i.SupplyItemID) AS positions,
                  IFNULL(SUM(i.Quantity), 0) AS qty
           FROM Supplies s LEFT JOIN SupplyItems i ON i.SupplyID = s.SupplyID
           GROUP BY s.SupplyID ORDER BY s.SupplyDate DESC, s.SupplyID DESC"""
    )
    abstract fun supplies(): List<SupplyRow>

    @Query(
        """SELECT p.ProductID AS productId, p.Name AS product, sp.SupplierID AS supplierId,
                  sp.CompanyName AS supplier, i.Quantity AS qty
           FROM SupplyItems i
           JOIN Products p ON p.ProductID = i.ProductID
           JOIN Suppliers sp ON sp.SupplierID = i.SupplierID
           WHERE i.SupplyID = :supplyId ORDER BY p.Name"""
    )
    abstract fun supplyLines(supplyId: Long): List<SupplyLine>

    @Insert abstract fun insertSupply(e: SupplyEntity): Long
    @Insert abstract fun insertSupplyItem(e: SupplyItemEntity): Long

    @Query("DELETE FROM SupplyItems WHERE SupplyID = :supplyId")
    abstract fun deleteSupplyItems(supplyId: Long)
    @Query("DELETE FROM Supplies WHERE SupplyID = :id")
    abstract fun deleteSupplyRow(id: Long)

    /** Регистрация поставки: шапка и позиции в одной транзакции; остатки увеличивает триггер. */
    @Transaction
    open fun createSupply(date: String, lines: List<SupplyLine>) {
        val supplyId = insertSupply(SupplyEntity(date = date))
        for (l in lines) {
            insertSupplyItem(SupplyItemEntity(supplyId = supplyId, productId = l.productId, supplierId = l.supplierId, quantity = l.qty))
        }
    }

    @Transaction
    open fun deleteSupply(id: Long) {
        deleteSupplyItems(id)
        deleteSupplyRow(id)
    }

    // ---------- отчёты ----------

    @Query(
        """SELECT p.Name AS product, c.Name AS category, SUM(i.Quantity) AS qty, SUM(i.Quantity * p.Price) AS revenue
           FROM OrderItems i
           JOIN Products p ON p.ProductID = i.ProductID
           JOIN Categories c ON c.CategoryID = p.CategoryID
           GROUP BY p.ProductID ORDER BY SUM(i.Quantity) DESC, p.Name"""
    )
    abstract fun popularProducts(): List<PopularRow>
}
