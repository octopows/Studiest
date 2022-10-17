package com.example.studiest

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class recuperar_senha : AppCompatActivity() {

    private lateinit var builder: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)


        builder = Dialog(this)
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

            //cria instancia
            var validaEmail = ValidaEmail()
            validaEmail.execute(email)

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

    inner class ValidaEmail: AsyncTask<String?, Void, String?>(){
        override fun onPreExecute() {
            super.onPreExecute()
            builder.setCancelable(false)
            builder.show()
        }

        override fun doInBackground(vararg params:String?): String?{
            //pega os parametros email e senha enviado pela interface
            var email = params[0] as String

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("email", email)

            //tentar estabelecer conexão com a internet
            try {
                val url = URL("http://studiestoficial.000webhostapp.com/app/enviaEmail.php")
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

            val campoEmailRecuperar = findViewById<EditText>(R.id.campoEmailRecuperar)
            val campoErroRecuperar = findViewById<TextView>(R.id.campoErroRecuperar)
            var email = campoEmailRecuperar.text.toString()
            //verificando se o usuario retornado foi nao nulo
            if (resultado == "sim") {
                campoErroRecuperar.text = ""
                campoEmailRecuperar.setBackgroundResource(R.drawable.custom_input)
                val intent = Intent(applicationContext, verificar_email::class.java)
                intent.putExtra("email",email)
                finish()
                startActivity(intent)
            }else{
                campoErroRecuperar.text = "Email inválido. Tente Novamente."
                campoEmailRecuperar.setBackgroundResource(R.drawable.custom_input_error)
                campoEmailRecuperar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
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