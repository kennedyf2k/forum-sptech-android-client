package br.com.studenton.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
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
    ): View {
        binding = FragmentDialogExcluirSalvoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPublicacao = arguments?.getInt("id")
        val usuario = arguments?.getInt("usuario")

        binding.btnCancelar.setOnClickListener {
            dismiss()
        }

        binding.btnApagar.setOnClickListener {

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

                    dismiss()

                    carregarSalvosFragment(idUsuario)


                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.i("Erro ao excluir", t.stackTraceToString())
                }
            }
        )
    }

    private fun carregarSalvosFragment(idUsuario: Int){

        val fragmentManager = activity?.supportFragmentManager

        val transaction = fragmentManager!!.beginTransaction()

        val salvoFragment = SalvosFragment()

        salvoFragment.arguments = bundleOf("id" to idUsuario)

        transaction.replace(R.id.fragments_container, salvoFragment)

        transaction.commit()
    }

}