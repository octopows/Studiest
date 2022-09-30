package com.example.studiest

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

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

        val ha = Handler()
        ha.postDelayed(object : Runnable {
            override fun run() {
                var selecionado = tabChecklists.selectedTabPosition
                quadroInformativo(selecionado)
                ha.postDelayed(this, 500)
            }
        }, 500)

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

    override fun onResume() {
        super.onResume()

        tabChecklists = findViewById(R.id.tabChecklists)
        var selecionado = tabChecklists.selectedTabPosition

        quadroInformativo(selecionado)
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

    private fun quadroInformativo(tipo: Int){
        val qtdPrioridade = findViewById<TextView>(R.id.qtdPrioridade)
        val qtdChegando = findViewById<TextView>(R.id.qtdChegando)
        val qtdDistante = findViewById<TextView>(R.id.qtdDistante)

        if(tipo ==0){
            var lista = AvaliacaoController.listaDeAvaliacoes()
            var prioridade = 0
            var chegando = 0
            var distante = 0

            for (j in 0 until lista.size) {
                var item = AvaliacaoController.getAvaliacao(j)
                val data = SimpleDateFormat("dd/MM/yyyy").parse(item.prazo)

                val data_atual = Date()
                val diff =  (data.time - data_atual.time)/(24*60*60*1000).toLong()
                if(diff >= 2){
                    distante=distante+1
                } else if(diff>=1){
                    chegando = chegando+1
                } else if(diff<1){
                    prioridade = prioridade+1
                }
            }
            //definindo os valores do quadro
            if(prioridade>0){
                qtdPrioridade.text = prioridade.toString()
            }else{
                qtdPrioridade.text = "-"
            }

            if(chegando>0){
                qtdChegando.text = chegando.toString()
            }else
                qtdChegando.text = "-"

            if(distante>0){
                qtdDistante.text = distante.toString()
            }else{
                qtdDistante.text = "-"
            }
        } else if(tipo ==1){
            var lista = AtividadeController.listaDeAtividades()
            var prioridade = 0
            var chegando = 0
            var distante = 0

            for (j in 0 until lista.size) {
                var item = AtividadeController.getAtividade(j)
                val data = SimpleDateFormat("dd/MM/yyyy").parse(item.prazo)

                val data_atual = Date()
                val diff =  (data.time - data_atual.time)/(24*60*60*1000).toLong()
                if(diff >= 2){
                    distante=distante+1
                } else if(diff>=1){
                    chegando = chegando+1
                } else if(diff<1){
                    prioridade = prioridade+1
                }
            }
            //definindo os valores do quadro
            if(prioridade>0){
                qtdPrioridade.text = prioridade.toString()
            }else{
                qtdPrioridade.text = "-"
            }

            if(chegando>0){
                qtdChegando.text = chegando.toString()
            }else
                qtdChegando.text = "-"

            if(distante>0){
                qtdDistante.text = distante.toString()
            }else{
                qtdDistante.text = "-"
            }
        }else if (tipo ==2){
            var lista = LembreteController.listaDeLembretes()
            var prioridade = 0
            var chegando = 0
            var distante = 0

            for (j in 0 until lista.size) {
                var item = LembreteController.getLembrete(j)
                val data = SimpleDateFormat("dd/MM/yyyy").parse(item.prazo)

                val data_atual = Date()
                val diff =  (data.time - data_atual.time)/(24*60*60*1000).toLong()
                if(diff >= 2){
                    distante=distante+1
                } else if(diff>=1){
                    chegando = chegando+1
                } else if(diff<1){
                    prioridade = prioridade+1
                }
            }
            //definindo os valores do quadro
            if(prioridade>0){
                qtdPrioridade.text = prioridade.toString()
            }else{
                qtdPrioridade.text = "-"
            }

            if(chegando>0){
                qtdChegando.text = chegando.toString()
            }else
                qtdChegando.text = "-"

            if(distante>0){
                qtdDistante.text = distante.toString()
            }else{
                qtdDistante.text = "-"
            }
        }




    }
}
