package com.example.biblioteca.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class LocalDBHelper(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME,null,
    DATABASE_VERSION
) {
    companion object{
        const val DATABASE_NAME="database.db"
        const val DATABASE_VERSION=1
        const val TABLE_NAME="localUser"
        const val USERNAME="username"
        const val NOME="nome"
        const val COGNOME="cognome"
        const val TYPE="type"
        const val QR="qr"
        const val ID = "id"

        private const val DB_CREATE=
            "CREATE TABLE" +                                            //creo la tabella utente nel db locale
                    " ${TABLE_NAME} (${ID} INTEGER PRIMARY KEY, "+
                    "${USERNAME} TEXT, "+
                    "${NOME} TEXT, "+
                    "${COGNOME} TEXT, "+
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
