package com.example.studiest

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.database.getLongOrNull
import androidx.core.database.getStringOrNull
import java.io.ByteArrayOutputStream

class AnexoRepositorio (contexto: Context) {

    val helperAnexo: AnexoSQLHelper

    init {
        helperAnexo = AnexoSQLHelper(contexto)
    }

    fun inserir(anexo: Anexo): Long{
        val db = helperAnexo.writableDatabase

        val valores = ContentValues()
        valores.put(AnexoSQLHelper.COLUNA_TITULO, anexo.titulo)
        valores.put(AnexoSQLHelper.COLUNA_URI, anexo.arquivo)
        val id = db.insert(AnexoSQLHelper.NOME_TABELA, null, valores)
        db.close()
        return id
    }

    fun atualizar(anexo: Anexo): Int{
        val db = helperAnexo.writableDatabase

        val valores = ContentValues()
        valores.put(AnexoSQLHelper.COLUNA_TITULO, anexo.titulo)
        valores.put(AnexoSQLHelper.COLUNA_URI, anexo.arquivo)

        val numLinhasAfetadas = db.update(
            AnexoSQLHelper.NOME_TABELA,
            valores,
            "${AnexoSQLHelper.COLUNA_ID} = ?",
            arrayOf<String>("${anexo.id.toString()}"))
        db.close()
        return numLinhasAfetadas
    }

    fun excluir(anexo: Anexo): Int{
        val db = helperAnexo.writableDatabase

        val numLinhasAfetadas = db.delete(
            AnexoSQLHelper.NOME_TABELA,
            "${AnexoSQLHelper.COLUNA_ID} = ?",
            arrayOf<String>("${anexo.id.toString()}"))
        db.close()
        return numLinhasAfetadas
    }

    fun buscarAnexos(nome: String?): List<Anexo>{
        val db = helperAnexo.writableDatabase
        var sql ="SELECT * FROM ${AnexoSQLHelper.NOME_TABELA} "

        var argumentos: Array<String>? = null

        if(nome!=null){
            sql += " WHERE ${AnexoSQLHelper.COLUNA_TITULO} LIKE ? "
            argumentos = arrayOf<String>("$nome")
        }
       // sql += " ORDER BY ${AnexoSQLHelper.COLUNA_TITULO}"

        val cursor = db.rawQuery(sql, argumentos)

        val anexos: MutableList<Anexo> = ArrayList()
        while(cursor.moveToNext()){
            var id = cursor.getLongOrNull(cursor.getColumnIndex(AnexoSQLHelper.COLUNA_ID))
            var titulo = cursor.getStringOrNull(cursor.getColumnIndex(AnexoSQLHelper.COLUNA_TITULO))
            var uri = cursor.getStringOrNull(cursor.getColumnIndex(AnexoSQLHelper.COLUNA_URI))
            val anexo = Anexo(id!!, titulo!!, uri!!)
            anexos.add(anexo)
        }
        cursor.close()
        db.close()
        return anexos
    }

}