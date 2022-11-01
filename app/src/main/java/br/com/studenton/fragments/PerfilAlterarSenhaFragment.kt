package br.com.studenton.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import br.com.studenton.R
import br.com.studenton.databinding.FragmentPerfilAlterarSenhaBinding
import br.com.studenton.domain.request.TrocarSenhaRequest
import br.com.studenton.repository.Rest
import br.com.studenton.services.UsuarioService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PerfilAlterarSenhaFragment : Fragment() {

    private lateinit var binding: FragmentPerfilAlterarSenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPerfilAlterarSenhaBinding.inflate(inflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAlterar.setOnClickListener{

            if(novaSenhaEConfirmarSenhaIguais()){

                val id = arguments?.getInt("id")
                val trocarSenhaRequest = TrocarSenhaRequest(binding.etSenhaAtual.text.toString(),
                                                            binding.etNovaSenha.text.toString())

                Log.i("TrocarSenhaRequestID", id.toString())

                Log.i("TrocarSenhaRequest", trocarSenhaRequest.toString())

                trocarSenha(id!!, trocarSenhaRequest )

            }



        }


    }

    private fun novaSenhaEConfirmarSenhaIguais(): Boolean{

        val novaSenha = binding.etNovaSenha.text.toString()

        if(novaSenha != binding.etConfirmarNovaSenha.text.toString()){

            binding.tvMsgErro.setText(R.string.alterar_senha_erro_senhas_nao_batem)
            return false

        }else if(novaSenha.length < 9){

            binding.tvMsgErro.setText(R.string.alterar_senha_erro_senha_menor_9)
            return false

        }

        return true

    }

    private fun trocarTela(){

        val fragmentManager = activity?.supportFragmentManager

        val transaction = fragmentManager!!.beginTransaction()

        val bundle = bundleOf(

            "id" to arguments?.getInt("id"),
            "nome" to arguments?.getString("nome"),
            "ra" to arguments?.getString("ra"),
            "curso" to arguments?.getString("curso"),
            "semestre" to arguments?.getInt("semestre"),
            "email" to arguments?.getString("email"),
            "urlFoto" to arguments?.getString("urlFoto"))

        val perfilFragment = PerfilFragment()

        perfilFragment.arguments = bundle

        transaction.replace(R.id.fragments_container, perfilFragment)

        transaction.commit()

    }

    private fun trocarSenha(id: Int, body: TrocarSenhaRequest){

        Rest.getInstance<UsuarioService>().trocarSenha(id, body).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if(response.isSuccessful){

                    val alerta = AlertDialog.Builder(context!!);

                    alerta.setTitle(R.string.alterar_senha_alerta_title)

                    alerta.setMessage(R.string.alterar_senha_alerta_text)

                    alerta.create().show();

                    trocarTela()

                }else if(response.code() == 404){

                    binding.tvMsgErro.setText(R.string.alterar_senha_erro_usuario_nao_encontrado)
                    Log.i("Usuário não encontrado", response.errorBody().toString())

                }else{

                    binding.tvMsgErro.setText(response.errorBody().toString())
                    Log.i("Ocorreu outro erro", response.errorBody().toString())

                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {

                binding.tvMsgErro.setText(R.string.login_erro_server)

                Log.i("Erro: ", t.stackTraceToString())
            }


        })

    }

}