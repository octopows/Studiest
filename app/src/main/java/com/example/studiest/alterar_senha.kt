package com.example.studiest

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class alterar_senha : AppCompatActivity() {
    private lateinit var builder: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alterar_senha)

        builder = Dialog(this)
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
            var senha = campoAlterarSenha.text.toString()
            var confirmarSenha = campoConfirmarAlterar.text.toString()

            if(senha.length>=6 && senha == confirmarSenha){
                campoErroAlterar.text = ""
                campoAlterarSenha.setBackgroundResource(R.drawable.custom_input)
                campoConfirmarAlterar.setBackgroundResource(R.drawable.custom_input)
                //cria instancia
                var alteraSenha = AlteraSenha()
                alteraSenha.execute(senha)
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

    inner class AlteraSenha: AsyncTask<String?, Void, String?>(){
        override fun onPreExecute() {
            super.onPreExecute()
            builder.setCancelable(false)
            builder.show()
        }

        override fun doInBackground(vararg params:String?): String?{
            //pega os parametros email e senha enviado pela interface
            var senha = params[0] as String

            val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
            var email = sharedPreference.getString("email","Null") as String

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("novaSenha", senha)
            jsonUsuario.put("email", email)

            //tentar estabelecer conexão com a internet
            try {
                val url = URL("http://192.168.1.11:8080/Studiest/alteraSenha.php")
                val conexao = (url.openConnection() as HttpURLConnection)

                conexao.readTimeout = 10000
                conexao.connectTimeout = 15000
                conexao.requestMethod = "POST"
                conexao.doInput = true
                conexao.doOutput = true
                conexao.setRequestProperty("Content-Type", "application/json")
                conexao.connect()

                //define uma variável para utilizar o fluxo de saída da conexão
                var outputStream: OutputStream = conexao.outputStream
                //escreve no fluxo de saí da os bytes que compoem o json a ser enviado
                outputStream.write(jsonUsuario.toString().encodeToByteArray())
                //finaliza a conexão
                outputStream.flush()
                outputStream.close()

                //verifica a resposta da página
                val responseCode = conexao.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //define uma variável para representar o fluxo de entrda
                    val inputStream = conexao.inputStream
                    //chama o método que transformará os bytes em String
                    //armazena a string na variavel resultado
                    var resultado = streamToString(inputStream)

                    //retorna o resultado
                    return resultado

                }

            } catch(e:Exception){
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(resultado: String?) {
            super.onPostExecute(resultado)

            builder.dismiss()

            //verificando se o usuario retornado foi nao nulo
            if (resultado == "sim") {
                Toast.makeText(applicationContext, "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show()
            }
        }
        //função de validar erros

        private fun streamToString(inputStream: InputStream): String {
            val buffer = ByteArray(1024)
            val dados = ByteArrayOutputStream()
            var bytesRead: Int
            while (true) {
                bytesRead = inputStream.read(buffer)
                if (bytesRead == -1) break
                dados.write(buffer, 0, bytesRead)
            }
            return String(dados.toByteArray(), Charset.forName("UTF-8"))
        }
    }
}