package br.com.studenton.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.AdapterCategoriaResponse
import br.com.studenton.adapter.AdapterPublicacaoResponse
import br.com.studenton.adapter.tracker.CategoriaKeyProvider
import br.com.studenton.adapter.tracker.CategoriaLockup
import br.com.studenton.adapter.tracker.CategoriaPredicate
import br.com.studenton.databinding.FragmentDialogPerguntarBinding
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

    private var idUsuario = -1
    private var acesso = -1
    private lateinit var adapterPublicacoes: AdapterPublicacaoResponse


    var contador = 0
    var contador2 = 0

    companion object {

        const val SELECTION_TACKER_KEY = "selection-tracker-categoria"

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvFeed = binding.rvFeed

        rvFeed.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvFeed.setHasFixedSize(true)

        rvCategorias = binding.rvCategorias

        rvCategorias.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        rvCategorias.setHasFixedSize(true)

        idUsuario = arguments?.getInt("id")!!

        acesso = arguments?.getInt("acesso")!!

        adapterPublicacoes = AdapterPublicacaoResponse(activity?.baseContext!!, idUsuario, acesso,{
                id -> curtir(id)
        }){
                id -> salvar(id)
        }

        rvFeed.adapter = adapterPublicacoes

        binding.fbPerguntar.setOnClickListener{

            var fragmentDialog = DialogPerguntar()

            val fragmentManager = activity?.supportFragmentManager

            fragmentDialog.show(fragmentManager!!, "");

        }

        binding.bottomNav.setOnItemSelectedListener { setarFeedByBottomNavigation(it.itemId) }

        var adapterFiltros: AdapterCategoriaResponse

        Rest.getInstance<CategoriaService>().categorias()
            .enqueue(object : Callback<MutableList<Categoria>> {

                override fun onResponse(
                    call: Call<MutableList<Categoria>>,
                    response: Response<MutableList<Categoria>>
                ) {

                    val categoria = Categoria(-1, "Todas")

                    val lista = mutableListOf<Categoria>()

                    lista.add(categoria)
                    lista.addAll(response.body()!!)
                    categorias = lista

                    adapterFiltros = AdapterCategoriaResponse(
                        context!!, categorias,
                    ) { id ->

                        setarFeedByCategoria(id)

                    }

                    rvCategorias.adapter = adapterFiltros

                    configSelectionTracker(savedInstanceState)

                }

                override fun onFailure(call: Call<MutableList<Categoria>>, t: Throwable) {
                    println("Cannot get Categoria")
                }

            })

    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentFeedBinding.inflate(inflater)
        return binding.root
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

    private fun setarFeedByCategoria(idCategoria: Int?) {


            if (idCategoria == -1 || idCategoria == null) {
                setarFeedByBottomNavigation(R.id.feed_menu_relevante)

            } else {

                Rest.getInstance<PublicacaoService>().getpublicacoesByCategoria(idCategoria)
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



                                adapterPublicacoes.setData(response.body()!!)

                                binding.shimmerFrameLayout.stopShimmerAnimation()
                                binding.shimmerFrameLayout.visibility = View.GONE
                                binding.llImgSemPublicacao.visibility = View.GONE
                                binding.feedSelectedRigth.visibility = View.INVISIBLE
                                binding.feedSelectedLeft.visibility = View.INVISIBLE

                                rvFeed.visibility = View.VISIBLE


                            }

                        }

                        override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {

                            Log.i("Cannot Get publicacoesByCategoria", t.stackTraceToString())

                        }

                    })
            }
    }

    private fun setarFeedByBottomNavigation(itemId: Int): Boolean{

        when(itemId){

            R.id.feed_menu_colaboracoes -> {

                Rest.getInstance<PublicacaoService>().getMinhasPublicacoes(idUsuario)
                    .enqueue(object : Callback<MutableList<Publicacao>> {

                        override fun onResponse(
                            call: Call<MutableList<Publicacao>>,
                            response: Response<MutableList<Publicacao>>
                        ) {

                            binding.feedSelectedRigth.visibility = View.VISIBLE
                            binding.feedSelectedLeft.visibility = View.INVISIBLE

                            binding.shimmerFrameLayout.visibility = View.VISIBLE
                            binding.shimmerFrameLayout.startShimmerAnimation()

                            binding.rvFeed.visibility = View.GONE

                            binding.shimmerFrameLayout.stopShimmerAnimation()
                            binding.shimmerFrameLayout.visibility = View.GONE
                            binding.llImgSemPublicacao.visibility = View.GONE

                            rvFeed.visibility = View.VISIBLE

                            adapterPublicacoes.setData(response.body()!!)

                        }

                        override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                            Log.i("Cannot Get All publicacoes", t.stackTraceToString())
                        }

                    })
            }

            R.id.feed_menu_relevante -> {

                Rest.getInstance<PublicacaoService>().getAllpublicacoes()
                    .enqueue(object : Callback<MutableList<Publicacao>> {

                        override fun onResponse(
                            call: Call<MutableList<Publicacao>>,
                            response: Response<MutableList<Publicacao>>
                        ) {

                            binding.feedSelectedRigth.visibility = View.INVISIBLE
                            binding.feedSelectedLeft.visibility = View.VISIBLE

                            binding.shimmerFrameLayout.visibility = View.VISIBLE
                            binding.shimmerFrameLayout.startShimmerAnimation()

                            binding.rvFeed.visibility = View.GONE


                            adapterPublicacoes.setData(response.body()!!)

                            binding.shimmerFrameLayout.stopShimmerAnimation()
                            binding.shimmerFrameLayout.visibility = View.GONE
                            binding.llImgSemPublicacao.visibility = View.GONE

                            rvFeed.visibility = View.VISIBLE

                        }

                        override fun onFailure(call: Call<MutableList<Publicacao>>, t: Throwable) {
                            Log.i("Cannot Get All publicacoes", t.stackTraceToString())
                        }

                    })
            }

        }
        return true
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

    private fun salvar(idPublicacao: Int){

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