package com.example.studiest

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.pdf.PdfDocument.Page
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*
import kotlin.concurrent.schedule

class tela_principal : AppCompatActivity(){

    lateinit var itemChecklistAdapter: ItemChecklistAdapter
    private lateinit var dialog: AlertDialog
    private lateinit var tabChecklists: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_principal)

        tabChecklists = findViewById(R.id.tabChecklists)
        viewPager = findViewById(R.id.viewPager)
       viewPager.adapter = PagerAdapter(this)

        TabLayoutMediator(tabChecklists,viewPager){ tab, index ->
            tab.text = when(index){
                0 -> {"Avaliações"}
                1 -> {"Atividades"}
                2 -> {"Lembretes"}
                else -> throw Resources.NotFoundException("Position Not Found")
            }
        }.attach()


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
        val btnAddItem = findViewById<ImageView>(R.id.btnAddItem)
        val cumprimento = findViewById<TextView>(R.id.cumprimento)

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

        //botão para adicionar item
        btnAddItem.setOnClickListener{
            var selecionado = tabChecklists.selectedTabPosition
            val intent = Intent(this, adicionar_item::class.java)
            intent.putExtra("selecionado",selecionado)
            startActivity(intent)
        }

        //abrir anexador de documentos
        icon_auxiliares.setOnClickListener{
            val intent = Intent(this, auxiliares_academicos::class.java)
            startActivity(intent)
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


    }
/*
    override fun onResume() {
        super.onResume()
        itemChecklistAdapter.clear()
        itemChecklistAdapter.addAll(ItemChecklistController.listaDeItensChecklist())
    }*/
