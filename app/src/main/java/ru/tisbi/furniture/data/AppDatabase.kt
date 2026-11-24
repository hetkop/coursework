package ru.tisbi.furniture.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * База данных приложения на основе Room (надстройка над SQLite).
 * Таблицы создаются по описаниям сущностей; триггеры и начальные данные
 * добавляются при первом создании базы в обратном вызове onCreate.
 */
@Database(
    entities = [
        CategoryEntity::class, MaterialEntity::class, StyleEntity::class, StatusEntity::class,
        CustomerEntity::class, SupplierEntity::class, ProductEntity::class, OrderEntity::class,
        OrderItemEntity::class, SupplyEntity::class, SupplyItemEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dao(): StoreDao

    companion object {
        fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "furniture_store_room.db")
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        (Schema.triggers + Schema.seed).forEach { db.execSQL(it) }
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        db.execSQL("PRAGMA foreign_keys = ON")
                    }
                })
                .build()
    }
}
