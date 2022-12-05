package br.com.studenton.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import br.com.studenton.R
import br.com.studenton.databinding.FragmentDialogExcluirPostagemBinding
import br.com.studenton.domain.Publicacao
import br.com.studenton.repository.Rest
import br.com.studenton.services.PublicacaoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogExcluirPostagem : DialogFragment() {

    private lateinit var binding: FragmentDialogExcluirPostagemBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogExcluirPostagemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnCancelar.setOnClickListener {
            dismiss()
        }

        binding.btnApagar.setOnClickListener {
            val idPublicacao = arguments?.getInt("id")
            Log.i("Id da publicação", idPublicacao.toString())
            deletarPergunta(idPublicacao!!)
        }

    }
    private fun deletarPergunta(idPublicacao: Int){

        Rest.getInstance<PublicacaoService>().deletarDuvida(idPublicacao)
            .enqueue(object : Callback<Boolean>{

                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if(response.isSuccessful){

                        val alerta = AlertDialog.Builder(context!!);

                        alerta.setTitle(R.string.alerta_excluir_pergunta_title)

                        alerta.setMessage(R.string.alerta_excluir_pergunta_text)

                        alerta.create().show()

                    } else {

                        binding.tvMsgErroTroca.setText((response.errorBody().toString()))
                        Log.i("Ocorreu outro erro", response.errorBody().toString())
                    }

                    Log.i("Teste foto perfil", response.toString())
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    binding.tvMsgErroTroca.setText("Aconteceu um erro inesperado, tente novamente mais tarde")
                    Log.i("Erro da troca de imagem: ", t.stackTraceToString())
                }
            })

    }

}