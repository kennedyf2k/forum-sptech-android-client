package br.com.studenton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.studenton.databinding.ActivityTermsBinding

class TermsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityTermsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {

            IrMainActivity()

        }

    }

    private fun IrMainActivity(){

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }
}