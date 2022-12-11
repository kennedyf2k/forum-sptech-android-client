package br.com.studenton.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import br.com.studenton.R
import br.com.studenton.databinding.FragmentEditarPerguntaBinding
import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.request.EditarPerguntaRequest
import br.com.studenton.repository.Rest
import br.com.studenton.services.PublicacaoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditarPerguntaFragment() : Fragment() {

    private lateinit var binding: FragmentEditarPerguntaBinding
    private lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditarPerguntaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPublicacao = arguments?.getInt("id")
        val titulo = arguments?.getString("titulo")
        val texto = arguments?.getString("texto")
        val fkCategoria = arguments?.getInt("categoria")

        val tituloEditado = binding.etEditarTitulo.text.toString()
        val textoEditado = binding.etEditarTexto.text.toString()

        binding.etEditarTitulo.setText(titulo)
        binding.etEditarTexto.setText(texto)

        binding.btnAtualizar.setOnClickListener {
            editarPostagem(idPublicacao!!, tituloEditado, textoEditado, fkCategoria!!)
        }

        binding.btnCancelar.setOnClickListener {
            backToQuestions()
        }

    }

    private fun editarPostagem(idPublicacao: Int, titulo: String, texto: String, categoria: Int){
        val body = EditarPerguntaRequest(idPublicacao, titulo, texto, categoria)
        Log.i("Body:", body.toString())

        Rest.getInstanceByAws<PublicacaoService>().putAtualizarDuvida(body).
        enqueue(object : Callback<Publicacao>{

            override fun onResponse(
                call: Call<Publicacao>,
                response: Response<Publicacao>
            ) {
                if (response.isSuccessful) {

                    Toast.makeText(context, R.string.fragment_criar_publicacao_sucesso,
                        Toast.LENGTH_SHORT).show()

                    backToQuestions()
                }else if(response.code() == 500){

                    binding.tvMsgErro.setText(R.string.fragment_criar_publicacao_erro)
                }

                Log.i("Resposta:", response.toString())
            }

            override fun onFailure(call: Call<Publicacao>, t: Throwable) {
                Log.i("Erro:", t.stackTraceToString())
            }
        })
    }

    private fun backToQuestions(){
        val fragmentManager = activity?.supportFragmentManager

        val transaction = fragmentManager!!.beginTransaction()

        val voltarParaPerguntas = PerguntasFragment()

        voltarParaPerguntas.arguments = bundleOf()

        transaction.replace(R.id.fragments_container, voltarParaPerguntas)

        transaction.commit()

    }

}