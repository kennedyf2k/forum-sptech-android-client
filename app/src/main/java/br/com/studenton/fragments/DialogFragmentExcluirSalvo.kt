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
import br.com.studenton.databinding.FragmentDialogExcluirSalvoBinding
import br.com.studenton.databinding.FragmentVisualizarSalvoBinding
import br.com.studenton.repository.Rest
import br.com.studenton.services.SalvarService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogFragmentExcluirSalvo : DialogFragment(){

    private lateinit var binding: FragmentDialogExcluirSalvoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogExcluirSalvoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPublicacao = arguments?.getInt("idPublicacao")
        val usuario = arguments?.getInt("id")

        binding.btnCancelar.setOnClickListener {
            dismiss()
        }

        binding.btnApagar.setOnClickListener {

            Log.i("Id da publicação", idPublicacao.toString())
            apagarSalvo(usuario!!, idPublicacao!!)
        }

    }

    private fun apagarSalvo(idUsuario: Int, idPublicacao: Int){

        Rest.getInstance<SalvarService>().deletarFavorito(idUsuario, idPublicacao).enqueue(
            object: Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    val alerta = AlertDialog.Builder(context!!);

                    alerta.setTitle(R.string.deletar_salvo_title)

                    alerta.setMessage(R.string.deletar_salvo_text)

                    alerta.create().show()

                    Log.i("Resposta:", response.toString())
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            }
        )

    }

}