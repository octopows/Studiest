package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class deletar_conta : AppCompatActivity() {
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deletar_conta)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnConfirmarDeletar = findViewById<Button>(R.id.btnConfirmarDeletar)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //botão de deletar
        btnConfirmarDeletar.setOnClickListener {
            showDialogDeletar()
        }
    }

    //função para chamar dialog sair
    private fun showDialogDeletar(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_deletar, null)
        build.setView(view)

        val cancelarDeletar = view.findViewById<TextView>(R.id.cancelarRemoverResumo)
        val confirmarDeletar = view.findViewById<TextView>(R.id.confirmarRemoverResumo)

        cancelarDeletar.setOnClickListener { dialog.dismiss() }

        confirmarDeletar.setOnClickListener {
            val intent = Intent(applicationContext, bemvindo::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("EXIT", true)
            startActivity(intent)
        }
        dialog = build.create()
        dialog.show()
    }
}