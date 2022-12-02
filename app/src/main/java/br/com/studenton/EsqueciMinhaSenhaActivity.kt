package br.com.studenton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import br.com.studenton.databinding.ActivityEsqueciMinhaSenhaBinding
import br.com.studenton.domain.Login
import br.com.studenton.domain.request.EsqueceuSenhaRequest
import br.com.studenton.domain.request.TrocarSenhaRequest
import br.com.studenton.repository.Rest
import br.com.studenton.services.PerguntasService
import br.com.studenton.services.UsuarioService
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EsqueciMinhaSenhaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEsqueciMinhaSenhaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEsqueciMinhaSenhaBinding.inflate(layoutInflater)

        setContentView(binding.root)



        binding.btnEsqueceuSenhaAlterar.setOnClickListener {

            var campoRa = binding.etEsqueceuSenhaRa
            var campoEmail = binding.etEsqueceuSenhaEmail
            var campoSenha = binding.etEsqueceuSenhaNovaSenha
            var campoSenhaVerificada = binding.etEsqueceuSenhaConfirmarNovaSenha

            val confirmacao = validarCampos(campoRa, campoEmail, campoSenha, campoSenhaVerificada)
            if (confirmacao == 1){
                alterarSenha(campoRa.text.toString(), campoEmail.text.toString(), campoSenha.text.toString())
            }
        }
    }




    private fun alterarSenha(ra: String, email: String, senhaNova: String){
        val body = EsqueceuSenhaRequest(ra, email, senhaNova)

        Rest.getInstance<UsuarioService>().esqueciSenha(body).enqueue(object: Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful()){
                    Toast.makeText(applicationContext, getString(R.string.esqueceu_senmha_alterada_com_sucesso),
                        Toast.LENGTH_SHORT).show()
                    alterarTela()
                } else if (response.code() == 404){
                    binding.tvEsqueceuSenhaMsgErro.text = getString(R.string.esqueceu_senha_nao_encontrado)
                }else if (response.code() == 500){
                    binding.tvEsqueceuSenhaMsgErro.text = getString(R.string.esqueceu_senha_erro_servidor)
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.i("Erro", "ERRO: ${t.message}" )
                binding.tvEsqueceuSenhaMsgErro.setText(R.string.esqueceu_senha_erro_servidor)
            }
        })
    }

    private fun alterarTela(){
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }



    private fun validarCampos(campoRa: EditText, campoEmail: EditText,
                              campoSenha: EditText, campoSenhaVerificada: EditText): Int{

        if(campoRa.text.toString().isEmpty()){
            campoRa.error = getString(R.string.login_erro_obrigatorio)
            return 0
        }else if (campoEmail.text.toString().isEmpty()){
            campoEmail.error = getString(R.string.login_erro_obrigatorio)
            return 0
        }else if (campoSenha.text.toString().isEmpty()){
            campoSenha.error = getString(R.string.login_erro_obrigatorio)
            return 0
        }else if (campoSenhaVerificada.text.toString().isEmpty()){
            campoSenhaVerificada.error = getString(R.string.login_erro_obrigatorio)
            return 0
        }else if(campoRa.text.toString().length <= 3){
            campoRa.error = getString(R.string.login_erro_tamanho_caracteres_ra)
            return 0
        } else if (campoSenha.text.toString().length <= 7 ||
                    campoSenhaVerificada.text.toString().length <= 7){
            campoSenhaVerificada.error = getString(R.string.esqueceu_senha_senha_erro_senha_menor_7)
            campoSenha.error = getString(R.string.esqueceu_senha_senha_erro_senha_menor_7)
            return 0
        } else if(campoSenha.text.toString() != campoSenhaVerificada.text.toString()){
            binding.tvEsqueceuSenhaMsgErro.setText(R.string.esqueceu_senha_senha_erro_senhas_nao_batem)
            return 0
        }
        return 1
    }


}