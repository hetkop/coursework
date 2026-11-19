package ru.tisbi.furniture.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/** Помощник SQLite: создаёт схему, триггеры и начальные данные при первом запуске. */
class Db(context: Context) : SQLiteOpenHelper(context, "furniture_store.db", null, 1) {

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }

    override fun onCreate(db: SQLiteDatabase) {
        (Schema.tables + Schema.indexes + Schema.triggers + Schema.seed).forEach { db.execSQL(it) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) = Unit
}
