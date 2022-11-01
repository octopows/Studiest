package com.example.studiest

import UriUtils
import android.Manifest
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.example.studiest.R.layout.dialog_adicionar_anexo
import java.io.*
import java.util.*


class anexo_documentos : AppCompatActivity(){

    val PDF: Int = 100
    lateinit var arquivo: ByteArray
    private lateinit var dialog: AlertDialog
    private lateinit var dialog2: AlertDialog
    lateinit var anexoAdapter: AnexoAdapter
    var arquivoURI: Uri? = "null".toUri()
    lateinit var valorCampo: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anexo_documentos)

        val anexoRepositorio = AnexoRepositorio(this)
        AnexoController.listaDeAnexos().clear()
        AnexoController.listaDeAnexos().addAll(anexoRepositorio.buscarAnexos(null))

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
            var arquivoFile = File(anexo.arquivo)
            var arquivoURI = Uri.fromFile(arquivoFile)

            var arquivoURI2 = FileProvider.getUriForFile(
                Objects.requireNonNull(getApplicationContext()),
                BuildConfig.APPLICATION_ID + ".provider",arquivoFile)

            applicationContext.grantUriPermission(applicationContext.getPackageName(), arquivoURI2, Intent.FLAG_GRANT_READ_URI_PERMISSION);

            val target = Intent(Intent.ACTION_VIEW)
            target.setDataAndType(arquivoURI2, "application/pdf")
            target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION or  Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            val intent: Intent = Intent.createChooser(target, "Open File")

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "Instale um leitor de PDF.", Toast.LENGTH_SHORT).show()
            }
            /*
            try{
                val intent = Intent(this, anexoReader::class.java)
                intent.putExtra("pdfURI", arquivoURI.toString())
                startActivity(intent)
            }catch (e: Exception){
                Toast.makeText(this, "Não foi possível abrir o documento.", Toast.LENGTH_SHORT).show()
            }*/


        }

        listViewAnexo.setOnItemLongClickListener{parent, view, position, id ->
            var anexo: Anexo = AnexoController.getAnexo(position)
            showDialogAddAnexo(position)

            true
        }

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }
        valorCampo = "Selecionar Arquivo ▼"
        //abrir dialog para add/editar anexo
        btnAddAnexo.setOnClickListener{
            valorCampo = "Selecionar Arquivo ▼"
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
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
                if (SDK_INT >= Build.VERSION_CODES.R) {
                        val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                }
            } else{
                    val intentPDF = Intent(Intent.ACTION_OPEN_DOCUMENT)
                    intentPDF.type = "application/pdf"
                    intentPDF.addCategory(Intent.CATEGORY_OPENABLE)
                    startActivityForResult(Intent.createChooser(intentPDF, "Selecione um pdf"), PDF)

            }
        }

        campoSelecionarArquivo.text = valorCampo
        val ha = Handler()
        ha.postDelayed(object : Runnable {
            override fun run() {
                campoSelecionarArquivo.text = valorCampo
                ha.postDelayed(this, 100)
            }
        }, 100)
        cancelarAnexo.setOnClickListener { dialog.dismiss() }

        var position = p
        btnDeletarAnexo.setOnClickListener {
            showDialogDeletarAnexo(position)
        }

        lateinit var anexo: Anexo
        var alterar = if(p!=-1) true else false
        if (alterar) {
            anexo = AnexoController.getAnexo(p)
            val anexo: Anexo = AnexoController.getAnexo(p)
            campoTituloAnexo.setText(anexo.titulo)
            valorCampo ="Alterar Arquivo ▼"
        }

        btnConfirmarAnexo.setOnClickListener {
            var campoArquivo = campoSelecionarArquivo.text.toString()
            val titulo = campoTituloAnexo.text.toString()

            if(titulo.isNotEmpty() && campoSelecionarArquivo.text != "Selecionar Arquivo ▼" ){
                var stringPath = UriUtils.getRealPath(this, arquivoURI!!).toString()

                var arquivoFile = File(stringPath+"/")
                //Toast.makeText(this, "$stringPath", Toast.LENGTH_SHORT).show()

                campoTituloAnexo.text.clear()

                if (!alterar){
                    var arquivoPath = copyFile(arquivoFile,titulo)
                    val anexo = Anexo(0L,titulo,arquivoPath)
                    val anexoRepositorio = AnexoRepositorio(applicationContext)
                    var id = anexoRepositorio.inserir(anexo)
                    if(id>=0) {
                        anexo.id = id
                        AnexoController.listaDeAnexos().clear()
                        AnexoController.listaDeAnexos().addAll(anexoRepositorio.buscarAnexos(null))
                    }
                    arquivoURI = "null".toUri()
                }
                else{
                    arquivoURI == "null".toUri()
                    val anexoEdit = AnexoController.getAnexo(p)
                    var arquivoPath: String = anexoEdit.arquivo.toString()
                    if(campoSelecionarArquivo.text == "Alterar Arquivo ▼"){
                        val anexo = Anexo(anexoEdit.id,titulo,anexoEdit.arquivo)
                        val anexoRepositorio = AnexoRepositorio(applicationContext)
                        var numLinhasAfetadas = anexoRepositorio.atualizar(anexo)
                        if(numLinhasAfetadas>0) {
                            AnexoController.listaDeAnexos().clear()
                            AnexoController.listaDeAnexos().addAll(anexoRepositorio.buscarAnexos(null))
                        }
                    }else{
                        var stringPath = UriUtils.getRealPath(this, arquivoURI!!).toString()

                        var arquivoFile = File(stringPath+"/")

                        var arquivoDelete = File(anexoEdit.arquivo).delete()
                        arquivoPath = copyFile(arquivoFile,titulo)
                    }

                    val anexo = Anexo(anexoEdit.id,titulo,arquivoPath)

                    val anexoRepositorio = AnexoRepositorio(applicationContext)
                    var numLinhasAfetadas = anexoRepositorio.atualizar(anexo)
                    if(numLinhasAfetadas>0) {
                        AnexoController.listaDeAnexos().clear()
                        AnexoController.listaDeAnexos().addAll(anexoRepositorio.buscarAnexos(null))
                    }
                    arquivoURI == "null".toUri()
                }
                valorCampo = "Selecionar Arquivo ▼"
                dialog.dismiss()
            } else{
                campoTituloAnexo.error = if(titulo.isEmpty()) "Insira um nome para o arquivo" else null
                if(campoArquivo == "Selecionar Arquivo ▼"){
                    Toast.makeText(applicationContext, "Selecione um arquivo.", Toast.LENGTH_SHORT).show()
                }
            }
            anexoAdapter.clear()
            anexoAdapter.addAll(AnexoController.listaDeAnexos())
        }
        arquivoURI == "null".toUri()
        dialog = build.create()
        dialog.show()
    }

    @Throws(IOException::class)
    fun copyFile(src: File?, titulo: String?): String {
        var dst = File("/data/user/0/com.example.studiest/files/$titulo.pdf")

        var `in`: InputStream = FileInputStream(src)

        try {
            val out: OutputStream = FileOutputStream(dst)
            try {
                // Transfer bytes from in to out
                val buf = ByteArray(1024)
                var len: Int
                while (`in`.read(buf).also { len = it } > 0) {
                    out.write(buf, 0, len)
                }
            } finally {
                out.close()
            }
        } finally {
            `in`.close()
        }
        var arquivoPath = dst.toString()
        return arquivoPath
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
                val anexo = AnexoController.getAnexo(p)
                var arquivoFile = File(anexo.arquivo).delete()

                val anexoRepositorio = AnexoRepositorio(this)
                var numLinhasAfetadas = anexoRepositorio.excluir(anexo)
                if(numLinhasAfetadas>0){
                    atualizaLista()
                }

            }
            dialog.dismiss()
            dialog2.dismiss()
        }
        dialog2 = build.create()
        dialog2.show()
    }

    fun atualizaLista(){
        val anexoRepositorio = AnexoRepositorio(this)
        val anexos = anexoRepositorio.buscarAnexos(null)
        AnexoController.listaDeAnexos().clear()
        AnexoController.listaDeAnexos().addAll(anexos)
        anexoAdapter.clear()
        anexoAdapter.addAll(AnexoController.listaDeAnexos())
        anexoAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int,resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == PDF && data != null && data.data != null ){
            arquivoURI = data.data
            valorCampo = "Arquivo Selecionado ▼"
        }

    }

}