package br.com.studenton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash);
        abrirImagem()
        changeToLogin()

    }

    fun changeToLogin(){
        val intent = Intent(this, MainActivity::class.java)
        Handler().postDelayed({
            intent.change()
        }, 1800)
    }
    fun Intent.change(){
        startActivity(this)
        finish()
    }

    fun abrirImagem(){
        Handler().postDelayed({
            val imageView: ImageView = findViewById(R.id.logoPrincipal)
            imageView.visibility = View.VISIBLE
            imageView.alpha = 0.5F
            imageView.alpha = 1.0F

        }, 800)
    }
}