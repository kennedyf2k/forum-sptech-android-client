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
import br.com.studenton.services.CurtirService
import br.com.studenton.services.PublicacaoService
import br.com.studenton.services.SalvarService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private lateinit var selectionTracker: SelectionTracker<Long>
    private lateinit var rvFeed: RecyclerView
    private lateinit var rvCategorias: RecyclerView
    private lateinit var categorias: MutableList<Categoria>
    private lateinit var adapterPublicacoes: AdapterPublicacaoResponse
    private var idUsuario = -1

    companion object {

        const val SELECTION_TACKER_KEY = "selection-tracker-categoria"

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        retainInstance = true
        binding = FragmentFeedBinding.inflate(inflater)

        rvFeed = binding.rvFeed
        rvCategorias = binding.rvCategorias

        rvFeed.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvFeed.setHasFixedSize(true)

        rvCategorias.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvCategorias.setHasFixedSize(true)

        Log.i("ID_USUARIO", idUsuario.toString())
        idUsuario = arguments?.getInt("id")!!

        var adapterFiltros: AdapterCategoriaResponse

        kotlin.run {
            Rest.getInstance<CategoriaService>().categorias()
                .enqueue(object : Callback<MutableList<Categoria>> {

                    override fun onResponse(
                        call: Call<MutableList<Categoria>>,
                        response: Response<MutableList<Categoria>>
                    ) {

                        val categoria = Categoria(response.body()!!.size + 1, "Todas")

                        val lista = mutableListOf<Categoria>()

                        lista.add(categoria)
                        lista.addAll(response.body()!!)
                        categorias = lista
                        adapterFiltros = AdapterCategoriaResponse(
                            context!!, categorias,
                        ) { id ->

                            setarFeed(id)

                        }

                        rvCategorias.adapter = adapterFiltros

                        configSelectionTracker(savedInstanceState)

                    }

                    override fun onFailure(call: Call<MutableList<Categoria>>, t: Throwable) {
                        println("Cannot get Categoria")
                    }

                })
        }.run {

            return binding.root
        }
    }

    private fun configSelectionTracker(savedInstanceState: Bundle?) {

        selectionTracker = SelectionTracker.Builder(

            SELECTION_TACKER_KEY,
            rvCategorias,
            CategoriaKeyProvider(categorias),
            CategoriaLockup(rvCategorias),
            StorageStrategy.createLongStorage()

        ).withSelectionPredicate(CategoriaPredicate()).build()

        (rvCategorias.adapter as AdapterCategoriaResponse).selectionTracker = selectionTracker

        if (savedInstanceState != null) {

            selectionTracker.onRestoreInstanceState(savedInstanceState)

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        selectionTracker.onSaveInstanceState(outState)
    }

    fun setarFeed(idCategoria: Int?) {

        kotlin.run {

            if (idCategoria == categorias.size || idCategoria == null) {

                Rest.getInstance<PublicacaoService>().GetAllpublicacoes()
                    .enqueue(object : Callback<MutableList<Publicacao>> {

                        override fun onResponse(
                            call: Call<MutableList<Publicacao>>,
                            response: Response<MutableList<Publicacao>>
                        ) {

                            binding.shimmerFrameLayout.visibility = View.VISIBLE
                            binding.shimmerFrameLayout.startShimmerAnimation()

                            binding.rvFeed.visibility = View.GONE

                            adapterPublicacoes =
                                AdapterPublicacaoResponse(context!!, response.body()!!, idUsuario,{

                                        id -> curtir(id)

                                }){

                                        id -> salvar(id)

                                }

                            binding.shimmerFrameLayout.stopShimmerAnimation()
                            binding.shimmerFrameLayout.visibility = View.GONE
                            binding.llImgSemPublicacao.visibility = View.GONE

                            rvFeed.visibility = View.VISIBLE
                            rvFeed.adapter = adapterPublicacoes

                        }

                        override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                            Log.i("Cannot Get All publicacoes", t.stackTraceToString())
                        }

                    })

            } else {

                Rest.getInstance<PublicacaoService>().GetpublicacoesByCategoria(idCategoria)
                    .enqueue(object : Callback<MutableList<Publicacao>> {

                        override fun onResponse(
                            call: Call<MutableList<Publicacao>>,
                            response: Response<MutableList<Publicacao>>
                        ) {

                            if(response.body().isNullOrEmpty()){

                                binding.shimmerFrameLayout.stopShimmerAnimation()
                                binding.shimmerFrameLayout.visibility = View.GONE

                                rvFeed.visibility = View.GONE
                                binding.llImgSemPublicacao.visibility = View.VISIBLE

                            }else {

                                adapterPublicacoes =
                                    AdapterPublicacaoResponse(context!!, response.body()!!, idUsuario,
                                        {

                                                id ->
                                            curtir(id)

                                        }) {

                                            id ->
                                        salvar(id)

                                    }

                                binding.shimmerFrameLayout.stopShimmerAnimation()
                                binding.shimmerFrameLayout.visibility = View.GONE

                                rvFeed.visibility = View.VISIBLE

                                rvFeed.adapter = adapterPublicacoes

                            }

                        }

                        override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {

                            Log.i("Cannot Get publicacoesByCategoria", t.stackTraceToString())

                        }

                    })
            }
        }
    }

    fun curtir(idPublicacao: Int){

        Rest.getInstance<CurtirService>().curtir(arguments?.getInt("id")!!, idPublicacao)

            .enqueue(object: Callback<Boolean>{

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.i("ERRO", t.stackTraceToString())
            }
        })
    }

    fun salvar(idPublicacao: Int){

        Rest.getInstance<SalvarService>().favoritar(arguments?.getInt("id")!!, idPublicacao)

            .enqueue(object: Callback<Boolean>{

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {

                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.i("ERRO", t.stackTraceToString())
                }
            })

    }

}