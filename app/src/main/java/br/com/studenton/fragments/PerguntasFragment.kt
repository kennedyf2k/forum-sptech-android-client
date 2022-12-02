package br.com.studenton.fragments


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


class PerguntasFragment : Fragment() {

    private lateinit var binding: FragmentPerguntasBinding
    private lateinit var rvPerguntas: RecyclerView

    private lateinit var filterTodos: MutableList<Publicacao>

    private lateinit var adpterPerguntasResponse: AdapterPerguntasResponse



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

        adpterPerguntasResponse = AdapterPerguntasResponse()
        rvPerguntas.adapter = adpterPerguntasResponse


        val cRaces = resources.getStringArray(br.com.studenton.R.array.filter)

        for (mString in cRaces) {
            println("ARRAAAAY" + mString)

        }

        val spinnerr = binding.idSpinnerPerguntas


        spinnerr.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                println(p0?.selectedItem.toString())

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