package br.com.studenton

import android.content.Intent
import android.os.Bundle
import br.com.studenton.databinding.ActivityTermsBinding
import androidx.appcompat.app.AppCompatActivity


class TermsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityTermsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

            val preferences = getSharedPreferences(
                "DADOS_CLIENTE",
                MODE_PRIVATE
            )

            val editor = preferences.edit()

            editor.putBoolean("aceitouTermos", true)

            editor.apply()

            IrMainActivity()

        }

    }

    private fun IrMainActivity(){

        val intent = Intent(this, Lgpd1Activity::class.java)

        startActivity(intent)
        finish()
    }
}


