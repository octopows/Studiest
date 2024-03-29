package com.example.studiest

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

class FragmentAvaliacoes : Fragment() {

    private var avaliacoesDowload: AvaliacoesDownload? = null
    lateinit var itemChecklistAdapter: ItemChecklistAdapter
    private lateinit var dialog: AlertDialog
    var sortType: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_avaliacoes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemChecklistAdapter = ItemChecklistAdapter(requireContext())

        if(sortType == 1){
            AvaliacaoController.sortCriacao()
            itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
        }else if(sortType == 2){
            AvaliacaoController.sortPrazo()
            itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
        }

        val listViewAvaliacoes = view.findViewById<ListView>(R.id.listview_avaliacoes)
        listViewAvaliacoes.adapter = itemChecklistAdapter

        val semAvaliacoes = view.findViewById<ImageView>(R.id.semAvaliacoes)

        val ha2 = Handler()
        ha2.postDelayed(object : Runnable {
            override fun run() {
                var estadoConexao = haveNetworkConnection()

                if(estadoConexao == true){
                    semAvaliacoes.setImageResource(R.drawable.tudo_concluido)
                    listViewAvaliacoes.setEmptyView(semAvaliacoes)
                }else{
                    semAvaliacoes.setImageResource(R.drawable.erro_conexao)
                    listViewAvaliacoes.setEmptyView(semAvaliacoes)
                }
                ha2.postDelayed(this, 500)
            }
        }, 500)

        listViewAvaliacoes.setOnItemClickListener{parent, view, position, id ->
            var avaliacao: ItemChecklist = AvaliacaoController.getAvaliacao(position)
            val intentAlterar = Intent(activity, adicionar_item::class.java)
            intentAlterar.putExtra("p",position)
            intentAlterar.putExtra("selecionado",0)
            activity?.startActivity(intentAlterar)
        }

