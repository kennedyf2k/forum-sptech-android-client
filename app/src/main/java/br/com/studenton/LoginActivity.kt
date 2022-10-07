package br.com.studenton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.studenton.databinding.ActivityLoginBinding
import br.com.studenton.models.request.LoginRequest
import br.com.studenton.models.response.LoginResponse
import br.com.studenton.rest.Rest
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

            if(validarTextos(campoRa, campoSenha)){

                login(campoRa.text.toString(), campoSenha.text.toString())

            }else{

                campoRa.error = "Esse campo é obrigatório"
                campoSenha.error = "Esse campo é obrigatório"

            }
        }
    }

    private fun login(ra: String, senha: String){

        val body = LoginRequest(ra, senha);

        val request = Rest.getInstance().create(Login::class.java)
        val intent = Intent(this, MainActivity::class.java)

        request.login(body).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {

                if(response.code() == 401){

                    binding.tvMsgErro.setText("ra ou senha estão incorretos")

                }else{

                    startActivity(intent)

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.i("Erro", "ERRO: ${t.message}" )
            }


        })
    }

    private fun validarTextos(campo1: TextInputEditText, campo2: TextInputEditText): Boolean{

        if(campo1.text.toString().isEmpty() || campo2.text.toString().isEmpty()){

            return false

        }

        return true
    }

}