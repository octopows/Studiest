package com.example.studiest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class adicionar_resumo : AppCompatActivity() {

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_resumo)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnDeletarResumo = findViewById<ImageView>(R.id.btnDeletarResumo)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //dialog box de deletar resumo
        btnDeletarResumo.setOnClickListener{
            showDialogRemoverResumo()
        }


    }
    //função para chamar dialog sair
    private fun showDialogRemoverResumo(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_remover_resumo, null)
        build.setView(view)

        val cancelarRemoverResumo = view.findViewById<TextView>(R.id.cancelarRemoverResumo)
        val confirmarRemoverResumo = view.findViewById<TextView>(R.id.confirmarRemoverResumo)

        cancelarRemoverResumo.setOnClickListener { dialog.dismiss() }

        confirmarRemoverResumo.setOnClickListener {
            finish()
        }
        dialog = build.create()
        dialog.show()
    }
}