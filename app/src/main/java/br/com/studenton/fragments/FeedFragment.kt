package br.com.studenton.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.studenton.adapter.AdapterCategoriaResponse
import br.com.studenton.databinding.FragmentFeedBinding
import br.com.studenton.models.response.CategoriaResponse
import br.com.studenton.rest.Rest
import br.com.studenton.services.Categoria
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFeedBinding.inflate(inflater)

        val rv_feed = binding.rvFeed;
        val rv_categorias = binding.rvCategorias;

        rv_feed.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false )
        rv_feed.setHasFixedSize(true)

        rv_categorias.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        rv_categorias.setHasFixedSize(true)
        var adapterFiltros: AdapterCategoriaResponse

        val request = Rest.getInstance().create(Categoria::class.java)

        request.categorias().enqueue(object: Callback<MutableList<CategoriaResponse>>{

            override fun onResponse(
                call: Call<MutableList<CategoriaResponse>>,
                response: Response<MutableList<CategoriaResponse>>
            ) {

                adapterFiltros = AdapterCategoriaResponse(context!!, response.body()!!)

                rv_categorias.adapter = adapterFiltros

            }

            override fun onFailure(call: Call<MutableList<CategoriaResponse>>, t: Throwable) {

            }

        })

        return binding.root

    }
}