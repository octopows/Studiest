package com.example.studiest

import android.content.Context
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

class ItemChecklistAdapter(contexto: Context) : ArrayAdapter<ItemChecklist>(contexto, 0){

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View
        if(convertView != null) {
            v = convertView
        }else{
            v = LayoutInflater.from(context).inflate(R.layout.checklist_item, parent, false)
        }


        val item = getItem(position)
        val tituloItem = v.findViewById<TextView>(R.id.tituloItem)
        val disciplinaItem = v.findViewById<TextView>(R.id.disciplinaItem)
        val prazoitem = v.findViewById<TextView>(R.id.prazoItem)
        val fundoItem = v.findViewById<View>(R.id.fundoItem)
        val btnMarcarConcluido = v.findViewById<ImageView>(R.id.btnMarcarConcluido)

        val sharedPreference = context.getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putInt("deletarItem", 0)
        editor.commit()


        btnMarcarConcluido.setOnClickListener{
            it.tag = item?.id
            btnMarcarConcluido.getLayoutParams().height = 90; //can change the size according to you requirements
            btnMarcarConcluido.getLayoutParams().width = 90; //--
            btnMarcarConcluido.requestLayout()
            btnMarcarConcluido.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            btnMarcarConcluido.setImageResource(R.drawable.visto)

            if(item?.tipo == 0){
                var ITEM = ItemChecklist(item?.id,item?.titulo,item?.disciplina,item?.prazo,item?.descricao,item?.tipo)
                var deletaItem: DeletaItem? = DeletaItem()
                AvaliacaoController.apaga(position)
                deletaItem?.execute(ITEM)
                deletaItem = null
                btnMarcarConcluido.setImageResource(R.drawable.visto)
                Toast.makeText(context, "Avaliação concluída com sucesso!", Toast.LENGTH_SHORT).show()

            } else  if(item?.tipo == 1){
                var ITEM = ItemChecklist(item?.id,item?.titulo,item?.disciplina,item?.prazo,item?.descricao,item?.tipo)
                var deletaItem: DeletaItem? = DeletaItem()
                AtividadeController.apaga(position)
                deletaItem?.execute(ITEM)
                deletaItem = null
                Toast.makeText(context, "Atividade concluída com sucesso!", Toast.LENGTH_SHORT).show()
            } else  if(item?.tipo == 2){
                var ITEM = ItemChecklist(item?.id,item?.titulo,item?.disciplina,item?.prazo,item?.descricao,item?.tipo)
                var deletaItem: DeletaItem? = DeletaItem()
                LembreteController.apaga(position)
                deletaItem?.execute(ITEM)
                deletaItem = null
                Toast.makeText(context, "Lembrete concluído com sucesso!", Toast.LENGTH_SHORT).show()
            }
            btnMarcarConcluido.setImageResource(R.drawable.visto)
            val sharedPreference = context.getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putInt("deletarItem", 1)
            editor.commit()

        }

        val data = SimpleDateFormat("dd/MM/yyyy").parse(item?.prazo)
        val data_atual = Date()
        val diff =  (data.time - data_atual.time)/(24*60*60*1000).toLong()
        if(diff >= 2){
            fundoItem.setBackgroundResource(R.drawable.retangulo_item_azul)
        } else if(diff>=1){
            fundoItem.setBackgroundResource(R.drawable.retangulo_item_amarelo)
        } else if(diff<1){
            fundoItem.setBackgroundResource(R.drawable.retangulo_item_vermelho)
        }


        tituloItem.text = item?.titulo
        disciplinaItem.text = item?.disciplina
        prazoitem.text = item?.prazo

        return v
    }

    inner class DeletaItem : AsyncTask<ItemChecklist?, Void, Boolean?>(){

        override fun doInBackground(vararg params: ItemChecklist?): Boolean? {
            val item : ItemChecklist = params[0] as ItemChecklist

            try {
                var url: URL? = null

                if(item.tipo == 0){
                    url = URL("http://192.168.1.11:8080/Studiest/deletaAvaliacao.php")
                } else if(item.tipo == 1){
                    url = URL("http://192.168.1.11:8080/Studiest/deletaAtividade.php")
                } else if(item.tipo == 2){
                    url = URL("http://192.168.1.11:8080/Studiest/deletaLembrete.php")
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
