package ru.tisbi.furniture.data

/** Справочники (таблицы, создаваемые Room из сущностей): имя таблицы, ключевое поле и сообщение при блокировке удаления. */
enum class RefKind(val table: String, val idCol: String, val title: String, val usedMsg: String) {
    Category("Categories", "CategoryID", "Категории", "Невозможно удалить категорию, к которой привязаны товары"),
    Material("Materials", "MaterialID", "Материалы", "Невозможно удалить материал, к которому привязаны товары"),
    Style("Styles", "StyleID", "Стили", "Невозможно удалить стиль, к которому привязаны товары"),
    Status("Statuses", "StatusID", "Статусы заказов", "Невозможно удалить статус, который используется в существующих заказах")
}

data class Ref(val id: Long, val name: String)

data class Product(
    val id: Long, val name: String, val description: String, val price: Double, val inStock: Int,
    val categoryId: Long, val materialId: Long, val styleId: Long,
    val categoryName: String = "", val materialName: String = "", val styleName: String = ""
)

data class Customer(val id: Long, val firstName: String, val lastName: String, val phone: String, val address: String)

val Customer.fullName: String get() = "$lastName $firstName"

data class Supplier(val id: Long, val company: String, val contact: String, val phone: String)

data class OrderRow(
    val id: Long, val customerId: Long, val customer: String, val statusId: Long, val status: String,
    val date: String, val total: Double, val qty: Int
)

data class OrderLine(val productId: Long, val product: String, val price: Double, val qty: Int)

val OrderLine.sum: Double get() = price * qty

data class SupplyRow(val id: Long, val date: String, val positions: Int, val qty: Int)

data class SupplyLine(val productId: Long, val product: String, val supplierId: Long, val supplier: String, val qty: Int)

data class PopularRow(val product: String, val category: String, val qty: Int, val revenue: Double)

/** Полный снимок данных, который кэшируется во ViewModel и обновляется после каждой записи. */
data class AppData(
    val categories: List<Ref> = emptyList(),
    val materials: List<Ref> = emptyList(),
    val styles: List<Ref> = emptyList(),
    val statuses: List<Ref> = emptyList(),
    val customers: List<Customer> = emptyList(),
    val suppliers: List<Supplier> = emptyList(),
    val products: List<Product> = emptyList(),
    val orders: List<OrderRow> = emptyList(),
    val supplies: List<SupplyRow> = emptyList()
)
