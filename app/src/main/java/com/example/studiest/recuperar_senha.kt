package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.w3c.dom.Text

class recuperar_senha : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val campoEmailRecuperar = findViewById<EditText>(R.id.campoEmailRecuperar)
        val btnConfirmarRecuperar = findViewById<Button>(R.id.btnConfirmarRecuperar)
        val campoErroRecuperar = findViewById<TextView>(R.id.campoErroRecuperar)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //validar email
        fun validarRecuperar(){
            var email = campoEmailRecuperar.text.toString()
            if(email == "david@gmail.com" || email == "geovanna@gmail.com" ){
                campoErroRecuperar.text = ""
                campoEmailRecuperar.setBackgroundResource(R.drawable.custom_input)
                finish()
                val intent = Intent(this, verificar_email::class.java)
                startActivity(intent)
            }
            else{
                campoErroRecuperar.text = "Email inválido. Tente Novamente."
                campoEmailRecuperar.setBackgroundResource(R.drawable.custom_input_error)
                campoEmailRecuperar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
            }
        }

        //botão confirmar
        btnConfirmarRecuperar.setOnClickListener {
            validarRecuperar()
        }


        //ajustar cores da borda e do icon
        campoEmailRecuperar.setOnClickListener{
            campoEmailRecuperar.setBackgroundResource(R.drawable.custom_input)
            campoEmailRecuperar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email, 0, 0, 0);
        }

        campoEmailRecuperar.setOnFocusChangeListener { campoEmailRecuperar, hasFocus ->
            if (hasFocus){
                campoEmailRecuperar.setBackgroundResource(R.drawable.custom_input)
            }
        }
    }
}