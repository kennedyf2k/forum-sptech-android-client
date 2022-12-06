package br.com.studenton.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.AdapterPerguntasResponse
import br.com.studenton.databinding.FragmentPerguntasBinding

import br.com.studenton.domain.Publicacao
import br.com.studenton.repository.Rest
import br.com.studenton.services.PublicacaoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerguntasFragment : Fragment() {

    private lateinit var binding: FragmentPerguntasBinding
    private lateinit var rvPerguntas: RecyclerView
    private lateinit var filterTodos: MutableList<Publicacao>
    private var acesso = -1
    private var idUsuario = -1
    private lateinit var adpterPerguntasResponse: AdapterPerguntasResponse
    private lateinit var btnNav: String
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerguntasBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvPerguntas = binding.recyclerViewPerguntas
        rvPerguntas.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvPerguntas.setHasFixedSize(true)

        btnNav = "resposta"

        acesso = arguments?.getInt("acesso")!!
        idUsuario = arguments?.getInt("id")!!

        adpterPerguntasResponse = AdapterPerguntasResponse( acesso, idUsuario)
        {id, status, acesso -> carregarPublicacao(id, status, acesso)}

        rvPerguntas.adapter = adpterPerguntasResponse

        println("ACESSO DA VEZ $acesso USUARIO $idUsuario")

        binding.bottomNav2.setOnItemSelectedListener { filtrarPerguntasVet(it.itemId) }

        spinner = if(acesso == 2){

            binding.idSpinnerVeterano

        }else{

            binding.idSpinnerPerguntas

        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println(p0?.selectedItem.toString())

                when(acesso){

                    1 -> {
                        binding.navTitulo.text = getString(R.string.perguntas_nav_titulo_minhas_perguntas)

                        binding.enviado.visibility = View.VISIBLE

                        binding.navColaboracoes.visibility = View.GONE
                        binding.sublinhadoVeterano.visibility = View.GONE

                        binding.idSpinnerVeterano.visibility = View.GONE
                        binding.idSpinnerPerguntas.visibility = View.VISIBLE

                    }

                    2 -> {

                        binding.navTitulo.text = getString(R.string.perguntas_nav_titulo_minhas_colaboracoes)
                        binding.idSpinnerVeterano.visibility = View.VISIBLE
                        binding.idSpinnerPerguntas.visibility = View.GONE
                        binding.sublinhadoCalouro.visibility = View.GONE

                    }
                }

                when(p0?.selectedItem.toString()){

                    "Todos" -> {

                        if(btnNav == "resposta"){

                            filtrarPerguntasVet(R.id.perguntas_menu_respostas)

                        }else{

                            filtrarPerguntasVet(R.id.perguntas_menu_informaçoes)

                        }
                    }

                    "Enviados" -> {
                        val lista = mutableListOf<Publicacao>()
                        for (perguntaDaVez in filterTodos){
                            if (perguntaDaVez.status == 1){
                                lista.add(perguntaDaVez)
                            }
                        }
                        adpterPerguntasResponse.setarDado(lista)
                    }

                    "Em análises" -> {
                        val lista = mutableListOf<Publicacao>()
                        for (perguntaDaVez in filterTodos){
                            if (perguntaDaVez.status == 2){
                                lista.add(perguntaDaVez)
                            }
                        }
                        adpterPerguntasResponse.setarDado(lista)
                    }

                    "Aprovados" -> {
                        val lista = mutableListOf<Publicacao>()
                        for (perguntaDaVez in filterTodos){
                            if (perguntaDaVez.status == 3){
                                lista.add(perguntaDaVez)
                            }
                        }
                        adpterPerguntasResponse.setarDado(lista)
                    }

                    "Recusados" -> {
                        val lista = mutableListOf<Publicacao>()
                        for (perguntaDaVez in filterTodos){
                            if (perguntaDaVez.status >= 4){
                                lista.add(perguntaDaVez)
                            }
                        }
                        adpterPerguntasResponse.setarDado(lista)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

    }

    private fun filtrarPerguntasVet(itemId: Int):Boolean{

       when(acesso){
           1 -> {
               Rest.getInstance<PublicacaoService>().getMinhasPublicacoes(idUsuario)
                   .enqueue(object : Callback<MutableList<Publicacao>> {

                       override fun onResponse(
                           call: Call<MutableList<Publicacao>>,
                           response: Response<MutableList<Publicacao>>,
                       ) {

                           val lista = mutableListOf<Publicacao>()
                           for (perguntaDaVez in response.body()!!){
                               if (perguntaDaVez.tipoPublicacao == 2){
                                   lista.add(perguntaDaVez)
                               }
                           }
                            lista.reverse()
                           filterTodos = lista

                           adpterPerguntasResponse.setarDado(lista)

                           btnNav = "info"

                       }

                       override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                           Log.i("Cannot Get All publicacoes", t.stackTraceToString())
                       }
                   })

           }

           2 -> {
               when(itemId){

                   R.id.perguntas_menu_respostas -> {

                       spinner.setSelection(0)
                       binding.gestaoPerguntasSublinhado.visibility = View.VISIBLE
                       binding.gestaoPerguntasPublicacoesSublinhado.visibility = View.INVISIBLE
                       Rest.getInstance<PublicacaoService>().getMinhasColaboracoes(idUsuario)
                           .enqueue(object : Callback<MutableList<Publicacao>> {

                               override fun onResponse(
                                   call: Call<MutableList<Publicacao>>,
                                   response: Response<MutableList<Publicacao>>,
                               ) {

                                   val lista = mutableListOf<Publicacao>()

                                   for (perguntaDaVez in response.body()!!){
                                       if (perguntaDaVez.tipoPublicacao == 2){
                                           lista.add(perguntaDaVez)
                                       }
                                   }

                                   filterTodos = lista

                                   adpterPerguntasResponse.setarDado(lista)

                                   btnNav = "resposta"
                               }

                               override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                                   Log.i("Cannot Get All publicacoes", t.stackTraceToString())
                               }

                           })
                   }

                   R.id.perguntas_menu_informaçoes -> {

                       spinner.setSelection(0)
                       binding.gestaoPerguntasSublinhado.visibility = View.INVISIBLE
                       binding.gestaoPerguntasPublicacoesSublinhado.visibility = View.VISIBLE

                       Rest.getInstance<PublicacaoService>().getMinhasColaboracoes(idUsuario)
                           .enqueue(object : Callback<MutableList<Publicacao>> {

                               override fun onResponse(
                                   call: Call<MutableList<Publicacao>>,
                                   response: Response<MutableList<Publicacao>>,
                               ) {

                                   val lista = mutableListOf<Publicacao>()
                                   for (perguntaDaVez in response.body()!!){
                                       if (perguntaDaVez.tipoPublicacao == 1){
                                           lista.add(perguntaDaVez)
                                       }
                                   }

                                   filterTodos = lista

                                   adpterPerguntasResponse.setarDado(lista)

                                   btnNav = "info"

                               }

                               override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                                   Log.i("Cannot Get All publicacoes", t.stackTraceToString())
                               }
                           })
                   }
               }

           }
       }
        return true
    }

    private fun carregarPublicacao(idPublicacao: Int, status: Int, acesso: Int){

        val fragmentManager = activity?.supportFragmentManager

        val transaction = fragmentManager!!.beginTransaction()

        val visualizarPergunta = VisualizarPerguntaFragment()

        visualizarPergunta.arguments = bundleOf("id" to idPublicacao, "status" to status, "acesso" to acesso)

        transaction.replace(R.id.fragments_container, visualizarPergunta)

        transaction.commit()
    }
}