package com.example.biblioteca.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

//questa classe gestisce un db locale per memorizzare le informazione dell'untente che ha effettuato l'accesso nell'app

class DBManager(val context: Context) {
    private lateinit var db_helper: LocalDBHelper
    private lateinit var db: SQLiteDatabase

    fun open() : DBManager{ //apro connessione
        db_helper = LocalDBHelper(context)
        db = db_helper.writableDatabase
        return this
    }

    fun close(){//chiudo connessione
        db_helper.close()
    }

    fun insert(username: String,nome: String, cognome: String,qr:String, type:String){ //insert di una tupla nel db locale
        val value = ContentValues().apply{
            put(LocalDBHelper.USERNAME,username)
            put(LocalDBHelper.NOME,nome)
            put(LocalDBHelper.COGNOME,cognome)
            put(LocalDBHelper.QR,qr)
            put(LocalDBHelper.TYPE,type)
        }
        db.insert(LocalDBHelper.TABLE_NAME, null, value)
    }

    fun delete(){//cancello il db locale
        val n= db.delete(LocalDBHelper.TABLE_NAME, null, null)
    }
    fun getUser():Cursor{
        val projection = arrayOf(LocalDBHelper.ID, LocalDBHelper.USERNAME,LocalDBHelper.NOME,LocalDBHelper.COGNOME, LocalDBHelper.QR, LocalDBHelper.TYPE /*, LocalDBHelper.ISLOGGED*/)
        val cursor = db.query(
            LocalDBHelper.TABLE_NAME, projection, null, null, null, null, null
        )
        cursor?.moveToFirst()
        return cursor
    }
}