package com.example.studiest

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.core.content.ContextCompat.getSystemService
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

class FragmentAtividades : Fragment() {

    private var atividadesDowload: AtividadesDownload? = null
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
        return inflater.inflate(R.layout.fragment_atividades, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        itemChecklistAdapter = ItemChecklistAdapter(requireContext())

        if(sortType == 1){
            AtividadeController.sortCriacao()
            itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())
        }else if(sortType == 2){
            AtividadeController.sortPrazo()
            itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())
        }

        val listViewAtividades = view.findViewById<ListView>(R.id.listview_atividades)
        listViewAtividades.adapter = itemChecklistAdapter

        val semAtividades = view.findViewById<ImageView>(R.id.semAtividades)


        val ha2 = Handler()
        ha2.postDelayed(object : Runnable {
            override fun run() {
                var estadoConexao = haveNetworkConnection()

                if(estadoConexao == true){
                    semAtividades.setImageResource(R.drawable.tudo_concluido)
                    listViewAtividades.setEmptyView(semAtividades)
                }else{
                    semAtividades.setImageResource(R.drawable.erro_conexao)
                    listViewAtividades.setEmptyView(semAtividades)
                }
                ha2.postDelayed(this, 100)
            }
        }, 100)


        listViewAtividades.setOnItemClickListener{parent, view, position, id ->
            var atividade: ItemChecklist = AtividadeController.getAtividade(position)
            val intentAlterar = Intent(activity, adicionar_item::class.java)
            intentAlterar.putExtra("p",position)
            intentAlterar.putExtra("selecionado",1)
            activity?.startActivity(intentAlterar)
        }

        listViewAtividades.setOnItemLongClickListener{parent, view, position, id ->
            var estadoConexao = haveNetworkConnection()

            if(estadoConexao == true){
                var atividade: ItemChecklist = AtividadeController.getAtividade(position)

                val concluido = view.findViewById<ImageView>(R.id.btnMarcarConcluido)

                concluido.getLayoutParams().height = 110; //can change the size according to you requirements
                concluido.getLayoutParams().width = 110; //--
                concluido.requestLayout()
                concluido.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                concluido.setImageResource(R.drawable.visto_concluido)

                Toast.makeText(context, "Atividade concluída com sucesso", Toast.LENGTH_SHORT).show()
                var deletaItem: DeletaItem? = DeletaItem()
                deletaItem?.execute(atividade)
                deletaItem = null

                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        AtividadeController.apaga(position)
                        itemChecklistAdapter.clear()
                        itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())
                    },
                    800 // value in milliseconds
                )
            }else{
                Toast.makeText(context, "Erro ao concluir. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show()
            }
            true
        }


        var atividadesDownload = AtividadesDownload()
        atividadesDownload?.execute()

        val refresh = view.findViewById<SwipeRefreshLayout>(R.id.refresh_atividades)
        refresh.setOnRefreshListener {
            var atividadesDownload = AtividadesDownload()
            atividadesDownload?.execute()

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
            itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())
        }else if(sortType == 2){
            itemChecklistAdapter.clear()
            AtividadeController.sortPrazo()
            itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())
        }
        var atividadesDownload = AtividadesDownload()
        atividadesDownload?.execute()

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


    inner class AtividadesDownload : AsyncTask<Void, Void, List<ItemChecklist>?>() {


        override fun doInBackground(vararg strings: Void): List<ItemChecklist>? {
            var atividades: List<ItemChecklist>? = null

            val sharedPreference = activity!!.getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
            var id = sharedPreference.getInt("id", 0) as Int

            //cria um JSON Object para ser enviado
            var jsonUsuario = JSONObject()
            jsonUsuario.put("id", id)

            try {
                val url = URL("http://studiestoficial.000webhostapp.com/app/carregaAtividades.php")
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
                    atividades = jsonToItens(json)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return atividades
        }

        override fun onPostExecute(atividades: List<ItemChecklist>?) {
            super.onPostExecute(atividades)

            val refresh = view?.findViewById<SwipeRefreshLayout>(R.id.refresh_atividades)

            refresh!!.setRefreshing(false)

            if (atividades != null) {
                AtividadeController.listaDeAtividades().clear()
                AtividadeController.listaDeAtividades().addAll(atividades)

                if(sortType == 1){
                    itemChecklistAdapter.clear()
                    AtividadeController.sortCriacao()
                    itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())
                }else if(sortType == 2){
                    itemChecklistAdapter.clear()
                    AtividadeController.sortPrazo()
                    itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())
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

        private fun jsonToItens(json: JSONObject): List<ItemChecklist> {
            val atividades = mutableListOf<ItemChecklist>()
            val jsonItens = json.getJSONArray("atividades")

            for (j in 0 until jsonItens.length()) {
                val jsonItens = jsonItens.getJSONObject(j)
                val atividade = ItemChecklist(
                    jsonItens.getInt("id"),
                    jsonItens.getString("titulo"),
                    jsonItens.getString("disciplina"),
                    jsonItens.getString("prazo"),
                    jsonItens.getString("descricao"),
                    1
                )
                atividades.add(atividade)
            }
            return atividades
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
            AtividadeController.sortPrazo()
            itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())

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
            AtividadeController.sortCriacao()
            itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())

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