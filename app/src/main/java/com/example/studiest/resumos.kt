package com.example.studiest

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class resumos : AppCompatActivity() {
    private var resumosDowload: ResumosDownload? = null
    lateinit var resumoAdapter: ResumoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resumos)

        resumoAdapter = ResumoAdapter(this)
        resumoAdapter.addAll(ResumoController.listaDeResumos())
        val semResumos = findViewById<ImageView>(R.id.semResumos)
        val listViewResumo = findViewById<ListView>(R.id.listview_resumos)
        listViewResumo.adapter = resumoAdapter

        val refresh = findViewById<SwipeRefreshLayout>(R.id.refresh)
        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnAddResumo = findViewById<ImageView>(R.id.btnAddResumo)

        listViewResumo.setEmptyView(semResumos)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //btn Adicionar Resumo
        btnAddResumo.setOnClickListener{
            val intentCadastrar= Intent(this, adicionar_resumo::class.java)
            startActivity(intentCadastrar)
        }

        listViewResumo.setOnItemClickListener{parent, view, position, id ->
            var resumo: Resumo = ResumoController.getResumo(position)
            val intentAlterar = Intent(this, adicionar_resumo::class.java)
            intentAlterar.putExtra("p",position)
            startActivity(intentAlterar)
        }


        var resumosDownload = ResumosDownload()
        resumosDownload?.execute()

        refresh.setOnRefreshListener {
            var resumosDownload = ResumosDownload()
            resumosDownload?.execute()
        }

    }
    override fun onResume() {
        super.onResume()

        resumoAdapter.clear()
        resumoAdapter.addAll(ResumoController.listaDeResumos())

    }

    inner class ResumosDownload : AsyncTask<Void, Void, List<Resumo>?>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg strings: Void): List<Resumo>? {
            var resumos: List<Resumo>? = null

            val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
            var id = sharedPreference.getInt("id",0) as Int

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("id", id)

            try {
                val url = URL("http://studiestoficial.000webhostapp.com/app/carregaResumos.php")
                val conexao = (url.openConnection() as HttpURLConnection)
                conexao.readTimeout = 10000
                conexao.connectTimeout = 15000
                conexao.requestMethod = "POST"
                conexao.doInput = true
                conexao.doOutput = true
                conexao.connect()

                //define uma variável para utilizar o fluxo de saída da conexão
                var outputStream: OutputStream = conexao.outputStream
                //escreve no fluxo de saí da os bytes que compoem o json a ser enviado
                outputStream.write(jsonUsuario.toString().encodeToByteArray())
                //finaliza a conexão
                outputStream.flush()
                outputStream.close()

                val responseCode = conexao.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = conexao.inputStream
                    val json = JSONObject(streamToString(inputStream))
                    resumos = jsonToResumos(json)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return resumos
        }

        override fun onPostExecute(resumos: List<Resumo>?) {
            super.onPostExecute(resumos)

            val refresh = findViewById<SwipeRefreshLayout>(R.id.refresh)

            refresh.setRefreshing(false)

            if (resumos != null) {
                ResumoController.listaDeResumos().clear()
                ResumoController.listaDeResumos().addAll(resumos)

                resumoAdapter.clear()
                resumoAdapter.addAll(ResumoController.listaDeResumos())
                resumoAdapter.notifyDataSetChanged()
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

        private fun jsonToResumos(json: JSONObject): List<Resumo> {
            val resumos = mutableListOf<Resumo>()
            val jsonResumos = json.getJSONArray("resumos")

            for (j in 0 until jsonResumos.length()) {
                val jsonResumos = jsonResumos.getJSONObject(j)
                val resumo = Resumo(
                    jsonResumos.getInt("id"),
                    jsonResumos.getString("titulo"),
                    jsonResumos.getString("disciplina"),
                    jsonResumos.getString("conteudo"),
                )
                resumos.add(resumo)
            }
            return resumos
        }

    }
}