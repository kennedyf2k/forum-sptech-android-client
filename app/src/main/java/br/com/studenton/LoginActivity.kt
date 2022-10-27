package br.com.studenton

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.studenton.databinding.ActivityLoginBinding
import br.com.studenton.adapter.models.request.LoginRequest
import br.com.studenton.adapter.models.response.LoginResponse
import br.com.studenton.repository.Rest
import br.com.studenton.services.Login
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var campoRa = binding.tiInputRa;
        var campoSenha = binding.tiInputSenha;

        binding.btnAcessar.setOnClickListener {

            val nResponse = validarCampos(campoRa, campoSenha)

            if(nResponse == 0){

                login(campoRa.text.toString(), campoSenha.text.toString())

            }else if(nResponse == 1){

                campoRa.error = getString(R.string.login_erro_obrigatorio)
                campoSenha.error = getString(R.string.login_erro_obrigatorio)

            }else{

                campoRa.error = getString(R.string.login_erro_tamanho_caracteres_ra)
                campoSenha.error =  getString(R.string.login_erro_tamanho_caracteres_senha)

            }

        }
    }

    private fun login(ra: String, senha: String){

        val body = LoginRequest(ra, senha);

        Rest.getInstance<Login>().login(body).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if(response.code() == 200){

                    salvarDados(response.body()!!)

                }else{

                    binding.tvMsgErro.setText(R.string.login_erro_login)

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

                Log.i("Erro", "ERRO: ${t.message}" )

                binding.tvMsgErro.setText(R.string.login_erro_server)

            }


        })

    }

    private fun validarCampos(campoRa: TextInputEditText, campoSenha: TextInputEditText): Int{

        if(campoRa.text.toString().isEmpty() || campoSenha.text.toString().isEmpty()){

            return 1

        }else if(campoRa.text.toString().length <= 3 || campoSenha.text.toString().length <= 5){

            return 2

        }

        return 0
    }

    private fun salvarDados(dados: LoginResponse){

        val preferences = getSharedPreferences(
            "DADOS_CLIENTE",
            AppCompatActivity.MODE_PRIVATE
        )

        var editor = preferences.edit();

        editor.putInt("idUsuario", dados.idUsuario);
        editor.putString("ra", dados.ra);
        editor.putString("nome", dados.nome);
        editor.putString("email", dados.email);
        editor.putString("curso", dados.curso);
        editor.putInt("semestre", dados.semestre);
        editor.putString("fotoPerfil", dados.fotoPerfil);
        editor.putInt("fkAcesso", dados.fkAcesso);
        editor.putBoolean("checkEmail", dados.checkEmail);
        editor.putBoolean("autenticado", dados.autenticado);

        editor.apply()

        if(preferences.getBoolean("aceitouTermos", false)){

            irActivity(MainActivity())

        }else{

            irActivity(TermsActivity())

        }

    }

    private fun irActivity(activity: Activity){

        val intent = Intent(this, activity::class.java)

        startActivity(intent)
    }
}