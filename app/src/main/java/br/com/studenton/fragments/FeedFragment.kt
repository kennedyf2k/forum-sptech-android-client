package br.com.studenton.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.adapter.AdapterCategoriaResponse
import br.com.studenton.adapter.AdapterPublicacaoResponse
import br.com.studenton.adapter.tracker.CategoriaKeyProvider
import br.com.studenton.adapter.tracker.CategoriaLockup
import br.com.studenton.adapter.tracker.CategoriaPredicate
import br.com.studenton.databinding.FragmentFeedBinding
import br.com.studenton.domain.Categoria
import br.com.studenton.domain.Publicacao
import br.com.studenton.repository.Rest
import br.com.studenton.services.CategoriaService
import br.com.studenton.services.PublicacaoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FeedFragment : Fragment()
{

    private lateinit var binding: FragmentFeedBinding
    private lateinit var selectionTracker: SelectionTracker<Long>
    private lateinit var rv_feed: RecyclerView
    private lateinit var rv_categorias: RecyclerView
    private lateinit var categorias: MutableList<Categoria>

    companion object{

        const val SELECTION_TACKER_KEY = "selection-tracker-categoria"

    }
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        retainInstance = true
        binding = FragmentFeedBinding.inflate(inflater)


        rv_feed = binding.rvFeed;
        rv_categorias = binding.rvCategorias;

        rv_feed.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false )
        rv_feed.setHasFixedSize(true)
        var adapterPublicacoes: AdapterPublicacaoResponse

        rv_categorias.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rv_categorias.setHasFixedSize(true)
        var adapterFiltros: AdapterCategoriaResponse

        Rest.getInstance<PublicacaoService>().GetAllpublicacoes()
            .enqueue(object: Callback<MutableList<Publicacao>>{

                override fun onResponse(
                    call: Call<MutableList<Publicacao>>,
                    response: Response<MutableList<Publicacao>>
                ) {

                    adapterPublicacoes = AdapterPublicacaoResponse(context!!, response.body()!!)

                    rv_feed.adapter = adapterPublicacoes

                }

                override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                    println("Cannot get Publicacao")
                }

            }).run {
                Rest.getInstance<CategoriaService>().categorias()
                    .enqueue(object: Callback<MutableList<Categoria>>{

                        override fun onResponse(
                            call: Call<MutableList<Categoria>>,
                            response: Response<MutableList<Categoria>>
                        ) {

                            var categoria = Categoria( response.body()!!.size +1, "Todas"  )

                            var lista = mutableListOf<Categoria>()

                            lista.add(categoria)
                            lista.addAll(response.body()!!)
                            categorias = lista
                            adapterFiltros = AdapterCategoriaResponse(context!!, categorias)

                            rv_categorias.adapter = adapterFiltros

                            configSelectionTracker( savedInstanceState )

                        }

                        override fun onFailure(call: Call<MutableList<Categoria>>, t: Throwable) {
                            println("Cannot get Categoria")
                        }

                    })
            }.run {
                return binding.root
            }
    }

    private fun configSelectionTracker( savedInstanceState: Bundle? ){

        selectionTracker = SelectionTracker.Builder<Long>(

            SELECTION_TACKER_KEY,
            rv_categorias,
            CategoriaKeyProvider( categorias ),
            CategoriaLockup( rv_categorias ),
            StorageStrategy.createLongStorage()

        ).withSelectionPredicate( CategoriaPredicate() ).build()

        ( rv_categorias.adapter as AdapterCategoriaResponse ).selectionTracker = selectionTracker

        if( savedInstanceState != null ){

            selectionTracker.onRestoreInstanceState( savedInstanceState )

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectionTracker.onSaveInstanceState(outState)
    }

}