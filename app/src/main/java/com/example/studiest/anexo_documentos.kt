package com.example.studiest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.studiest.R.layout.dialog_adicionar_anexo


class anexo_documentos : AppCompatActivity() {

    val PDF: Int = 100
    lateinit var arquivo: ByteArray
    private lateinit var dialog: AlertDialog
    private lateinit var dialog2: AlertDialog
    lateinit var anexoAdapter: AnexoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anexo_documentos)

        anexoAdapter = AnexoAdapter(this)
        anexoAdapter.addAll(AnexoController.listaDeAnexos())
        val semAnexos = findViewById<ImageView>(R.id.semAnexos)
        val listViewAnexo = findViewById<ListView>(R.id.listview_anexo)
        listViewAnexo.adapter = anexoAdapter

        listViewAnexo.setEmptyView(semAnexos)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnAddAnexo = findViewById<ImageView>(R.id.btnAddAnexo)

        listViewAnexo.setOnItemClickListener{parent, view, position, id ->
            var anexo: Anexo = AnexoController.getAnexo(position)
             showDialogAddAnexo(position)
        }


        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //abrir dialog para add/editar anexo
        btnAddAnexo.setOnClickListener{
            showDialogAddAnexo(-1)
        }

    }

    override fun onResume() {
        super.onResume()
        anexoAdapter.clear()
        anexoAdapter.addAll(AnexoController.listaDeAnexos())

    }

    //função para chamar dialog sair
    private fun showDialogAddAnexo(p: Int){

        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(dialog_adicionar_anexo, null)
        build.setView(view)

        val cancelarAnexo = view.findViewById<TextView>(R.id.cancelarAnexo)
        val btnConfirmarAnexo = view.findViewById<Button>(R.id.btnConfirmarAnexo)
        val campoTituloAnexo = view.findViewById<EditText>(R.id.campoNomeAnexo)
        val btnDeletarAnexo = view.findViewById<ImageView>(R.id.btnDeletarAnexo)
        val retanguloArquivo = view.findViewById<View>(R.id.retanguloArquivo)
        val campoSelecionarArquivo = view.findViewById<TextView>(R.id.campoSelecionarArquivo)

        retanguloArquivo.setOnClickListener {
            val intentPDF = Intent(Intent.ACTION_GET_CONTENT)
            intentPDF.type = "application/pdf"
            intentPDF.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(Intent.createChooser(intentPDF, "Selecione um pdf"), PDF)

        }

        cancelarAnexo.setOnClickListener { dialog.dismiss() }

        var position = p
        btnDeletarAnexo.setOnClickListener {
            showDialogDeletarAnexo(position)
        }

        btnConfirmarAnexo.setOnClickListener {
            var id = -1
            val titulo = campoTituloAnexo.text.toString()

            if(titulo.isNotEmpty()){
                val anexo = Anexo(id,titulo)
                campoTituloAnexo.text.clear()

                if (p == -1){
                    AnexoController.cadastra(anexo)
                    /*
                    var cadastraAnexo: CadastraAnexo? = CadastraAnexo()
                    cadastraAnexo?.execute(anexo)
                    cadastraAnexo= null

                     */

                }
                else{
                    val anexoEdit = AnexoController.getAnexo(p)
                    val anexo = Anexo(anexoEdit.id,titulo)
                    AnexoController.atualiza(p,anexo)
                    /*
                    var editaAnexo: EditaAnexo? = EditaAnexo()
                    editaAnexo?.execute(anexo)
                    editaAnexo = null

                     */

                }
                dialog.dismiss()

            }
            else{
                campoTituloAnexo.error = if(titulo.isEmpty()) "Insira um nome para o arquivo" else null
            }

            anexoAdapter.clear()
            anexoAdapter.addAll(AnexoController.listaDeAnexos())
        }

        if (p!=-1) {
            val anexo: Anexo = AnexoController.getAnexo(p)
            campoTituloAnexo.setText(anexo.titulo)

        }
        dialog = build.create()
        dialog.show()
    }

    //função para chamar dialog sair
    private fun showDialogDeletarAnexo(p: Int){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_deletar_anexo, null)
        build.setView(view)

        val cancelarDeletarAnexo = view.findViewById<TextView>(R.id.cancelarRemoverAnexo)
        val confirmarDeletarAnexo = view.findViewById<TextView>(R.id.confirmarRemoverAnexo)


        cancelarDeletarAnexo.setOnClickListener {
            dialog2.dismiss() }

        confirmarDeletarAnexo.setOnClickListener {
            if(p!=-1) {
                val anexoEdit = AnexoController.getAnexo(p)
              /*  val resumo = Resumo(resumoEdit.id,resumoEdit.titulo,resumoEdit.disciplina,resumoEdit.conteudo)
                var deletaResumo: adicionar_resumo.DeletaResumo? = DeletaResumo()
                deletaResumo?.execute(resumo)
                deletaResumo = null*/
                AnexoController.apaga(p)
                anexoAdapter.clear()
                anexoAdapter.addAll(AnexoController.listaDeAnexos())
            }
            dialog.dismiss()
            dialog2.dismiss()
        }
        dialog2 = build.create()
        dialog2.show()
    }

    override fun onActivityResult(requestCode: Int,resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)

        val legendaAnexador = findViewById<TextView>(R.id.legendaAnexador)
        if(resultCode == Activity.RESULT_OK && requestCode == PDF && data != null && data.data != null ){
            val selectedPdfFromStorage = data.data
            openPDF(this,selectedPdfFromStorage)
        }

    }

    fun openPDF(context: Context, localUri: Uri?) {
        val i = Intent(Intent.ACTION_VIEW)
        i.setDataAndType(localUri, "application/pdf")
        context.startActivity(i)
    }

}