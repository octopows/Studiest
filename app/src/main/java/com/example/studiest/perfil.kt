package com.example.studiest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class perfil : AppCompatActivity() {
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val retangulo_not2 = findViewById<View>(R.id.retangulo_not2)
        val alterarSenha2 = findViewById<TextView>(R.id.alterarSenha2)
        val setaAlterarSenha = findViewById<ImageView>(R.id.setaAlterarSenha)
        val textoDeletarConta = findViewById<TextView>(R.id.textoDeletarConta)
        val setaDeletarConta = findViewById<ImageView>(R.id.imageView12)
        val textoSair = findViewById<TextView>(R.id.textoSair)
        val btnAlterarNome = findViewById<ImageView>(R.id.btnAlterarNome)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }
        //abrir alterar senha
        setaAlterarSenha.setOnClickListener{
            val intent = Intent(this, alterar_senha::class.java)
            startActivity(intent)
        }
        retangulo_not2.setOnClickListener{
            val intent = Intent(this, alterar_senha::class.java)
            startActivity(intent)
        }
        alterarSenha2.setOnClickListener{
            val intent = Intent(this, alterar_senha::class.java)
            startActivity(intent)
        }

        //abrir deletar conta
        textoDeletarConta.setOnClickListener{
            val intent = Intent(this, deletar_conta::class.java)
            startActivity(intent)
        }
        setaDeletarConta.setOnClickListener{
            val intent = Intent(this, deletar_conta::class.java)
            startActivity(intent)
        }

        //abrir dialog para confirmar sair de conta
        textoSair.setOnClickListener{
            showDialogSair()
        }
        //abrir dialog para alterar nome
        btnAlterarNome.setOnClickListener{
            showDialogAlterarNome()
        }
    }


    //função para chamar dialog sair
    private fun showDialogSair(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_sair, null)
        build.setView(view)

        val cancelarSair = view.findViewById<TextView>(R.id.cancelarRemoverResumo)
        val confirmarSair = view.findViewById<TextView>(R.id.confirmarRemoverResumo)

        cancelarSair.setOnClickListener { dialog.dismiss() }

        confirmarSair.setOnClickListener {
            val intent = Intent(applicationContext, bemvindo::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("EXIT", true)
            startActivity(intent)
        }
        dialog = build.create()
        dialog.show()
    }

    //função para chamar dialog sair
    private fun showDialogAlterarNome(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_alterar_nome, null)
        build.setView(view)

        val nomeUsuario = findViewById<TextView>(R.id.nomeUsuario)
        val cancelarAlterarNome = view.findViewById<TextView>(R.id.cancelarAlterarNome)
        val confirmarAlterarNome = view.findViewById<TextView>(R.id.confirmarAlterarNome)
        val campoAlterarNome = view.findViewById<EditText>(R.id.campoAlterarNome)

        cancelarAlterarNome.setOnClickListener { dialog.dismiss() }

        confirmarAlterarNome.setOnClickListener {
            var campoNome = campoAlterarNome.text.toString()

            if(campoNome.isNotEmpty()){
                nomeUsuario.text = campoNome
                dialog.dismiss()
            }
            else{
                campoAlterarNome.error = if(campoNome.isEmpty()) "Insira seu nome" else null
            }

        }

        dialog = build.create()
        dialog.show()
    }
}