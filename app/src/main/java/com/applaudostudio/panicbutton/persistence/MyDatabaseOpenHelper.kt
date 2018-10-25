package com.applaudostudio.panicbutton.persistence

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.applaudostudio.panicbutton.model.ContactDBModel
import org.jetbrains.anko.db.*

class MyDatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {
    companion object {
        private var instance: MyDatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): MyDatabaseOpenHelper {
            if (instance == null) {
                instance = MyDatabaseOpenHelper(ctx.getApplicationContext())
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable(ContactDBModel.TABLENAME, true,
                ContactDBModel.COLUMN_ID to INTEGER + PRIMARY_KEY + UNIQUE + AUTOINCREMENT,
                ContactDBModel.COLUMN_NAME to TEXT,
                ContactDBModel.COLUMN_PHONE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable(ContactDBModel.TABLENAME, true)
    }
}
