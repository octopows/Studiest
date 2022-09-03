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

class cadastrar : AppCompatActivity() {

    private lateinit var builder: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastrar)

        builder = Dialog(this)
        var i: Int = 1
        var j: Int = 1
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnConfirmarCadastrar = findViewById<Button>(R.id.btnConfirmarCadastrar)
        val btnNaoPossuiConta = findViewById<TextView>(R.id.btnNaoPossuiConta)
        val campoNome = findViewById<EditText>(R.id.campoNome)
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

                var email = campoEmailCadastrar.text.toString()
                var senha = campoSenhaCadastrar.text.toString()
                var confirmarSenha = campoConfirmarSenha.text.toString()
                var nome = campoNome.text.toString()

                if(nome.isNotEmpty() && email.isNotEmpty() && senha.length>=6 && senha == confirmarSenha){
                    campoErro2.text = ""
                    campoNome.setBackgroundResource(R.drawable.custom_input)
                    campoEmailCadastrar.setBackgroundResource(R.drawable.custom_input)
                    campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input)
                    campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
                    //cria uma instancia da classe que fará a requisição
                    var cadastraUsuario = CadastraUsuario()
                    cadastraUsuario.execute(nome,email,senha)


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

    inner class CadastraUsuario: AsyncTask<String?, Void, String?>(){
        override fun onPreExecute() {
            super.onPreExecute()
            builder.setCancelable(false)
            builder.show()
        }

        override fun doInBackground(vararg params:String?): String?{
            //pega os parametros email e senha enviado pela interface
            var nome = params[0] as String
            var email = params[1] as String
            var senha = params[2] as String

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("nome", nome)
            jsonUsuario.put("email", email)
            jsonUsuario.put("senha",senha)

            //tentar estabelecer conexão com a internet
            try {
                val url = URL("http://192.168.1.11:8080/Studiest/cadastraUsuario.php")
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

            val campoNome = findViewById<EditText>(R.id.campoNome)
            val campoEmailCadastrar = findViewById<EditText>(R.id.campoEmailCadastrar)
            val campoSenhaCadastrar = findViewById<EditText>(R.id.campoSenhaCadastrar)
            val campoConfirmarSenha = findViewById<EditText>(R.id.campoConfirmarSenha)
            val campoErro2 = findViewById<TextView>(R.id.campoErro2)

            //verificando se o usuario retornado foi nao nulo
            if (resultado == "sim") {
                campoErro2.text = ""
                campoNome.setBackgroundResource(R.drawable.custom_input)
                campoEmailCadastrar.setBackgroundResource(R.drawable.custom_input)
                campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input)
                campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
                campoEmailCadastrar.setBackgroundResource(R.drawable.custom_input)
                Toast.makeText(applicationContext, "Cadastro Efetivado!", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, entrar::class.java)
                finish()
                startActivity(intent)
            }else{
                campoErro2.text = "Este email já cadastrado."
                campoEmailCadastrar.setBackgroundResource(R.drawable.custom_input_error)
                campoSenhaCadastrar.setBackgroundResource(R.drawable.custom_input)
                campoConfirmarSenha.setBackgroundResource(R.drawable.custom_input)
                campoEmailCadastrar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.email, 0, 0, 0);
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