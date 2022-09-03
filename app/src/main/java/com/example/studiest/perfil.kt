package com.example.studiest

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.AsyncTask.execute
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class perfil : AppCompatActivity() {
    private lateinit var dialog: AlertDialog
    private lateinit var builder: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        builder = Dialog(this)
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val retangulo_not2 = findViewById<View>(R.id.retangulo_not2)
        val alterarSenha2 = findViewById<TextView>(R.id.alterarSenha2)
        val setaAlterarSenha = findViewById<ImageView>(R.id.setaAlterarSenha)
        val textoDeletarConta = findViewById<TextView>(R.id.textoDeletarConta)
        val setaDeletarConta = findViewById<ImageView>(R.id.imageView12)
        val textoSair = findViewById<TextView>(R.id.textoSair)
        val btnAlterarNome = findViewById<ImageView>(R.id.btnAlterarNome)
        val nomeUsuario = findViewById<TextView>(R.id.nomeUsuario)
        val emailUsuario = findViewById<TextView>(R.id.emailUsuario)

        //colocando informações do usuário no perfil
        val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
        var nome = sharedPreference.getString("nome","Null")
        var email = sharedPreference.getString("email","Null")

        nomeUsuario.text = "$nome"
        emailUsuario.text = "$email"
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

        val cancelarSair = view.findViewById<TextView>(R.id.cancelarSair)
        val confirmarSair = view.findViewById<TextView>(R.id.confirmarSair)
        val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
        cancelarSair.setOnClickListener { dialog.dismiss() }

        confirmarSair.setOnClickListener {
            sharedPreference.edit().clear().commit();
            val intent = Intent(applicationContext, bemvindo::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("EXIT", true)
            finish()
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
            var nome = campoAlterarNome.text.toString()
            if(nome.isNotEmpty()){

                //cria instancia
                var alteraNome = AlteraNome()
                alteraNome.execute(nome)

            }
            else{
                campoAlterarNome.error = if(nome.isEmpty()) "Insira seu nome" else null
            }

        }

        dialog = build.create()
        dialog.show()
    }
    inner class AlteraNome: AsyncTask<String?, Void, String?>(){
        override fun onPreExecute() {
            super.onPreExecute()
            builder.setCancelable(false)
            builder.show()
        }

        override fun doInBackground(vararg params:String?): String?{
            //pega os parametros email e senha enviado pela interface
            var nome = params[0] as String

            val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
            var email = sharedPreference.getString("email","Null") as String

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("novoNome", nome)
            jsonUsuario.put("email", email)

            //tentar estabelecer conexão com a internet
            try {
                val url = URL("http://192.168.1.11:8080/Studiest/alteraNome.php")
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

                    resultado = "$resultado,$nome"
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
            var ArrayResultado = resultado!!.split(",").toTypedArray()
            var resposta = ArrayResultado[0] as String
            var novoNome = ArrayResultado[1] as String

            val nomeUsuario = findViewById<TextView>(R.id.nomeUsuario)

            //verificando se o usuario retornado foi nao nulo
            if (resposta == "sim") {
                dialog.dismiss()
                nomeUsuario.text = novoNome
                val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putString("nome",novoNome)
                editor.commit()
            }
        }

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