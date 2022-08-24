package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*

class alterar_senha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_senha)

        var i: Int = 1
        var j: Int = 1
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val campoAlterarSenha = findViewById<EditText>(R.id.campoAlterarSenha)
        val campoConfirmarAlterar = findViewById<EditText>(R.id.campoConfirmarAlterar)
        val btnMostrarSenha = findViewById<ImageView>(R.id.btnMostrarSenha)
        val btnMostrarSenha2 = findViewById<ImageView>(R.id.btnMostrarSenha2)
        val campoErroAlterar = findViewById<TextView>(R.id.campoErroAlterar)
        val btnConfirmarAlterar = findViewById<Button>(R.id.btnConfirmarAlterar)


        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //verificarCampos
        fun validarCampos(){
            var senha = campoAlterarSenha.text.toString()
            var confirmarSenha = campoConfirmarAlterar.text.toString()

            if(senha.length>=6 && senha == confirmarSenha){
                campoErroAlterar.text = ""
                campoAlterarSenha.setBackgroundResource(R.drawable.custom_input)
                campoConfirmarAlterar.setBackgroundResource(R.drawable.custom_input)
                Toast.makeText(applicationContext, "Senha Alterada!", Toast.LENGTH_SHORT).show()
            }
            else if(senha.length<6 && senha == confirmarSenha){
                campoErroAlterar.text = "A senha deve ter no mínimo 6 caracteres."
                campoAlterarSenha.setBackgroundResource(R.drawable.custom_input_error)
                campoConfirmarAlterar.setBackgroundResource(R.drawable.custom_input)
                campoAlterarSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cadeado_senha, 0, 0, 0);
            }
            else{
                campoErroAlterar.text = "As senhas não coincidem. Tente novamente."
                campoAlterarSenha.setBackgroundResource(R.drawable.custom_input_error)
                campoAlterarSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cadeado_senha, 0, 0, 0);
                campoConfirmarAlterar.setBackgroundResource(R.drawable.custom_input_error)
                campoConfirmarAlterar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cadeado_senha, 0, 0, 0);
            }

        }

        //mudar os onfocus
        campoAlterarSenha.setOnFocusChangeListener { campoAlterarSenha, hasFocus ->
            if (hasFocus) {
                campoAlterarSenha.setBackgroundResource(R.drawable.custom_input)
                campoConfirmarAlterar.setBackgroundResource(R.drawable.custom_input)
            }
        }

        campoConfirmarAlterar.setOnFocusChangeListener { campoConfirmarAlterar, hasFocus ->
            if (hasFocus) {
                campoConfirmarAlterar.setBackgroundResource(R.drawable.custom_input)
                campoAlterarSenha.setBackgroundResource(R.drawable.custom_input)
            }
        }

        //Onclick q chama a função
        btnConfirmarAlterar.setOnClickListener{
            validarCampos()
        }

        //mudar cor com setonclick
        campoAlterarSenha.setOnClickListener{
            campoConfirmarAlterar.setBackgroundResource(R.drawable.custom_input)
            campoAlterarSenha.setBackgroundResource(R.drawable.custom_input)
            campoAlterarSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
            campoConfirmarAlterar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
        }

        campoConfirmarAlterar.setOnClickListener{
            campoConfirmarAlterar.setBackgroundResource(R.drawable.custom_input)
            campoAlterarSenha.setBackgroundResource(R.drawable.custom_input)
            campoConfirmarAlterar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
            campoAlterarSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
        }

        //mostrar senha
        btnMostrarSenha.setOnClickListener {
            if (i == 1) {
                btnMostrarSenha.setImageResource(R.drawable.senha_mostrada)
                i = 0
                campoAlterarSenha.transformationMethod= HideReturnsTransformationMethod.getInstance()
            } else {
                btnMostrarSenha.setImageResource(R.drawable.senha_ocultada)
                i = 1
                campoAlterarSenha.transformationMethod= PasswordTransformationMethod.getInstance()
            }
        }

        //mostrar senha 2
        btnMostrarSenha2.setOnClickListener {
            if(j==1){
                btnMostrarSenha2.setImageResource(R.drawable.senha_mostrada)
                j=0
                campoConfirmarAlterar.transformationMethod= HideReturnsTransformationMethod.getInstance()
            }
            else{
                btnMostrarSenha2.setImageResource(R.drawable.senha_ocultada)
                j=1
                campoConfirmarAlterar.transformationMethod= PasswordTransformationMethod.getInstance()
            }
        }
    }
}