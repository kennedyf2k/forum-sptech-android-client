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
    ): View {
        binding = FragmentDialogExcluirPostagemBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPublicacao = arguments?.getInt("idPublicacao")

        binding.btnCancelar.setOnClickListener {
            dismiss()
        }

        binding.btnApagar.setOnClickListener {

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

                    Log.i("Teste excluir publi", response.toString())
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    binding.tvMsgErroTroca.setText(R.string.alerta_excluir_pergunta_erro)
                    Log.i("Erro da exclusão da publi: ", t.stackTraceToString())
                }
            })

    }

}