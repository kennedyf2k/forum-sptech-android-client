package br.com.studenton.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        Rest.getInstance<PerguntasService>().getPergunta(arguments?.getInt("id",0)!!)
            .enqueue(object: Callback<MutableList<Publicacao>> {

                override fun onResponse(
                    call: Call<MutableList<Publicacao>>,
                    response: Response<MutableList<Publicacao>>
                ) {
                    Log.i("AAAAAA", response.toString())
                    rvPerguntas.adapter = AdapterPerguntasResponse(response.body()!!)
                }

                override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                    Log.i("Erroo", t.stackTraceToString())
                }

            })


    }
}