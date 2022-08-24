package com.example.studiest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog

class anexo_documentos : AppCompatActivity() {

    private lateinit var dialog: AlertDialog
    private lateinit var dialog2: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anexo_documentos)

        val btnVoltar = findViewById<ImageView>(R.id.btnVoltar)
        val btnAddAnexo = findViewById<ImageView>(R.id.btnAddAnexo)

        //botão para voltar
        btnVoltar.setOnClickListener{
            finish()
        }

        //abrir dialog para add/editar anexo
        btnAddAnexo.setOnClickListener{
            showDialogAddAnexo()
        }

    }



    //função para chamar dialog sair
    private fun showDialogAddAnexo(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_adicionar_anexo, null)
        build.setView(view)


        val cancelarAnexo = view.findViewById<TextView>(R.id.cancelarAnexo)
        val btnConfirmarAnexo = view.findViewById<TextView>(R.id.btnConfirmarAnexo)
        val campoNomeAnexo = view.findViewById<EditText>(R.id.campoNomeAnexo)
        val btnDeletarAnexo = view.findViewById<ImageView>(R.id.btnDeletarAnexo)

        cancelarAnexo.setOnClickListener { dialog.dismiss() }

        btnDeletarAnexo.setOnClickListener {
            showDialogDeletarAnexo() }

        btnConfirmarAnexo.setOnClickListener {
            var anexo = campoNomeAnexo.text.toString()

            if(anexo.isNotEmpty()){
                dialog.dismiss()
                Toast.makeText(this, "Anexo Adicionado!", Toast.LENGTH_SHORT).show()
            }
            else{
                campoNomeAnexo.error = if(anexo.isEmpty()) "Insira um nome para o arquivo" else null
            }

        }

        dialog = build.create()
        dialog.show()
    }

    //função para chamar dialog sair
    private fun showDialogDeletarAnexo(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_deletar_anexo, null)
        build.setView(view)

        val cancelarDeletarAnexo = view.findViewById<TextView>(R.id.cancelarRemoverAnexo)
        val confirmarDeletarAnexo = view.findViewById<TextView>(R.id.confirmarRemoverAnexo)

        cancelarDeletarAnexo.setOnClickListener {
            dialog2.dismiss() }

        confirmarDeletarAnexo.setOnClickListener {
            dialog.dismiss()
            dialog2.dismiss()
        }
        dialog2 = build.create()
        dialog2.show()
    }
}