        listViewAvaliacoes.setOnItemLongClickListener{parent, view, position, id ->
            var estadoConexao = haveNetworkConnection()

            if(estadoConexao == true){
                var avaliacao: ItemChecklist = AvaliacaoController.getAvaliacao(position)

                val concluido = view.findViewById<ImageView>(R.id.btnMarcarConcluido)

                concluido.getLayoutParams().height = 110; //can change the size according to you requirements
                concluido.getLayoutParams().width = 110; //--
                concluido.requestLayout()
                concluido.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                concluido.setImageResource(R.drawable.visto_concluido)

                Toast.makeText(context, "Avaliação concluída com sucesso", Toast.LENGTH_SHORT).show()
                var deletaItem: DeletaItem? = DeletaItem()
                deletaItem?.execute(avaliacao)
                deletaItem = null

                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        AvaliacaoController.apaga(position)
                        itemChecklistAdapter.clear()
                        itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
                    },
                    800 // value in milliseconds
                )
            }else{
                Toast.makeText(context, "Erro ao concluir. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show()
            }
            true
        }

        var avaliacoesDownload = AvaliacoesDownload()
        avaliacoesDownload?.execute()

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refresh_avaliacoes)
        refresh.setOnRefreshListener {
            var avaliacoesDownload = AvaliacoesDownload()
            avaliacoesDownload?.execute()
        }

        val btnOrdenar = view.findViewById<TextView>(R.id.btnOrdenar)

        //abrir dialog ordenar
        btnOrdenar.setOnClickListener {
            showDialogOrdenar()
        }
    }

    override fun onResume() {
        super.onResume()

        if(sortType == 1){
            itemChecklistAdapter.clear()
            itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
        }else if(sortType == 2){
            itemChecklistAdapter.clear()
            AvaliacaoController.sortPrazo()
            itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
        }
        var avaliacoesDownload = AvaliacoesDownload()
        avaliacoesDownload?.execute()
    }

    private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = requireActivity().getSystemService(AppCompatActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals(
                    "WIFI",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }

    inner class AvaliacoesDownload : AsyncTask<Void, Void, List<ItemChecklist>?>() {


        override fun doInBackground(vararg strings: Void): List<ItemChecklist>? {
            var avaliacoes: List<ItemChecklist>? = null

            val sharedPreference = activity!!.getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
            var id = sharedPreference.getInt("id", 0) as Int

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("id", id)

            try {
                val url = URL("http://studiestoficial.000webhostapp.com/app/carregaAvaliacoes.php")
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
                    avaliacoes = jsonToAvaliacoes(json)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return avaliacoes
        }

        override fun onPostExecute(avaliacoes: List<ItemChecklist>?) {
            super.onPostExecute(avaliacoes)

           val refresh = view?.findViewById<SwipeRefreshLayout>(R.id.refresh_avaliacoes)

            refresh!!.setRefreshing(false)

            if (avaliacoes != null) {
                AvaliacaoController.listaDeAvaliacoes().clear()
                AvaliacaoController.listaDeAvaliacoes().addAll(avaliacoes)

                if(sortType == 1){
                    itemChecklistAdapter.clear()
                    AvaliacaoController.sortCriacao()
                    itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
                }else if(sortType == 2){
                    itemChecklistAdapter.clear()
                    AvaliacaoController.sortPrazo()
                    itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
                }
                itemChecklistAdapter.notifyDataSetChanged()
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

        private fun jsonToAvaliacoes(json: JSONObject): List<ItemChecklist> {
            val avaliacoes = mutableListOf<ItemChecklist>()
            val jsonAvaliacoes = json.getJSONArray("avaliacoes")

            for (j in 0 until jsonAvaliacoes.length()) {
                val jsonAvaliacoes = jsonAvaliacoes.getJSONObject(j)
                val avaliacao = ItemChecklist(
                    jsonAvaliacoes.getInt("id"),
                    jsonAvaliacoes.getString("titulo"),
                    jsonAvaliacoes.getString("disciplina"),
                    jsonAvaliacoes.getString("prazo"),
                    jsonAvaliacoes.getString("descricao"),
                    0
                )
                avaliacoes.add(avaliacao)
            }
            return avaliacoes
        }
    }

    inner class DeletaItem : AsyncTask<ItemChecklist?, Void, Boolean?>(){

        override fun doInBackground(vararg params: ItemChecklist?): Boolean? {
            val item : ItemChecklist = params[0] as ItemChecklist

            try {
                var url: URL? = null

                if(item.tipo == 0){
                    url = URL("http://studiestoficial.000webhostapp.com/app/deletaAvaliacao.php")
                } else if(item.tipo == 1){
                    url = URL("http://studiestoficial.000webhostapp.com/app/deletaAtividade.php")
                } else if(item.tipo == 2){
                    url = URL("http://studiestoficial.000webhostapp.com/app/deletaLembrete.php")
                }

                val conexao = (url!!.openConnection() as HttpURLConnection)

                conexao.readTimeout = 15000
                conexao.connectTimeout = 15000
                conexao.requestMethod = "POST"
                conexao.doInput = true
                conexao.doOutput = true
                conexao.setRequestProperty("Content-Type","application/json")
                conexao.connect()

                var outputStream: OutputStream = conexao.outputStream
                outputStream.write(ItemToJsonBytes(item))
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

        private fun ItemToJsonBytes(item: ItemChecklist): ByteArray?{
            try{
                var jsonItem = JSONObject()

                jsonItem.put("id",item.id)

                var byteArray = jsonItem.toString().encodeToByteArray()
                return byteArray
            }catch (e: Exception){
                e.printStackTrace()
            }
            return null
        }
    }
    //função para chamar dialog ordenar
    private fun showDialogOrdenar(){
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_ordenar, null)
        build.setView(view)

        val ordenarPrazo = view.findViewById<TextView>(R.id.ordenarPrazo)
        val ordenarCriacao = view.findViewById<TextView>(R.id.ordenarCriacao)
        val vistoPrazo = view.findViewById<ImageView>(R.id.vistoPrazo)
        val vistoCriacao = view.findViewById<ImageView>(R.id.vistoCriacao)

        if(sortType == 1){
            ordenarPrazo.setTextColor(ContextCompat.getColor(requireActivity(),R.color.cinza_hint))
            vistoPrazo.setImageResource(0)
            ordenarCriacao.setTextColor(ContextCompat.getColor(requireActivity(),R.color.azul))
            vistoCriacao.setImageResource(R.drawable.visto)
        }else if(sortType == 2){
            ordenarPrazo.setTextColor(ContextCompat.getColor(requireActivity(),R.color.azul))
            vistoPrazo.setImageResource(R.drawable.visto)
            ordenarCriacao.setTextColor(ContextCompat.getColor(requireActivity(),R.color.cinza_hint))
            vistoCriacao.setImageResource(0)
        }

        ordenarPrazo.setOnClickListener {
            sortType = 2
            itemChecklistAdapter.clear()
            AvaliacaoController.sortPrazo()
            itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())

            ordenarPrazo.setTextColor(ContextCompat.getColor(requireActivity(),R.color.azul))
            vistoPrazo.setImageResource(R.drawable.visto)
            ordenarCriacao.setTextColor(ContextCompat.getColor(requireActivity(),R.color.cinza_hint))
            vistoCriacao.setImageResource(0)
            Timer().schedule(200){
                dialog.dismiss()
            }
        }

        ordenarCriacao.setOnClickListener {
            sortType = 1

            itemChecklistAdapter.clear()
            AvaliacaoController.sortCriacao()
            itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
            ordenarPrazo.setTextColor(ContextCompat.getColor(requireActivity(),R.color.cinza_hint))
            vistoPrazo.setImageResource(0)
            ordenarCriacao.setTextColor(ContextCompat.getColor(requireActivity(),R.color.azul))
            vistoCriacao.setImageResource(R.drawable.visto)
            Timer().schedule(200){
                dialog.dismiss()
            }
        }

        dialog = build.create()
        dialog.show()
    }

}