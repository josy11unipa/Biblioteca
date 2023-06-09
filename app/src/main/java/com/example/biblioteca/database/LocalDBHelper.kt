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
        const val NOME="nome"
        const val COGNOME="cognome"
        const val TYPE="type"
        const val QR="qr"
        const val ID = "id"
        //const val ISLOGGED = "flag"

        private const val DB_CREATE=
            "CREATE TABLE" +
                    " ${TABLE_NAME} (${ID} INTEGER PRIMARY KEY, "+
                    "${USERNAME} TEXT, "+
                    "${NOME} TEXT, "+
                    "${COGNOME} TEXT, "+
                    "${TYPE} TEXT, "+
                    //"${ISLOGGED} BOOLEAN, "+
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
