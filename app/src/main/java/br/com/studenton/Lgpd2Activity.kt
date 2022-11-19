package br.com.studenton

import android.content.Intent
import android.os.Bundle
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import br.com.studenton.databinding.ActivityLgpd1Binding
import br.com.studenton.databinding.ActivityLgpd2Binding


class Lgpd2Activity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLgpd2Binding;

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityLgpd2Binding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.btnAvancar.setOnClickListener(this)

    }

    override fun onClick(view: View) {
        if(view.id == R.id.btn_avancar){
            val intent = Intent(this, Lgpd3Activity::class.java)
            startActivity(intent)
        }
    }


}


