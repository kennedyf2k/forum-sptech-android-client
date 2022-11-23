package br.com.studenton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.studenton.databinding.ActivityEsqueciMinhaSenhaBinding
import br.com.studenton.domain.Login
import br.com.studenton.domain.request.TrocarSenhaRequest
import br.com.studenton.repository.Rest
import br.com.studenton.services.UsuarioService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EsqueciMinhaSenhaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEsqueciMinhaSenhaBinding
    private lateinit var ra: String
    private lateinit var email: String
    private lateinit var senhaNova: String
    private lateinit var senhaNovaConf: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_esqueci_minha_senha)

        binding.btnEsqueceuSenhaAlterar.setOnClickListener {
            if (verificarCampos(binding.etEsqueceuSenhaNovaSenha.text.toString(),
                    binding.etEsqueceuSenhaConfirmarNovaSenha.text.toString())
            ){
                ra = binding.etEsqueceuSenhaRa.text.toString()
                email = binding.etEsqueceuSenhaEmail.text.toString()
                senhaNova = binding.etEsqueceuSenhaNovaSenha.text.toString()
                senhaNovaConf = binding.etEsqueceuSenhaConfirmarNovaSenha.text.toString()

                trazerUsuarios()
            }
        }
    }



    private fun verificarUsuario(list: List<Login>){

        for (usuarios in list){
            if (usuarios.ra.equals(ra) && usuarios.email.equals(email) ){
//                alterarSenha(usuarios.idUsuario, TrocarSenhaRequest())
            }
        }
    }

    private fun alterarSenha(idUsuario : Int, trocarSenha: TrocarSenhaRequest){
//        Rest.getInstance<UsuarioService>().trocarSenha( )
    }

    private fun trazerUsuarios(){
        Rest.getInstance<UsuarioService>().getAllUsuarios()
            .enqueue(object : Callback<MutableList<Login>> {
                override fun onResponse(
                    call: Call<MutableList<Login>>,
                    response: Response<MutableList<Login>>
                ) {
                    verificarUsuario(response.body()!!)
                }

                override fun onFailure(call: Call<MutableList<Login>>, t: Throwable) {
                    Log.i("Cannot Get All publicacoes", t.stackTraceToString())
                }
            })
    }

    private fun verificarCampos(senha1: String, senha2: String): Boolean{

        if (senha1.equals(senha2)){
            return true
        }

        return false

    }


}