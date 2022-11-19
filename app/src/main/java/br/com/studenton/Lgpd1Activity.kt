package br.com.studenton

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import br.com.studenton.databinding.ActivityLgpd1Binding


class Lgpd1Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLgpd1Binding;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLgpd1Binding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnNewGame.setOnClickListener {

            startActivity(Intent(this, Lgpd1Activity::class.java))
            finish()

        }

    }


}


