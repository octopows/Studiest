package com.example.studiest

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class deletar_conta : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var builder: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deletar_conta)

        builder = Dialog(this)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnConfirmarDeletar = findViewById<Button>(R.id.btnConfirmarDeletar)
        val nomeUsuario = findViewById<TextView>(R.id.nomeUsuario)
        val emailUsuario = findViewById<TextView>(R.id.emailUsuario)

        //colocando informações do usuário
        val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
        var nome = sharedPreference.getString("nome","Null")
        var email = sharedPreference.getString("email","Null")

        nomeUsuario.text = "$nome"
        emailUsuario.text = "$email"


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

        val cancelarDeletar = view.findViewById<TextView>(R.id.cancelarRemoverConta)
        val confirmarDeletar = view.findViewById<TextView>(R.id.confirmarRemoverConta)

        cancelarDeletar.setOnClickListener { dialog.dismiss() }

        confirmarDeletar.setOnClickListener {
            val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
            var email = sharedPreference.getString("email","Null")
            //cria instancia
            var deletaConta = DeletaConta()
            deletaConta.execute(email)
        }
        dialog = build.create()
        dialog.show()
    }

    inner class DeletaConta: AsyncTask<String?, Void, String?>(){
        override fun onPreExecute() {
            super.onPreExecute()
            builder.setCancelable(false)
            builder.show()
            dialog.dismiss()

        }

        override fun doInBackground(vararg params:String?): String?{
            //pega os parametros email e senha enviado pela interface
            var email = params[0] as String

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("email", email)

            //tentar estabelecer conexão com a internet
            try {
                val url = URL("http://studiestoficial.000webhostapp.com/app/deletaConta.php")
                val conexao = (url.openConnection() as HttpURLConnection)

                conexao.readTimeout = 10000
                conexao.connectTimeout = 10000
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
            val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)

            //verificando se o usuario retornado foi nao nulo
            if (resultado == "sim") {
                sharedPreference.edit().clear().commit();
                val intent = Intent(applicationContext, bemvindo::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("EXIT", true)
                finish()
                startActivity(intent)
            }else{
                Toast.makeText(this@deletar_conta, "Erro. Tente Novamente.", Toast.LENGTH_SHORT).show()
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