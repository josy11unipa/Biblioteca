package com.example.biblioteca.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

class DBManager(val context: Context) {
    private lateinit var db_helper: LocalDBHelper
    private lateinit var db: SQLiteDatabase

    fun open() : DBManager{
        db_helper = LocalDBHelper(context)
        db = db_helper.writableDatabase
        return this
    }

    fun close(){
        db_helper.close()
    }

    fun insert(username: String,nome: String, cognome: String,qr:String, type:String /*, flag:Boolean*/){
        val value = ContentValues().apply{
            put(LocalDBHelper.USERNAME,username)
            put(LocalDBHelper.NOME,nome)
            put(LocalDBHelper.COGNOME,cognome)
            put(LocalDBHelper.QR,qr)
            put(LocalDBHelper.TYPE,type)
            //put(LocalDBHelper.ISLOGGED,flag)
        }
        db.insert(LocalDBHelper.TABLE_NAME, null, value)
    }

    fun update(username:String, name: String, desc: String) : Int{
      /*  val selection = "${LocalDBHelper.USERNAME} = ? "
        val selectionArgs = arrayOf(username.toString())

        val values = ContentValues().apply{
            put(DbHelper.SUBJECT,name)
            put(DbHelper.DESC,desc)
        }
        val n = db.update(DbHelper.TABLE_NAME, values, selection, selectionArgs)
       */ return 1 //n
    }
    fun delete(){

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

  /*  fun fetchAll(): Cursor {
        val projection = arrayOf(DbHelper.ID, DbHelper.SUBJECT, DbHelper.DESC)
        val cursor = db.query(
            DbHelper.TABLE_NAME, projection, null, null, null, null, null
        )
        cursor?.moveToFirst()
        return cursor
    }*/

}