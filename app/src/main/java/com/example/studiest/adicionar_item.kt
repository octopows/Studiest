package com.example.studiest

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import java.util.*

class adicionar_item : AppCompatActivity() {

    private lateinit var dialog: AlertDialog

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

        
        campoDataEntrega.setOnClickListener {
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener{view,mYear,mMonth,mDay ->
                if((mMonth+1) <10 && mDay <10)
                campoDataEntrega.setText(""+0+mDay+"/"+0+(mMonth+1)+"/"+mYear+" ▼")
                else if((mMonth+1) <10 && mDay >=10)
                campoDataEntrega.setText(""+mDay+"/"+0+(mMonth+1)+"/"+mYear+" ▼")
                    else if((mMonth+1) >=10 && mDay <10)
                    campoDataEntrega.setText(""+0+mDay+"/"+(mMonth+1)+"/"+mYear+" ▼")
                    else
                    campoDataEntrega.setText(""+mDay+"/"+(mMonth+1)+"/"+mYear+" ▼")
            },year,month,day)
            dpd.show()
        }


        //salvar item
        val btnSalvarItem = findViewById<Button>(R.id.btnSalvarItem)
        val campoTituloItem = findViewById<EditText>(R.id.campoTituloItem)
        val campoDisciplinaItem = findViewById<EditText>(R.id.campoDisciplinaItem)
        val campoDescricaoItem = findViewById<EditText>(R.id.campoDescricaoItem)
        val campoPrazo = findViewById<TextView>(R.id.campoPrazo)

        val it : Intent = intent
        var p = it.getIntExtra("p",-1)

        btnSalvarItem.setOnClickListener {
            val titulo = campoTituloItem.text.toString()
            val disciplina = campoDisciplinaItem.text.toString()
            val descricao = campoDescricaoItem.text.toString()
            val prazo = campoPrazo.text.toString()

            if(titulo.isNotEmpty() && disciplina.isNotEmpty()){
                val itemCheck = ItemChecklist(titulo, disciplina, prazo, descricao)
                campoTituloItem.text.clear()
                campoTituloItem.requestFocus()
                campoDisciplinaItem.text.clear()
                campoDescricaoItem.text.clear()
                campoPrazo.text = "Selecionar ▼"

                if (p == -1){
                    ItemChecklistController.cadastra(itemCheck)
                    finish()
                }
                else{
                    ItemChecklistController.atualiza(p,itemCheck)
                    finish()
                }
            }else{
                campoTituloItem.error = if(titulo.isEmpty()) "Insira um título" else null
                campoDisciplinaItem.error = if(disciplina.isEmpty()) "Insira uma disciplina" else null
            }
        }
        if (p!=-1) {
            val itemChecklist: ItemChecklist = ItemChecklistController.getItemChecklist(p)
            campoTituloItem.setText(itemChecklist.titulo)
            campoDisciplinaItem.setText(itemChecklist.disciplina)
            campoDescricaoItem.setText(itemChecklist.descricao)
            campoPrazo.setText(itemChecklist.prazo)
        }
    }

    //função para chamar dialog sair
    private fun showDialogRemoverItem(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_remover_item, null)
        build.setView(view)

        val cancelarRemoverItem = view.findViewById<TextView>(R.id.cancelarRemoverResumo)
        val confirmarRemoverItem = view.findViewById<TextView>(R.id.confirmarRemoverResumo)

        cancelarRemoverItem.setOnClickListener { dialog.dismiss() }

        confirmarRemoverItem.setOnClickListener {
            finish()
        }
        dialog = build.create()
        dialog.show()
    }
}