package ru.tisbi.furniture.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/*
 * Сущности Room. Каждый класс описывает одну таблицу базы данных; по этим описаниям
 * Room сам формирует инструкции CREATE TABLE. Названия таблиц и столбцов заданы явно,
 * чтобы на них можно было ссылаться в SQL-запросах и триггерах (см. Schema.kt).
 */

@Entity(tableName = "Categories", indices = [Index(value = ["Name"], unique = true)])
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "CategoryID") val id: Long = 0,
    @ColumnInfo(name = "Name") val name: String
)

@Entity(tableName = "Materials", indices = [Index(value = ["Name"], unique = true)])
data class MaterialEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "MaterialID") val id: Long = 0,
    @ColumnInfo(name = "Name") val name: String
)

@Entity(tableName = "Styles", indices = [Index(value = ["Name"], unique = true)])
data class StyleEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "StyleID") val id: Long = 0,
    @ColumnInfo(name = "Name") val name: String
)

@Entity(tableName = "Statuses", indices = [Index(value = ["Name"], unique = true)])
data class StatusEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "StatusID") val id: Long = 0,
    @ColumnInfo(name = "Name") val name: String
)

@Entity(tableName = "Customers", indices = [Index(value = ["LastName"])])
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "CustomerID") val id: Long = 0,
    @ColumnInfo(name = "FirstName") val firstName: String,
    @ColumnInfo(name = "LastName") val lastName: String,
    @ColumnInfo(name = "PhoneNumber") val phone: String,
    @ColumnInfo(name = "ShippingAddress") val address: String
)

@Entity(tableName = "Suppliers")
data class SupplierEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "SupplierID") val id: Long = 0,
    @ColumnInfo(name = "CompanyName") val company: String,
    @ColumnInfo(name = "ContactPerson") val contact: String,
    @ColumnInfo(name = "PhoneNumber") val phone: String
)

@Entity(
    tableName = "Products",
    foreignKeys = [
        ForeignKey(entity = CategoryEntity::class, parentColumns = ["CategoryID"], childColumns = ["CategoryID"]),
        ForeignKey(entity = MaterialEntity::class, parentColumns = ["MaterialID"], childColumns = ["MaterialID"]),
        ForeignKey(entity = StyleEntity::class, parentColumns = ["StyleID"], childColumns = ["StyleID"])
    ],
    indices = [
        Index(value = ["Name"]), Index(value = ["CategoryID"]),
        Index(value = ["MaterialID"]), Index(value = ["StyleID"])
    ]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "ProductID") val id: Long = 0,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "Description") val description: String?,
    @ColumnInfo(name = "Price") val price: Double,
    @ColumnInfo(name = "InStock") val inStock: Int,
    @ColumnInfo(name = "CategoryID") val categoryId: Long,
    @ColumnInfo(name = "MaterialID") val materialId: Long,
    @ColumnInfo(name = "StyleID") val styleId: Long
)

@Entity(
    tableName = "Orders",
    foreignKeys = [
        ForeignKey(entity = CustomerEntity::class, parentColumns = ["CustomerID"], childColumns = ["CustomerID"]),
        ForeignKey(entity = StatusEntity::class, parentColumns = ["StatusID"], childColumns = ["StatusID"])
    ],
    indices = [Index(value = ["CustomerID"]), Index(value = ["StatusID"]), Index(value = ["OrderDate"])]
)
data class OrderEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "OrderID") val id: Long = 0,
    @ColumnInfo(name = "CustomerID") val customerId: Long,
    @ColumnInfo(name = "StatusID") val statusId: Long,
    @ColumnInfo(name = "OrderDate") val date: String
)

@Entity(
    tableName = "OrderItems",
    foreignKeys = [
        ForeignKey(entity = OrderEntity::class, parentColumns = ["OrderID"], childColumns = ["OrderID"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ProductEntity::class, parentColumns = ["ProductID"], childColumns = ["ProductID"])
    ],
    indices = [Index(value = ["OrderID"]), Index(value = ["ProductID"])]
)
data class OrderItemEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "OrderItemID") val id: Long = 0,
    @ColumnInfo(name = "OrderID") val orderId: Long,
    @ColumnInfo(name = "ProductID") val productId: Long,
    @ColumnInfo(name = "Quantity") val quantity: Int
)

@Entity(tableName = "Supplies")
data class SupplyEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "SupplyID") val id: Long = 0,
    @ColumnInfo(name = "SupplyDate") val date: String
)

@Entity(
    tableName = "SupplyItems",
    foreignKeys = [
        ForeignKey(entity = SupplyEntity::class, parentColumns = ["SupplyID"], childColumns = ["SupplyID"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = ProductEntity::class, parentColumns = ["ProductID"], childColumns = ["ProductID"]),
        ForeignKey(entity = SupplierEntity::class, parentColumns = ["SupplierID"], childColumns = ["SupplierID"])
    ],
    indices = [Index(value = ["SupplyID"]), Index(value = ["ProductID"]), Index(value = ["SupplierID"])]
)
data class SupplyItemEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "SupplyItemID") val id: Long = 0,
    @ColumnInfo(name = "SupplyID") val supplyId: Long,
    @ColumnInfo(name = "ProductID") val productId: Long,
    @ColumnInfo(name = "SupplierID") val supplierId: Long,
    @ColumnInfo(name = "Quantity") val quantity: Int
)
