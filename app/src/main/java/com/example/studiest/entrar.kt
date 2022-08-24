package com.example.studiest

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class entrar : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrar)

        var check: Int = 1
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnEsqueceuSenha = findViewById<TextView>(R.id.btnEsqueceuSenha)
        val btnNaoPossuiConta = findViewById<TextView>(R.id.btnNaoPossuiConta)
        val btnConfirmarEntrar = findViewById<Button>(R.id.btnConfirmarEntrar)
        var campoEmail = findViewById<EditText>(R.id.campoEmail)
        val campoSenha = findViewById<EditText>(R.id.campoSenha)
        var btnMostrarSenha = findViewById<ImageView>(R.id.btnMostrarSenha2)
        val campoErro = findViewById<TextView>(R.id.campoErro)



        //função de validar erros
        fun validarCampos(){
            var email = campoEmail.text.toString()
            var senha = campoSenha.text.toString()
            if(email != "" && senha != ""){
                campoErro.text = ""
                campoEmail.setBackgroundResource(R.drawable.custom_input)
                campoSenha.setBackgroundResource(R.drawable.custom_input)
                Toast.makeText(applicationContext, "Dados Válidos!", Toast.LENGTH_SHORT).show()
                finish()
                val intent = Intent(this, tela_principal::class.java)
                startActivity(intent)
            }
            else{
                campoErro.text = "Email ou senha inválidos"
                campoEmail.setBackgroundResource(R.drawable.custom_input_error)
                campoSenha.setBackgroundResource(R.drawable.custom_input_error)
                campoEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
                campoSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.cadeado_senha, 0, 0, 0);
            }

        }
        //botao para apertar pra entrar
        btnConfirmarEntrar.setOnClickListener {
            validarCampos()
        }

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //apertar na frase pra ir direto pro cadastro
        btnNaoPossuiConta.setOnClickListener{
            finish()
            val intent = Intent(this, cadastrar::class.java)
            startActivity(intent)
        }

        //apertar no Esqueceu sua senha?
        btnEsqueceuSenha.setOnClickListener{
            val intent = Intent(this, recuperar_senha::class.java)
            startActivity(intent)

        }

        campoEmail.setOnClickListener{
            campoEmail.setBackgroundResource(R.drawable.custom_input)
            campoSenha.setBackgroundResource(R.drawable.custom_input)
            campoEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email, 0, 0, 0);
        }

        campoSenha.setOnClickListener{
            campoEmail.setBackgroundResource(R.drawable.custom_input)
            campoSenha.setBackgroundResource(R.drawable.custom_input)
            campoSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
        }

        campoEmail.setOnFocusChangeListener { campoEmail, hasFocus ->
            if (hasFocus){
                campoEmail.setBackgroundResource(R.drawable.custom_input)
                campoSenha.setBackgroundResource(R.drawable.custom_input)
            }
        }

        campoSenha.setOnFocusChangeListener { campoSenha, hasFocus ->
            if (hasFocus){
                campoEmail.setBackgroundResource(R.drawable.custom_input)
                campoSenha.setBackgroundResource(R.drawable.custom_input)
            }
        }


        //mostrar senha
        btnMostrarSenha.setOnClickListener {
            if(check==1){
                btnMostrarSenha.setImageResource(R.drawable.senha_mostrada)
                check=0
                campoSenha.transformationMethod=HideReturnsTransformationMethod.getInstance()
            }
            else{
                btnMostrarSenha.setImageResource(R.drawable.senha_ocultada)
                check=1
                campoSenha.transformationMethod=PasswordTransformationMethod.getInstance()
            }

        }
    }
}