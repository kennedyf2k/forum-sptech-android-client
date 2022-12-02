package br.com.studenton.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.com.studenton.R
import br.com.studenton.databinding.FragmentCriarPerguntaBinding
import br.com.studenton.domain.request.PublicacaoRequest
import br.com.studenton.repository.Rest
import br.com.studenton.services.PerguntasService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CriarPerguntaFragment : Fragment() {

    private lateinit var binding: FragmentCriarPerguntaBinding
    private lateinit var bundle: Bundle
    private var idUssuario = -1
    private var acesso = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCriarPerguntaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setarDados()

        binding.ivArrowBack.setOnClickListener {

            val fragmentManager = activity?.supportFragmentManager

            val transaction = fragmentManager!!.beginTransaction()

            val feedFragment = FeedFragment()

            feedFragment.arguments = bundle

            transaction.replace(R.id.fragments_container, feedFragment)
            transaction.commit()

        }

        binding.criarPergunta.setOnClickListener {
            var titulo = binding.etTituloPergunta.text.toString()
            var conteudo = binding.etConteudoPergunta.text.toString()
            val spinnerr = binding.idSpinner
            val spinner2= spinnerr.selectedItem.toString()
            val categoria: String = spinner2
            verificarCampos(titulo, conteudo, categoria)


        }
    }



    private fun verificarCampos(titulo: String, conteudo: String, categoria: String){
        var idCategoria: Int = 0
        if (categoria != "Selecione uma cetegoria"){
            when(categoria){
                "SPTrans" -> idCategoria = 1
                "MeuID" -> idCategoria = 2
                "Secretaria" -> idCategoria = 3
                "Aula</item>" -> idCategoria = 4
                "Estagio" -> idCategoria = 5
                "Empresas" -> idCategoria = 6
                "Relatos" -> idCategoria = 7
                "Biblioteca" -> idCategoria = 8
                "Formatura" -> idCategoria = 9
            }
            if (titulo.length < 5){
                binding.etTituloPergunta.error = getString(R.string.fragment_criar_publicacao_length_titulo)
            }else if (conteudo.length < 10){
                binding.etConteudoPergunta.error = getString(R.string.fragment_criar_publicacao_length_texto)
            } else{
                this.criarPergunta(titulo,idCategoria, conteudo)
            }
        }else{
            binding.tvMsgErro.setText(R.string.fragment_criar_publicacao_categoria)
        }


    }

    private fun criarPergunta(titulo: String, idCategoria: Int, conteudo: String){
        var body: PublicacaoRequest = PublicacaoRequest(titulo, conteudo, idCategoria, 0, idUssuario, 0)
        if (acesso == 1){
            body.tipoPublicacao = 2
            body.status = 1

        }else if (acesso == 2){
            body.tipoPublicacao = 1
            body.status = 2
        }


        Rest.getInstance<PerguntasService>().createPergunta(body).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                if (response.isSuccessful) {
                    Toast.makeText(context, R.string.fragment_criar_publicacao_sucesso,
                        Toast.LENGTH_SHORT).show()

                    backToFeed()
                }else if(response.code() == 500){

                    binding.tvMsgErro.setText(R.string.fragment_criar_publicacao_erro)
                }


            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun backToFeed(){
        val fragmentManager = activity?.supportFragmentManager

        val transaction = fragmentManager!!.beginTransaction()

        val voltarParaFeed = FeedFragment()

        voltarParaFeed.arguments = bundle

        transaction.replace(R.id.fragments_container, voltarParaFeed)

        transaction.commit()


    }



    private fun setarDados(){
        idUssuario = arguments?.getInt("id")!!
        acesso = arguments?.getInt("acesso")!!
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
}