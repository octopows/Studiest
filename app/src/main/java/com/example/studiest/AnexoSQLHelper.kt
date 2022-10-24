package com.example.studiest

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AnexoSQLHelper (context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, VERSAO) {

    companion object {
        private const val DB_NAME = "STUDIEST"
        private const val VERSAO = 1
        const val NOME_TABELA = "Anexo"
        const val COLUNA_ID = "_id"
        const val COLUNA_TITULO = "titulo"
        const val COLUNA_URI = "uri"
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        val sql = "CREATE TABLE $NOME_TABELA (" +
                "$COLUNA_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUNA_TITULO TEXT NOT NULL, " +
                "$COLUNA_URI TEXT NOT NULL)"
        sqLiteDatabase.execSQL(sql)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS $NOME_TABELA")
        onCreate(sqLiteDatabase)
    }

}