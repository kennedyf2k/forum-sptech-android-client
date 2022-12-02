package br.com.studenton.fragments


import android.content.res.Resources
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.AdapterPerguntasResponse
import br.com.studenton.databinding.FragmentPerguntasBinding
import br.com.studenton.domain.Publicacao
import br.com.studenton.repository.Rest
import br.com.studenton.services.PerguntasService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Arrays
import java.util.ResourceBundle


class PerguntasFragment : Fragment() {

    private lateinit var binding: FragmentPerguntasBinding
    private lateinit var rvPerguntas: RecyclerView

    private lateinit var filterTodos: MutableList<Publicacao>
    private var acesso = -1
    private lateinit var adpterPerguntasResponse: AdapterPerguntasResponse
    private lateinit var adapterPerguntas: AdapterPerguntasResponse



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerguntasBinding.inflate(inflater)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        

        rvPerguntas = binding.recyclerViewPerguntas
        rvPerguntas.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvPerguntas.setHasFixedSize(true)
        acesso = arguments?.getInt("acesso")!!
        adpterPerguntasResponse = AdapterPerguntasResponse( acesso )
        rvPerguntas.adapter = adpterPerguntasResponse

        println("ACESSO DA VEZ " + acesso)

        var cRaces = resources.getStringArray(br.com.studenton.R.array.filter)
        when(acesso){

            1 ->   cRaces = resources.getStringArray(br.com.studenton.R.array.filter)
            2 ->   cRaces = resources.getStringArray(br.com.studenton.R.array.filterVeterano)


        }

     //   for (mString in cRaces) {
     //       println("ARRAAAAY" + mString)
//}

        var spinner = binding.idSpinnerPerguntas
        if(acesso == 2){
            spinner = binding.idSpinnerVeterano
        }else{
            spinner = binding.idSpinnerPerguntas
        }



        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println(p0?.selectedItem.toString())

                when(acesso){

                    1 -> {
                        binding.navTitulo.text = "Minhas Perguntas"

                        binding.enviado.visibility = View.VISIBLE

                        binding.navColaboracoes.visibility = View.GONE
                        binding.sublinhadoVeterano.visibility = View.GONE

                        binding.idSpinnerVeterano.visibility = View.GONE
                        binding.idSpinnerPerguntas.visibility = View.VISIBLE

                    }

                    2 -> {

                        binding.navTitulo.text = "Minhas Colaboraçõe"

                        binding.idSpinnerVeterano.visibility = View.VISIBLE
                        binding.idSpinnerPerguntas.visibility = View.GONE

                    }
                }

                when(p0?.selectedItem.toString()){

                    "Todos" -> filtrarTodos()
                    "Enviados" -> {
                        var lista = mutableListOf<Publicacao>()
                        for (perguntaDaVez in filterTodos){
                            if (perguntaDaVez.status == 1){
                                lista.add(perguntaDaVez)
                            }
                        }
                        adpterPerguntasResponse.setarDado(lista)
                    }
                    "Em análises" -> {
                        var lista = mutableListOf<Publicacao>()
                        for (perguntaDaVez in filterTodos){
                            if (perguntaDaVez.status == 2){
                                lista.add(perguntaDaVez)
                            }
                        }
                        adpterPerguntasResponse.setarDado(lista)
                    }
                    "Aprovados" -> {
                        var lista = mutableListOf<Publicacao>()
                        for (perguntaDaVez in filterTodos){
                            if (perguntaDaVez.status == 3){
                                lista.add(perguntaDaVez)
                            }
                        }
                        adpterPerguntasResponse.setarDado(lista)
                    }
                    "Recusados" -> {
                        var lista = mutableListOf<Publicacao>()
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

    private fun filtrarTodos(){
        Rest.getInstance<PerguntasService>().getPergunta(arguments?.getInt("id",0)!!)
            .enqueue(object: Callback<MutableList<Publicacao>> {

                override fun onResponse(
                    call: Call<MutableList<Publicacao>>,
                    response: Response<MutableList<Publicacao>>


                ) {
                    Log.i("AAAAAA", response.toString())

                    filterTodos = response.body()!!
                    adpterPerguntasResponse.setarDado(filterTodos)

                }

                override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                    Log.i("Erroo", t.stackTraceToString())
                }

            })

    }

}