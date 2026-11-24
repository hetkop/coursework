package ru.tisbi.furniture.data

import android.database.sqlite.SQLiteConstraintException

/** Ошибка, текст которой можно показать пользователю. */
class UserError(message: String) : Exception(message)

/**
 * Слой доступа к данным над Room. Вызывает методы DAO и переводит ошибки СУБД
 * (нарушение внешнего ключа, срабатывание триггера) в понятные сообщения.
 * Многошаговые операции (createOrder, deleteOrder, createSupply, deleteSupply)
 * выполняются в DAO как транзакции (@Transaction).
 */
class Repository(private val dao: StoreDao) {

    private fun <T> guard(fkMessage: String? = null, block: () -> T): T = try {
        block()
    } catch (e: SQLiteConstraintException) {
        val m = e.message ?: ""
        throw UserError(
            when {
                "TRIGGER" in m -> m.substringBefore(" (code")      // сообщение из RAISE триггера
                "FOREIGN KEY" in m -> fkMessage ?: "Запись связана с другими данными"
                "UNIQUE" in m -> "Такая запись уже существует"
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

    fun refs(k: RefKind): List<Ref> = when (k) {
        RefKind.Category -> dao.categories()
        RefKind.Material -> dao.materials()
        RefKind.Style -> dao.styles()
        RefKind.Status -> dao.statuses()
    }

    fun saveRef(k: RefKind, id: Long, name: String) = guard {
        if (id == 0L) {
            when (k) {
                RefKind.Category -> dao.insertCategory(CategoryEntity(name = name))
                RefKind.Material -> dao.insertMaterial(MaterialEntity(name = name))
                RefKind.Style -> dao.insertStyle(StyleEntity(name = name))
                RefKind.Status -> dao.insertStatus(StatusEntity(name = name))
            }
        } else {
            when (k) {
                RefKind.Category -> dao.updateCategory(id, name)
                RefKind.Material -> dao.updateMaterial(id, name)
                RefKind.Style -> dao.updateStyle(id, name)
                RefKind.Status -> dao.updateStatus(id, name)
            }
        }
        Unit
    }

    fun deleteRef(k: RefKind, id: Long) = guard(k.usedMsg) {
        when (k) {
            RefKind.Category -> dao.deleteCategory(id)
            RefKind.Material -> dao.deleteMaterial(id)
            RefKind.Style -> dao.deleteStyle(id)
            RefKind.Status -> dao.deleteStatus(id)
        }
    }

    // ---------- товары ----------

    fun products(): List<Product> = dao.products()

    fun saveProduct(p: Product) = guard {
        if (p.id == 0L) {
            dao.insertProduct(ProductEntity(0, p.name, p.description, p.price, p.inStock, p.categoryId, p.materialId, p.styleId))
            Unit
        } else {
            dao.updateProduct(p.id, p.name, p.description, p.price, p.inStock, p.categoryId, p.materialId, p.styleId)
        }
    }

    fun deleteProduct(id: Long) = guard("Невозможно удалить товар, который входит в состав заказов или поставок") {
        dao.deleteProduct(id)
    }

    // ---------- клиенты ----------

    fun customers(): List<Customer> = dao.customers()

    fun saveCustomer(c: Customer) = guard {
        if (c.id == 0L) {
            dao.insertCustomer(CustomerEntity(0, c.firstName, c.lastName, c.phone, c.address))
            Unit
        } else {
            dao.updateCustomer(c.id, c.firstName, c.lastName, c.phone, c.address)
        }
    }

    fun deleteCustomer(id: Long) = guard("Невозможно удалить клиента, у которого есть заказы") {
        dao.deleteCustomer(id)
    }

    // ---------- поставщики ----------

    fun suppliers(): List<Supplier> = dao.suppliers()

    fun saveSupplier(s: Supplier) = guard {
        if (s.id == 0L) {
            dao.insertSupplier(SupplierEntity(0, s.company, s.contact, s.phone))
            Unit
        } else {
            dao.updateSupplier(s.id, s.company, s.contact, s.phone)
        }
    }

    fun deleteSupplier(id: Long) = guard("Невозможно удалить поставщика, связанного с существующими поставками") {
        dao.deleteSupplier(id)
    }

    // ---------- заказы ----------

    /** Заказы; итоговая сумма вычисляется агрегатным запросом (в таблице не хранится). */
    fun orders(from: String? = null, to: String? = null): List<OrderRow> =
        if (from != null && to != null) dao.ordersBetween(from, to) else dao.orders()

    fun orderLines(orderId: Long): List<OrderLine> = dao.orderLines(orderId)

    fun createOrder(customerId: Long, statusId: Long, date: String, items: List<Pair<Long, Int>>) =
        guard { dao.createOrder(customerId, statusId, date, items) }

    fun setOrderStatus(orderId: Long, statusId: Long) = guard { dao.setOrderStatus(orderId, statusId) }

    fun deleteOrder(id: Long) = guard { dao.deleteOrder(id) }

    // ---------- поставки ----------

    fun supplies(): List<SupplyRow> = dao.supplies()

    fun supplyLines(supplyId: Long): List<SupplyLine> = dao.supplyLines(supplyId)

    fun createSupply(date: String, lines: List<SupplyLine>) = guard { dao.createSupply(date, lines) }

    fun deleteSupply(id: Long) = guard { dao.deleteSupply(id) }

    // ---------- отчёты ----------

    fun popularProducts(): List<PopularRow> = dao.popularProducts()
}
