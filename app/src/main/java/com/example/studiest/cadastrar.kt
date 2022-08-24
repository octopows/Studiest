package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*

class cadastrar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        var i: Int = 1
        var j: Int = 1
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnConfirmarCadastrar = findViewById<Button>(R.id.btnConfirmarCadastrar)
        val btnNaoPossuiConta = findViewById<TextView>(R.id.btnNaoPossuiConta)
        val campoNome = findViewById<EditText>(R.id.campoAlterarNome)
        val campoEmailCadastrar = findViewById<EditText>(R.id.campoEmailCadastrar)
        val campoSenhaCadastrar = findViewById<EditText>(R.id.campoSenhaCadastrar)
        val campoConfirmarSenha = findViewById<EditText>(R.id.campoConfirmarSenha)
        val campoErro2 = findViewById<TextView>(R.id.campoErro2)
        val btnMostrarSenha2 = findViewById<ImageView>(R.id.btnMostrarSenha2)
        val btnMostrarSenha3 = findViewById<ImageView>(R.id.btnMostrarSenha3)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }
        btnNaoPossuiConta.setOnClickListener{
            finish()
            val intent = Intent(this, entrar::class.java)
            startActivity(intent)
        }

        //verificarCampos
        fun validarCampos(){
            var email = campoEmailCadastrar.text.toString()
            var senha = campoSenhaCadastrar.text.toString()
            var confirmarSenha = campoConfirmarSenha.text.toString()
            var nome = campoNome.text.toString()

            if(senha.length>=6 && senha == confirmarSenha){
                campoErro2.text = ""
                campoNome.setBackgroundResource(R.drawable.custom_input)
                campoEmailCadastrar.setBackgroundResource(R.drawable.custom_input)
                campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input)
                campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
                Toast.makeText(applicationContext, "Cadastro CONCLUÍDO com sucesso!!", Toast.LENGTH_SHORT).show()
            }
            else if(senha.length<6 && senha == confirmarSenha){
                campoErro2.text = "A senha deve ter no mínimo 6 caracteres."
                campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input_error)
                campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
                campoSenhaCadastrar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cadeado_senha, 0, 0, 0);
            }
            else{
                campoErro2.text = "As senhas não coincidem. Tente novamente."
                campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input_error)
                campoSenhaCadastrar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cadeado_senha, 0, 0, 0);
                campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input_error)
                campoConfirmarSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cadeado_senha, 0, 0, 0);
            }

        }

        //mudar os onfocus
        campoSenhaCadastrar.setOnFocusChangeListener { campoSenhaCadastrar, hasFocus ->
            if (hasFocus) {
                campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input)
                campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
            }
        }

        campoConfirmarSenha.setOnFocusChangeListener { campoConfirmarSenha, hasFocus ->
            if (hasFocus) {
                campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
                campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input)
            }
        }

        //Onclick q chama a função
        btnConfirmarCadastrar.setOnClickListener{
            validarCampos()
        }

        campoSenhaCadastrar.setOnClickListener{
            campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
            campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input)
            campoSenhaCadastrar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
            campoConfirmarSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
        }

        campoConfirmarSenha.setOnClickListener{
            campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
            campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input)
            campoConfirmarSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
            campoSenhaCadastrar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
        }



        //mostrar senha 2
        btnMostrarSenha2.setOnClickListener {
            if (i == 1) {
                btnMostrarSenha2.setImageResource(R.drawable.senha_mostrada)
                i = 0
                campoSenhaCadastrar.transformationMethod= HideReturnsTransformationMethod.getInstance()
            } else {
                btnMostrarSenha2.setImageResource(R.drawable.senha_ocultada)
                i = 1
                campoSenhaCadastrar.transformationMethod= PasswordTransformationMethod.getInstance()
            }
        }

        //mostrar senha 3
        btnMostrarSenha3.setOnClickListener {
            if(j==1){
                btnMostrarSenha3.setImageResource(R.drawable.senha_mostrada)
                j=0
                campoConfirmarSenha.transformationMethod= HideReturnsTransformationMethod.getInstance()
            }
            else{
                btnMostrarSenha3.setImageResource(R.drawable.senha_ocultada)
                j=1
                campoConfirmarSenha.transformationMethod= PasswordTransformationMethod.getInstance()
            }
        }
    }
}