package br.com.studenton

import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import br.com.studenton.databinding.ActivityTermsBinding
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.*
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import br.com.studenton.models.response.LoginResponse


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

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }
}


