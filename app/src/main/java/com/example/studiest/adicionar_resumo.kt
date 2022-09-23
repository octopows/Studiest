package com.example.studiest

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class adicionar_resumo : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    private lateinit var builder: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_resumo)

        builder = Dialog(this)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnDeletarResumo = findViewById<ImageView>(R.id.btnDeletarResumo)
        val btnSalvarResumo = findViewById<Button>(R.id.btnSalvarResumo)
        val campoTituloResumo = findViewById<EditText>(R.id.campoTituloResumo)
        val campoDisciplinaResumo = findViewById<EditText>(R.id.campoDisciplinaResumo)
        val campoConteudoResumo = findViewById<EditText>(R.id.campoConteudoResumo)

        val it : Intent = intent
        var p = it.getIntExtra("p",-1)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //dialog box de deletar resumo
        btnDeletarResumo.setOnClickListener{
            showDialogRemoverResumo()
        }

        btnSalvarResumo.setOnClickListener {
            var id = -1
            val titulo = campoTituloResumo.text.toString()
            val disciplina = campoDisciplinaResumo.text.toString()
            val conteudo = campoConteudoResumo.text.toString()

            if(titulo.isNotEmpty() && disciplina.isNotEmpty() && conteudo.isNotEmpty()){
                val resumo = Resumo(id,titulo,disciplina,conteudo)
                campoTituloResumo.text.clear()
                campoTituloResumo.requestFocus()
                campoDisciplinaResumo.text.clear()
                campoDisciplinaResumo.text.clear()
                campoConteudoResumo.text.clear()
                campoConteudoResumo.text.clear()

                if (p == -1){
                    ResumoController.cadastra(resumo)
                    var cadastraResumo: CadastraResumo? = CadastraResumo()
                    cadastraResumo?.execute(resumo)
                    cadastraResumo = null

                }
                else{
                    val resumoEdit = ResumoController.getResumo(p)
                    val resumo = Resumo(resumoEdit.id,titulo,disciplina,conteudo)
                    ResumoController.atualiza(p,resumo)
                    var editaResumo: EditaResumo? = EditaResumo()
                    editaResumo?.execute(resumo)
                    editaResumo = null

                }
            }else{
                campoTituloResumo.error = if(titulo.isEmpty()) "Preencha um título" else null
                campoDisciplinaResumo.error = if(disciplina.isEmpty()) "Preencha uma disciplina" else null
                campoConteudoResumo.error = if(conteudo.isEmpty()) "Insira o conteúdo do resumo" else null
            }
            finish()
        }
        if (p!=-1) {
            val resumo: Resumo = ResumoController.getResumo(p)
            campoTituloResumo.setText(resumo.titulo)
            campoDisciplinaResumo.setText(resumo.disciplina)
            campoConteudoResumo.setText(resumo.conteudo)
        }

    }
    //função para chamar dialog sair
    private fun showDialogRemoverResumo(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_remover_resumo, null)
        build.setView(view)

        val cancelarRemoverResumo = view.findViewById<TextView>(R.id.cancelarRemoverResumo)
        val confirmarRemoverResumo = view.findViewById<TextView>(R.id.confirmarRemoverResumo)

        var resumoAdapter = ResumoAdapter(this)

        val it : Intent = intent
        var p = it.getIntExtra("p",-1)

        cancelarRemoverResumo.setOnClickListener { dialog.dismiss() }

        confirmarRemoverResumo.setOnClickListener {
            if(p!=-1) {
                val resumoEdit = ResumoController.getResumo(p)
                val resumo = Resumo(resumoEdit.id,resumoEdit.titulo,resumoEdit.disciplina,resumoEdit.conteudo)
                var deletaResumo: DeletaResumo? = DeletaResumo()
                deletaResumo?.execute(resumo)
                deletaResumo = null
                ResumoController.apaga(p)
                resumoAdapter.clear()
                resumoAdapter.addAll(ResumoController.listaDeResumos())
            }
            finish()
        }
        dialog = build.create()
        dialog.show()
    }

    inner class CadastraResumo : AsyncTask<Resumo?, Void, Boolean?>(){

        override fun doInBackground(vararg params: Resumo?): Boolean? {
            val resumo : Resumo = params[0] as Resumo

            try {
                val url = URL("http://192.168.1.11:8080/Studiest/cadastraResumo.php")
                val conexao = (url.openConnection() as HttpURLConnection)

                conexao.readTimeout = 15000
                conexao.connectTimeout = 15000
                conexao.requestMethod = "POST"
                conexao.doInput = true
                conexao.doOutput = true
                conexao.setRequestProperty("Content-Type","application/json")
                conexao.connect()

                var outputStream: OutputStream = conexao.outputStream
                outputStream.write(ResumoToJsonBytes(resumo))
                outputStream.flush()
                outputStream.close()

                val responseCode = conexao.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = conexao.inputStream
                    var resultado = streamToString(inputStream)
                    val json = JSONObject(resultado)
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
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

        private fun ResumoToJsonBytes(resumo: Resumo): ByteArray?{
            try{
                var jsonResumo = JSONObject()
                val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
                var id = sharedPreference.getInt("id",0)

                jsonResumo.put("user_id",id)
                jsonResumo.put("titulo", resumo.titulo)
                jsonResumo.put("disciplina", resumo.disciplina)
                jsonResumo.put("conteudo", resumo.conteudo)
                var byteArray = jsonResumo.toString().encodeToByteArray()
                return byteArray
            }catch (e: Exception){
                e.printStackTrace()
            }
            return null
        }
    }

    inner class EditaResumo : AsyncTask<Resumo?, Void, Boolean?>(){

        override fun doInBackground(vararg params: Resumo?): Boolean? {
            val resumo : Resumo = params[0] as Resumo

            try {
                val url = URL("http://192.168.1.11:8080/Studiest/editaResumo.php")
                val conexao = (url.openConnection() as HttpURLConnection)

                conexao.readTimeout = 15000
                conexao.connectTimeout = 15000
                conexao.requestMethod = "POST"
                conexao.doInput = true
                conexao.doOutput = true
                conexao.setRequestProperty("Content-Type","application/json")
                conexao.connect()

                var outputStream: OutputStream = conexao.outputStream
                outputStream.write(ResumoToJsonBytes(resumo))
                outputStream.flush()
                outputStream.close()

                val responseCode = conexao.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = conexao.inputStream
                    var resultado = streamToString(inputStream)
                    val json = JSONObject(resultado)
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
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

        private fun ResumoToJsonBytes(resumo: Resumo): ByteArray?{
            try{
                var jsonResumo = JSONObject()
                val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
                var user_id = sharedPreference.getInt("id",0)

                jsonResumo.put("id",resumo.id)
                jsonResumo.put("user_id",user_id)
                jsonResumo.put("titulo", resumo.titulo)
                jsonResumo.put("disciplina", resumo.disciplina)
                jsonResumo.put("conteudo", resumo.conteudo)
                var byteArray = jsonResumo.toString().encodeToByteArray()
                return byteArray

            }catch (e: Exception){
                e.printStackTrace()
            }
            return null
        }
    }

    inner class DeletaResumo : AsyncTask<Resumo?, Void, Boolean?>(){
        override fun onPreExecute() {
            super.onPreExecute()
            builder.setCancelable(false)
            builder.show()
        }

        override fun doInBackground(vararg params: Resumo?): Boolean? {
            val resumo : Resumo = params[0] as Resumo

            try {
                val url = URL("http://192.168.1.11:8080/Studiest/deletaResumo.php")
                val conexao = (url.openConnection() as HttpURLConnection)

                conexao.readTimeout = 15000
                conexao.connectTimeout = 15000
                conexao.requestMethod = "POST"
                conexao.doInput = true
                conexao.doOutput = true
                conexao.setRequestProperty("Content-Type","application/json")
                conexao.connect()

                var outputStream: OutputStream = conexao.outputStream
                outputStream.write(ResumoToJsonBytes(resumo))
                outputStream.flush()
                outputStream.close()

                val responseCode = conexao.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = conexao.inputStream
                    var resultado = streamToString(inputStream)
                    val json = JSONObject(resultado)
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
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

        private fun ResumoToJsonBytes(resumo: Resumo): ByteArray?{
            try{
                var jsonResumo = JSONObject()

                jsonResumo.put("id",resumo.id)
                var byteArray = jsonResumo.toString().encodeToByteArray()
                return byteArray

            }catch (e: Exception){
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(resposta: Boolean?) {
            super.onPostExecute(resposta)
            builder.dismiss()
        }
    }
}