package com.example.biblioteca.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDBHelper(context: Context): SQLiteOpenHelper(context,
    DaTABASE_NAME,null,
    DATABASE_VERSION
) {

    companion object{
        const val DaTABASE_NAME="LocalDB.db"
        const val DATABASE_VERSION=1
        const val TABLE_NAME="localUser"
        const val USERNAME="username"
        const val TYPE="type"
        const val QR="qr"
        private const val DB_CREATE=
            "CREATE TABLE" +
                    " ${TABLE_NAME} (${USERNAME} INTEGER PRIMARY KEY, "+
                    "${TYPE} TEXT, "+
                    "${QR} TEXT );"
        const val SQL_DELETE="DROP TABLE "+
                "IF EXISTS $TABLE_NAME;"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DB_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE)
        onCreate(db)
    }
}