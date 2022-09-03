package com.example.studiest

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import java.util.*
import kotlin.concurrent.schedule

class tela_principal : AppCompatActivity() {

    lateinit var itemChecklistAdapter: ItemChecklistAdapter
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)
/*
        //definindo adapter
        itemChecklistAdapter = ItemChecklistAdapter(this)
        itemChecklistAdapter.addAll(ItemChecklistController.listaDeItensChecklist())

        val listViewItemChecklist = findViewById<ListView>(R.id.lista_checklists)
        listViewItemChecklist.adapter = itemChecklistAdapter

        listViewItemChecklist.setOnItemClickListener { parent, view, position, id ->
            var itemChecklist: ItemChecklist = ItemChecklistController.getItemChecklist(position)
            val intentAlterar = Intent(this, adicionar_item::class.java)
            intentAlterar.putExtra("p",position)
            startActivity(intentAlterar)
        }
*/
        val icon_perfil = findViewById<ImageView>(R.id.icon_perfil)
        val icon_estudo = findViewById<ImageView>(R.id.icon_estudo)
        val icon_auxiliares = findViewById<ImageView>(R.id.icon_auxiliares)
        val icon_calendario = findViewById<ImageView>(R.id.icon_calendario)
        val btnAddItem2 = findViewById<Button>(R.id.btnAddItem2)
        val btnAddItem = findViewById<ImageView>(R.id.btnAddItem)
        val cumprimento = findViewById<TextView>(R.id.cumprimento)
        val btnOrdenar = findViewById<TextView>(R.id.btnOrdenar)

        val tresBarras = findViewById<ImageView>(R.id.imageView15)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val headerview: View = navigationView.getHeaderView(0)
        val btnAddChecklist = headerview.findViewById<TextView>(R.id.btnAddChecklist)
        val navHeader: ConstraintLayout = headerview.findViewById(R.id.nav_header)
        //abrir barra de navegação lateral
        tresBarras.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.END);
        }

        //abrir dialog para add checklist
        headerview.setOnClickListener {
            showDialogAddChecklist()
        }

        //mensagem de cumprimento
        val horario = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val intent: Intent = this.intent

        val sharedPreference =  getSharedPreferences("dadosUsuario", Context.MODE_PRIVATE)
        var nome = sharedPreference.getString("nome","Null") as String

        var SplitNome = nome!!.split(" ").toTypedArray()
        var first = SplitNome[0]


        when (horario) {
            in 0..11 -> cumprimento.text = "Bom dia, $first!"
            in 12..17 -> cumprimento.text = "Boa tarde, $first!"
            in 18..23 -> cumprimento.text = "Boa noite, $first!"
        }

        //botão perfil
        icon_perfil.setOnClickListener{
            val intent = Intent(this, perfil::class.java)
            startActivity(intent)
        }

        //botão calendario
        icon_calendario.setOnClickListener{
            val intent = Intent(this, calendario::class.java)
            startActivity(intent)
        }

        //botão estudos
        icon_estudo.setOnClickListener{
            val intent = Intent(this, estudos::class.java)
            startActivity(intent)
        }

        //botões para adicionar item
        btnAddItem.setOnClickListener{
            val intent = Intent(this, adicionar_item::class.java)
            startActivity(intent)
        }
        btnAddItem2.setOnClickListener{
            val intent = Intent(this,adicionar_item::class.java)
            startActivity(intent)
        }

        //abrir anexador de documentos
        icon_auxiliares.setOnClickListener{
            val intent = Intent(this, auxiliares_academicos::class.java)
            startActivity(intent)
        }
        //abrir dialog ordenar
        btnOrdenar.setOnClickListener {
            showDialogOrdenar()
        }
    }

    //função para chamar dialog sair
    private fun showDialogAddChecklist(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_adicionar_checklist, null)
        build.setView(view)

        val cancelarAdicionarChecklist = view.findViewById<TextView>(R.id.cancelarAdicionarChecklist)
        val confirmarAdicionarChecklist = view.findViewById<TextView>(R.id.confirmarAdicionarChecklist)
        val campoAddChecklist = view.findViewById<EditText>(R.id.campoAddChecklist)

        cancelarAdicionarChecklist.setOnClickListener { dialog.dismiss() }

        confirmarAdicionarChecklist.setOnClickListener {
            var nomeCheck = campoAddChecklist.text.toString()

            if(nomeCheck.isNotEmpty()){
                dialog.dismiss()
                Toast.makeText(this, "Checklist adicionada!", Toast.LENGTH_SHORT).show()
            }
            else
                campoAddChecklist.error = if(nomeCheck.isEmpty()) "Insira um nome" else null
        }
        dialog = build.create()
        dialog.show()
    }

    //função para chamar dialog sair
    private fun showDialogOrdenar(){
        val build = AlertDialog.Builder(this, R.style.ThemeCustomDialog)
        val view = layoutInflater.inflate(R.layout.dialog_ordenar, null)
        build.setView(view)

        val ordenarPrazo = view.findViewById<TextView>(R.id.ordenarPrazo)
        val ordenarAlfabetica = view.findViewById<TextView>(R.id.ordenarAlfabetica)
        val vistoPrazo = view.findViewById<ImageView>(R.id.vistoPrazo)
        val vistoAlfabetica = view.findViewById<ImageView>(R.id.vistoAlfabetica)

        ordenarPrazo.setOnClickListener {
            ordenarPrazo.setTextColor(ContextCompat.getColor(this,R.color.azul))
            vistoPrazo.setImageResource(R.drawable.visto)
            ordenarAlfabetica.setTextColor(ContextCompat.getColor(this,R.color.cinza_hint))
            vistoAlfabetica.setImageResource(0)
            Timer().schedule(200){
                dialog.dismiss()
            }
        }

        ordenarAlfabetica.setOnClickListener {
            ordenarPrazo.setTextColor(ContextCompat.getColor(this,R.color.cinza_hint))
            vistoPrazo.setImageResource(0)
            ordenarAlfabetica.setTextColor(ContextCompat.getColor(this,R.color.azul))
            vistoAlfabetica.setImageResource(R.drawable.visto)
            Timer().schedule(200){
                dialog.dismiss()
            }
        }

        dialog = build.create()
        dialog.show()
    }
/*
    override fun onResume() {
        super.onResume()
        itemChecklistAdapter.clear()
        itemChecklistAdapter.addAll(ItemChecklistController.listaDeItensChecklist())
    }*/

}