package com.example.studiest

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*


class adicionar_item : AppCompatActivity() {

    private lateinit var dialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_item)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val campoDataEntrega = findViewById<TextView>(R.id.campoPrazo)
        val btnDeletarItem = findViewById<ImageView>(R.id.btnDeletarItem)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }


        //dialog box de deletar item
        btnDeletarItem.setOnClickListener{
            showDialogRemoverItem()
        }

        //DATE PICKER
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var prazoLimpo: String? = null
        campoDataEntrega.setOnClickListener {
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view,mYear,mMonth,mDay ->
                if((mMonth+1) <10 && mDay <10){
                    prazoLimpo = ""+0+mDay+"/"+0+(mMonth+1)+"/"+mYear
                    campoDataEntrega.setText(prazoLimpo+" ▼")

                } else if((mMonth+1) <10 && mDay >=10){
                    prazoLimpo = ""+mDay+"/"+0+(mMonth+1)+"/"+mYear
                    campoDataEntrega.setText(prazoLimpo+" ▼")
                } else if((mMonth+1) >=10 && mDay <10){
                    prazoLimpo = ""+0+mDay+"/"+(mMonth+1)+"/"+mYear
                    campoDataEntrega.setText(prazoLimpo+" ▼")
                } else{
                    prazoLimpo = ""+mDay+"/"+(mMonth+1)+"/"+mYear
                    campoDataEntrega.setText(prazoLimpo+" ▼")
                }

            },year,month,day)
            dpd.show()
        }


        //salvar item
        val btnSalvarItem = findViewById<Button>(R.id.btnSalvarItem)
        val campoTituloItem = findViewById<EditText>(R.id.campoTituloItem)
        val campoDisciplinaItem = findViewById<EditText>(R.id.campoDisciplinaItem)
        val campoDescricaoItem = findViewById<EditText>(R.id.campoDescricaoItem)
        val campoPrazo = findViewById<TextView>(R.id.campoPrazo)


        var it : Intent = intent
        var p = it.getIntExtra("p",-1)

        var it2 : Intent = intent
        var tipo = it2.getIntExtra("selecionado",-1)


       //var  tipo = 0

        btnSalvarItem.setOnClickListener {
            var estadoConexao = haveNetworkConnection()

            if(estadoConexao == true){
                var id = -1
                val titulo = campoTituloItem.text.toString()
                val disciplina = campoDisciplinaItem.text.toString()
                val descricao = campoDescricaoItem.text.toString()
                var prazo: String? = null

                if(titulo.isNotEmpty() && disciplina.isNotEmpty() && campoPrazo.text != "Selecionar ▼" ){
                    val item = ItemChecklist(id,titulo, disciplina, prazoLimpo, descricao,tipo)
                    campoTituloItem.text.clear()
                    campoDisciplinaItem.text.clear()
                    campoDescricaoItem.text.clear()
                    campoPrazo.text = "Selecionar ▼"
                    if (p == -1){
                        if(tipo == 0){
                            AvaliacaoController.cadastra(item)
                        } else if(tipo == 1){
                            AtividadeController.cadastra(item)
                        } else if(tipo == 2){
                            LembreteController.cadastra(item)
                        }
                        var cadastraItem: CadastraItem? = CadastraItem()
                        cadastraItem?.execute(item)
                        cadastraItem = null
                        finish()
                    }
                    else{
                        if(tipo == 0){
                            val itemEdit =AvaliacaoController.getAvaliacao(p)
                            if(prazoLimpo != null){
                                prazo = prazoLimpo
                            }else
                                prazo = itemEdit.prazo
                            var item = ItemChecklist(itemEdit.id,titulo,disciplina,prazo,descricao,0)
                            AvaliacaoController.atualiza(p,item)
                            var editaItem: EditaItem? = EditaItem()
                            editaItem?.execute(item)
                            editaItem = null

                        } else if(tipo == 1){
                            val itemEdit =AtividadeController.getAtividade(p)
                            if(prazoLimpo != null){
                                prazo = prazoLimpo
                            }else
                                prazo = itemEdit.prazo
                            var item = ItemChecklist(itemEdit.id,titulo,disciplina,prazo,descricao,1)
                            AtividadeController.atualiza(p,item)
                            var editaItem: EditaItem? = EditaItem()
                            editaItem?.execute(item)
                            editaItem = null
                        } else if(tipo == 2){
                            val itemEdit =LembreteController.getLembrete(p)
                            if(prazoLimpo != null){
                                prazo = prazoLimpo
                            }else
                                prazo = itemEdit.prazo
                            val item = ItemChecklist(itemEdit.id,titulo,disciplina,prazo,descricao,2)
                            LembreteController.atualiza(p,item)
                            var editaItem: EditaItem? = EditaItem()
                            editaItem?.execute(item)
                            editaItem = null
                        }
                    }
                    finish()
                }else{
                    if(campoPrazo.text == "Selecionar ▼"){
                        Toast.makeText(this, "Selecione uma data", Toast.LENGTH_SHORT).show()
                    }
                    campoTituloItem.error = if(titulo.isEmpty()) "Insira um título" else null
                    campoDisciplinaItem.error = if(disciplina.isEmpty()) "Insira uma disciplina" else null
                }

            }else{
                Toast.makeText(this, "Erro ao salvar. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show()
            }

        }
        if (p!=-1) {
            var itemChecklist: ItemChecklist? = null

            if(tipo == 0){
                itemChecklist = AvaliacaoController.getAvaliacao(p)
            } else if(tipo == 1){
                itemChecklist = AtividadeController.getAtividade(p)
            } else if(tipo == 2){
                itemChecklist = LembreteController.getLembrete(p)
            }

            campoTituloItem.setText(itemChecklist!!.titulo)
            campoDisciplinaItem.setText(itemChecklist!!.disciplina)
            campoDescricaoItem.setText(itemChecklist.descricao)
            campoPrazo.setText(itemChecklist!!.prazo+" ▼")
        }
    }

    private fun haveNetworkConnection(): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
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

    //função para chamar dialog sair
    private fun showDialogRemoverItem(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_remover_item, null)
        build.setView(view)

        val cancelarRemoverItem = view.findViewById<TextView>(R.id.cancelarRemoverResumo)
        val confirmarRemoverItem = view.findViewById<TextView>(R.id.confirmarRemoverResumo)

        var itemChecklistAdapter = ItemChecklistAdapter(this)

        val it : Intent = intent
        var p = it.getIntExtra("p",-1)

        val it2 : Intent = intent
        var tipo = it2.getIntExtra("selecionado",-1)

        cancelarRemoverItem.setOnClickListener { dialog.dismiss() }

        confirmarRemoverItem.setOnClickListener {
            var estadoConexao = haveNetworkConnection()

            if(estadoConexao == true){
                if(p!=-1) {
                    if(tipo == 0 ){
                        val itemEdit =AvaliacaoController.getAvaliacao(p)
                        var item = ItemChecklist(itemEdit.id,itemEdit.titulo,itemEdit.disciplina,itemEdit.prazo,itemEdit.descricao,itemEdit.tipo)

                        var deletaItem: DeletaItem? = DeletaItem()
                        deletaItem?.execute(item)
                        deletaItem = null

                        AvaliacaoController.apaga(p)
                        itemChecklistAdapter.clear()
                        itemChecklistAdapter.addAll(AvaliacaoController.listaDeAvaliacoes())
                    } else if(tipo ==1){
                        val itemEdit =AtividadeController.getAtividade(p)
                        var item = ItemChecklist(itemEdit.id,itemEdit.titulo,itemEdit.disciplina,itemEdit.prazo,itemEdit.descricao,itemEdit.tipo)

                        var deletaItem: DeletaItem? = DeletaItem()
                        deletaItem?.execute(item)
                        deletaItem = null

                        AtividadeController.apaga(p)
                        itemChecklistAdapter.clear()
                        itemChecklistAdapter.addAll(AtividadeController.listaDeAtividades())
                    } else if(tipo ==2){
                        val itemEdit =LembreteController.getLembrete(p)
                        var item = ItemChecklist(itemEdit.id,itemEdit.titulo,itemEdit.disciplina,itemEdit.prazo,itemEdit.descricao,itemEdit.tipo)

                        var deletaItem: DeletaItem? = DeletaItem()
                        deletaItem?.execute(item)
                        deletaItem = null

                        LembreteController.apaga(p)
                        itemChecklistAdapter.clear()
                        itemChecklistAdapter.addAll(LembreteController.listaDeLembretes())
                    }
                }
                finish()

            }else{
                Toast.makeText(this, "Erro ao deletar. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show()
            }
        }
        dialog = build.create()
        dialog.show()
    }

    inner class CadastraItem : AsyncTask<ItemChecklist?, Void, Boolean?>(){

        override fun doInBackground(vararg params: ItemChecklist?): Boolean? {
            val item : ItemChecklist = params[0] as ItemChecklist

            try {
                var url: URL? = null

                if(item.tipo == 0){
                    url = URL("http://studiestoficial.000webhostapp.com/app/cadastraAvaliacao.php")
                } else if(item.tipo == 1){
                    url = URL("http://studiestoficial.000webhostapp.com/app/cadastraAtividade.php")
                } else if(item.tipo == 2){
                    url = URL("http://studiestoficial.000webhostapp.com/app/cadastraLembrete.php")
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
                val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
                var id = sharedPreference.getInt("id",0)

                jsonItem.put("user_id",id)
                jsonItem.put("titulo", item.titulo)
                jsonItem.put("disciplina", item.disciplina)
                jsonItem.put("descricao", item.descricao)
                jsonItem.put("prazo", item.prazo)

                var byteArray = jsonItem.toString().encodeToByteArray()
                return byteArray
            }catch (e: Exception){
                e.printStackTrace()
            }
            return null
        }
    }

    inner class EditaItem : AsyncTask<ItemChecklist?, Void, Boolean?>(){

        override fun doInBackground(vararg params: ItemChecklist?): Boolean? {
            val item : ItemChecklist = params[0] as ItemChecklist

            try {
                var url: URL? = null

                if(item.tipo == 0){
                    url = URL("http://studiestoficial.000webhostapp.com/app/editaAvaliacao.php")
                } else if(item.tipo == 1){
                    url = URL("http://studiestoficial.000webhostapp.com/app/editaAtividade.php")
                } else if(item.tipo == 2){
                    url = URL("http://studiestoficial.000webhostapp.com/app/editaLembrete.php")
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
                val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
                var id = sharedPreference.getInt("id",0)

                jsonItem.put("id",item.id)
                jsonItem.put("user_id",id)
                jsonItem.put("titulo", item.titulo)
                jsonItem.put("disciplina", item.disciplina)
                jsonItem.put("descricao", item.descricao)
                jsonItem.put("prazo", item.prazo)

                var byteArray = jsonItem.toString().encodeToByteArray()
                return byteArray
            }catch (e: Exception){
                e.printStackTrace()
            }
            return null
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

}