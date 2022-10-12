package com.example.studiest

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset


class entrar : AppCompatActivity() {

    private lateinit var builder: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrar)

        var check: Int = 1
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnEsqueceuSenha = findViewById<TextView>(R.id.btnEsqueceuSenha)
        val btnNaoPossuiConta = findViewById<TextView>(R.id.btnNaoPossuiConta)
        val btnConfirmarEntrar = findViewById<Button>(R.id.btnConfirmarEntrar)
        val campoEmail = findViewById<EditText>(R.id.campoEmail)
        val campoSenha = findViewById<EditText>(R.id.campoSenha)
        val campoErro = findViewById<TextView>(R.id.campoErro)
        var btnMostrarSenha = findViewById<ImageView>(R.id.btnMostrarSenha2)

        builder = Dialog(this)

        //botao para apertar pra entrar
        btnConfirmarEntrar.setOnClickListener {
            var email = campoEmail.text.toString()
            var senha = campoSenha.text.toString()

            if (email.isNotEmpty() && senha.isNotEmpty()) {
                //cria uma instancia da classe que fará a requisição
                var validaUsuario = ValidaUsuario()
                validaUsuario.execute(email, senha)
            } else {
                campoErro.text = "Email ou senha inválidos"
                campoEmail.setBackgroundResource(R.drawable.custom_input_error)
                campoSenha.setBackgroundResource(R.drawable.custom_input_error)
                campoEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
                campoSenha.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.cadeado_senha,
                    0,
                    0,
                    0
                );
            }


        }

        //botão para voltar
        btnVoltar.setOnClickListener {
            finish()
        }

        //apertar na frase pra ir direto pro cadastro
        btnNaoPossuiConta.setOnClickListener {
            finish()
            val intent = Intent(this, cadastrar::class.java)
            startActivity(intent)
        }

        //apertar no Esqueceu sua senha?
        btnEsqueceuSenha.setOnClickListener {
            val intent = Intent(this, recuperar_senha::class.java)
            startActivity(intent)

        }

        campoEmail.setOnClickListener {
            campoEmail.setBackgroundResource(R.drawable.custom_input)
            campoSenha.setBackgroundResource(R.drawable.custom_input)
            campoEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_email, 0, 0, 0);
        }

        campoSenha.setOnClickListener {
            campoEmail.setBackgroundResource(R.drawable.custom_input)
            campoSenha.setBackgroundResource(R.drawable.custom_input)
            campoSenha.setCompoundDrawablesWithIntrinsicBounds(R.drawable.custom_cadeado, 0, 0, 0);
        }

        campoEmail.setOnFocusChangeListener { campoEmail, hasFocus ->
            if (hasFocus) {
                campoEmail.setBackgroundResource(R.drawable.custom_input)
                campoSenha.setBackgroundResource(R.drawable.custom_input)
            }
        }

        campoSenha.setOnFocusChangeListener { campoSenha, hasFocus ->
            if (hasFocus) {
                campoEmail.setBackgroundResource(R.drawable.custom_input)
                campoSenha.setBackgroundResource(R.drawable.custom_input)
            }
        }


        //mostrar senha
        btnMostrarSenha.setOnClickListener {
            if (check == 1) {
                btnMostrarSenha.setImageResource(R.drawable.senha_mostrada)
                check = 0
                campoSenha.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                btnMostrarSenha.setImageResource(R.drawable.senha_ocultada)
                check = 1
                campoSenha.transformationMethod = PasswordTransformationMethod.getInstance()
            }

        }
    }

    inner class ValidaUsuario : AsyncTask<String?, Void, Usuario?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            builder.setCancelable(false)
            builder.show()
        }

        override fun doInBackground(vararg params: String?): Usuario? {
            //pega os parametros email e senha enviado pela interface
            var email = params[0] as String
            var senha = params[1] as String

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("email", email)
            jsonUsuario.put("senha", senha)

            //tentar estabelecer conexão com a internet
            try {
                val url = URL("http://studiestoficial.000webhostapp.com/app/verificaUsuario.php")
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
                    //criar um objeto JSON a partir da entrada
                    val json = JSONObject(resultado)
                    //pega o id do objeto json
                    var id = json.getInt("id")
                    var nome = json.getString("nome")
                    //constroi um usuário a partir do id retornado e o nome
                    return Usuario(id, nome, email)
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(usuario: Usuario?) {
            super.onPostExecute(usuario)
            builder.dismiss()

            val campoEmail = findViewById<EditText>(R.id.campoEmail)
            val campoSenha = findViewById<EditText>(R.id.campoSenha)
            val campoErro = findViewById<TextView>(R.id.campoErro)

            //verificando se o usuario retornado foi nao nulo
            if (usuario != null) {
                campoErro.text = ""
                campoEmail.setBackgroundResource(R.drawable.custom_input)
                campoSenha.setBackgroundResource(R.drawable.custom_input)


                val sharedPreference = getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.putString("nome", usuario.nome)
                editor.putString("email", usuario.email)
                editor.putInt("id", usuario.id)
                editor.putInt("notStatus",1)
                editor.commit()

                AvaliacaoController.listaDeAvaliacoes().clear()
                AtividadeController.listaDeAtividades().clear()
                LembreteController.listaDeLembretes().clear()
                ResumoController.listaDeResumos().clear()


                //cria uma intent para chamar a proxima tela
                val intent = Intent(applicationContext, tela_principal::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                intent.putExtra("nome", usuario.nome)
                finish()
                startActivity(intent)

            } else {
                campoErro.text = "Email ou senha inválidos"
                campoEmail.setBackgroundResource(R.drawable.custom_input_error)
                campoSenha.setBackgroundResource(R.drawable.custom_input_error)
                campoEmail.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
                campoSenha.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.cadeado_senha,
                    0,
                    0,
                    0
                );
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
