package br.com.studenton.fragments

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.os.bundleOf
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.AdapterCategoriaResponse
import br.com.studenton.adapter.AdapterPublicacaoResponse
import br.com.studenton.adapter.AdapterRespostaResponse
import br.com.studenton.adapter.tracker.CategoriaKeyProvider
import br.com.studenton.adapter.tracker.CategoriaLockup
import br.com.studenton.adapter.tracker.CategoriaPredicate
import br.com.studenton.databinding.FragmentFeedBinding

import br.com.studenton.domain.Categoria
import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.Resposta
import br.com.studenton.domain.request.RespostaRequest
import br.com.studenton.repository.Rest
import br.com.studenton.services.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment : Fragment() {

    private lateinit var binding: FragmentFeedBinding
    private lateinit var selectionTracker: SelectionTracker<Long>
    private lateinit var bottomNav: String

    private lateinit var rvFeed: RecyclerView
    private lateinit var rvCategorias: RecyclerView
    private lateinit var rvRespostas: RecyclerView

    private lateinit var categorias: MutableList<Categoria>
    private lateinit var publicacoes: MutableList<Publicacao>

    private lateinit var bundle: Bundle

    private var idUsuario = -1
    private var acesso = -1

    private lateinit var adapterPublicacoes: AdapterPublicacaoResponse
    private lateinit var adapterRespostaResponse: AdapterRespostaResponse
    private lateinit var adapterFiltros: AdapterCategoriaResponse

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

        bottomNav = "relevante"

        adapterPublicacoes = AdapterPublicacaoResponse(activity?.baseContext!!, idUsuario, acesso,{
                id -> curtir(id)
        },{
                id -> salvar(id)
        },{
                lista, tipo, id -> showBottomSheet(lista, tipo, id)
        })

        rvFeed.adapter = adapterPublicacoes

        setarDados()

        binding.fbPerguntar.setOnClickListener{

            val fragmentManager = activity?.supportFragmentManager

            val transaction = fragmentManager!!.beginTransaction()

            val criarPerguntaFragment = CriarPerguntaFragment()

            criarPerguntaFragment.arguments = bundle

            transaction.replace(R.id.fragments_container, criarPerguntaFragment)
            transaction.commit()

        }

        if(acesso == 1){
            binding.bottomNav.visibility = View.VISIBLE
            binding.bottomNavVet.visibility = View.INVISIBLE
        }else{
            binding.bottomNav.visibility = View.GONE
            binding.bottomNavVet.visibility = View.VISIBLE
        }

        binding.bottomNav.setOnItemSelectedListener { setarFeedByBottomNavigation(it.itemId) }

        Rest.getInstance<CategoriaService>().categorias()
            .enqueue(object : Callback<MutableList<Categoria>> {

                override fun onResponse(
                    call: Call<MutableList<Categoria>>,
                    response: Response<MutableList<Categoria>>,
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
        savedInstanceState: Bundle?,

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

            if(bottomNav == "relevante"){

                setarFeedByBottomNavigation(R.id.feed_menu_relevante)

            }else{

                setarFeedByBottomNavigation(R.id.feed_menu_colaboracoes)

            }


        } else {

            val lista = mutableListOf<Publicacao>()

            for (pubDaVez in publicacoes){

                if(pubDaVez.fkCategoria == idCategoria){
                    lista.add(pubDaVez)
                }

            }

            if(lista.isEmpty()){

                binding.shimmerFrameLayout.stopShimmerAnimation()
                binding.shimmerFrameLayout.visibility = View.GONE

                rvFeed.visibility = View.GONE
                binding.llImgSemPublicacao.visibility = View.VISIBLE

            }else {

                adapterPublicacoes.setData(lista)

                binding.shimmerFrameLayout.stopShimmerAnimation()
                binding.shimmerFrameLayout.visibility = View.GONE
                binding.llImgSemPublicacao.visibility = View.GONE

                if(bottomNav == "relevante"){

                    binding.feedSelectedRigth.visibility = View.INVISIBLE
                    binding.feedSelectedLeft.visibility = View.VISIBLE

                }else{

                    binding.feedSelectedRigth.visibility = View.VISIBLE
                    binding.feedSelectedLeft.visibility = View.INVISIBLE

                }

                rvFeed.visibility = View.VISIBLE

            }
        }
    }

    private fun setarFeedByBottomNavigation(itemId: Int): Boolean{

        binding.shimmerFrameLayout.startShimmerAnimation()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.llImgSemPublicacao.visibility = View.GONE
        binding.rvFeed.visibility = View.GONE

        when(itemId){

            R.id.feed_menu_colaboracoes -> {

                bottomNav = "colab"

                binding.feedSelectedRigth.visibility = View.VISIBLE
                binding.feedSelectedLeft.visibility = View.INVISIBLE
                adapterFiltros.setCategoriaTodas()

                Rest.getInstance<PublicacaoService>().getMinhasColaboracoes(idUsuario)
                    .enqueue(object : Callback<MutableList<Publicacao>> {

                        override fun onResponse(
                            call: Call<MutableList<Publicacao>>,
                            response: Response<MutableList<Publicacao>>,
                        ) {

                            publicacoes = response.body()!!

                            adapterPublicacoes.setData(publicacoes)

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

            R.id.feed_menu_relevante -> {

                bottomNav = "relevante"

                binding.feedSelectedRigth.visibility = View.INVISIBLE
                binding.feedSelectedLeft.visibility = View.VISIBLE
                adapterFiltros.setCategoriaTodas()

                Rest.getInstance<PublicacaoService>().getAllpublicacoes()
                    .enqueue(object : Callback<MutableList<Publicacao>> {

                        override fun onResponse(
                            call: Call<MutableList<Publicacao>>,
                            response: Response<MutableList<Publicacao>>,
                        ) {
                            val lista = mutableListOf<Publicacao>()

                            for (perguntaDaVez in  response.body()!!){
                                if (perguntaDaVez.status == 3){
                                    lista.add(perguntaDaVez)
                                }
                            }

                            publicacoes = lista

                            adapterPublicacoes.setData(publicacoes)

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

    private fun curtir(idPublicacao: Int){

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

    private fun setarDados(){

        bundle = bundleOf(

            "id" to arguments?.getInt("id"),
            "nome" to arguments?.getString("nome"),
            "ra" to arguments?.getString("ra"),
            "curso" to arguments?.getString("curso"),
            "semestre" to arguments?.getInt("semestre"),
            "email" to arguments?.getString("email"),
            "urlFoto" to arguments?.getString("urlFoto"),
            "acesso" to arguments?.getInt("acesso")
        )

    }

    @SuppressLint("InflateParams")
    private fun showBottomSheet(comentarios: MutableList<Resposta>?, tipoPublicacao: Int, idPublicacao: Int){


        val bottomSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme).apply {
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
        val bottomSheetView = LayoutInflater.from(requireActivity()).inflate(R.layout.comentarios_bottomsheet, null)

        bottomSheetView.findViewById<ImageView>(R.id.iv_arrow_back).setOnClickListener {

            bottomSheet.dismiss()

        }

        val nomeUsuarioResposta = bottomSheetView.findViewById<TextView>(R.id.tv_nome_usuario_resposta)
        val etTextoComentario = bottomSheetView.findViewById<TextView>(R.id.et_texto_comentario)

        bottomSheetView.findViewById<TextView>(R.id.btn_comentar).setOnClickListener {

            val body = RespostaRequest( etTextoComentario.text.toString(), idUsuario)

            Rest.getInstanceByAws<RespostaService>().postarResposta(idPublicacao, body)
                .enqueue(object : Callback<Resposta> {

                    @SuppressLint("NotifyDataSetChanged")
                    override fun onResponse(call: Call<Resposta>, response: Response<Resposta>) {

                        comentarios!!.add(response.body()!!)

                        adapterRespostaResponse.setData(comentarios)
                        adapterPublicacoes.notifyDataSetChanged()

                        etTextoComentario.text = ""

                    }

                    override fun onFailure(call: Call<Resposta>, t: Throwable) {
                        Log.i("ErroRespostaService: ", t.stackTraceToString())
                    }

                })

        }



        when(tipoPublicacao){

            1 -> {

                bottomSheetView.findViewById<TextView>(R.id.tv_title_comentarios).text = getString(R.string.comentarios_bottomsheet_title_comentarios)
                etTextoComentario.visibility = View.VISIBLE
                bottomSheetView.findViewById<TextView>(R.id.btn_comentar).visibility = View.VISIBLE
                nomeUsuarioResposta.visibility = View.GONE

            }
            else -> {

                bottomSheetView.findViewById<TextView>(R.id.tv_title_comentarios).text = getString(R.string.comentarios_bottomsheet_title_resposta)
                etTextoComentario.visibility = View.GONE
                bottomSheetView.findViewById<TextView>(R.id.btn_comentar).visibility = View.GONE


                nomeUsuarioResposta.visibility = View.VISIBLE
                nomeUsuarioResposta.text = comentarios!![0].nomeUsuario

            }

        }

        rvRespostas = bottomSheetView.findViewById(R.id.rv_comentarios)
        rvRespostas.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        rvRespostas.setHasFixedSize(true)

        adapterRespostaResponse =  AdapterRespostaResponse(activity?.baseContext!!)

        rvRespostas.adapter = adapterRespostaResponse

        adapterRespostaResponse.setData(comentarios!!)

        bottomSheet.setContentView(bottomSheetView)

        val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        bottomSheet.findViewById<CoordinatorLayout>(R.id.bottom_sheet_layout)!!.minimumHeight =
            Resources.getSystem().displayMetrics.heightPixels

        bottomSheet.show()

    }
}