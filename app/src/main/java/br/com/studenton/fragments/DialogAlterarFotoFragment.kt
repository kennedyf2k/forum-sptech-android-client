package br.com.studenton.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import br.com.studenton.R
import br.com.studenton.databinding.FragmentDialogAlterarFotoBinding
import br.com.studenton.repository.Rest
import br.com.studenton.services.UsuarioService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DialogAlterarFotoFragment : DialogFragment() {

    private lateinit var binding: FragmentDialogAlterarFotoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDialogAlterarFotoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancelar.setOnClickListener {
            dismiss()
        }

        binding.btnAlterar.setOnClickListener {

            val id = arguments?.getInt("id")
            val trocarPerfilRequest = binding.etAlterarFoto.text.toString()

            Log.i("TrocarFotoPerfilRequestID", id.toString())
            Log.i("TrocarFotoPerfilRequest", trocarPerfilRequest)

            trocarFotoPerfil(id!!, trocarPerfilRequest)

        }
    }

    private fun trocarFotoPerfil(id: Int, urlFoto: String){

        Rest.getInstance<UsuarioService>().trocarFotoPerfil(id, urlFoto).enqueue(
            object: Callback<Unit>{
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                    if(response.isSuccessful){

                        val alerta = AlertDialog.Builder(context!!);

                        alerta.setTitle(R.string.alterar_foto_alerta_title)

                        alerta.setMessage(R.string.alterar_senha_alerta_text)

                        alerta.create().show()

                    } else {

                        binding.tvMsgErroTroca.setText((response.errorBody().toString()))
                        Log.i("Ocorreu outro erro", response.errorBody().toString())
                    }

                    Log.i("Teste foto perfil", response.toString())
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    binding.tvMsgErroTroca.setText("Aconteceu um erro inesperado, tente novamente mais tarde")
                    Log.i("Erro da troca de imagem: ", t.stackTraceToString())
                }
            })
    }



}