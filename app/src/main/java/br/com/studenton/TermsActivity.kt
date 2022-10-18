package br.com.studenton

import android.content.Intent
import android.os.Bundle
import br.com.studenton.databinding.ActivityTermsBinding
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.viewpager2.widget.ViewPager2
import br.com.studenton.models.response.LoginResponse
import br.com.studenton.util_extesion.setVisible

class TermsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsBinding;


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityTermsBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val slideViewPager = binding.fragmentOnboardingSlideViewpager

        val items = listOf(

            IntroSlide(R.drawable.login_logos,
            "Bem vindo ao Fórum SPTECH por Studen On!",
            "Estimulando a formação colaborativa entre os alunos."),
            IntroSlide(R.drawable.elementosintro2,
            "Calouro",
            "as perguntas realizadas pelo calouro são envidas para serem respondidas e só assim, são postadas no feed"),
            IntroSlide(R.drawable.elementosintro3,
            "Veterano",
            "As respostas são enviadas pelo veterano por possuírem maturidade experiência combase na pergunta realizada."),
            IntroSlide(R.drawable.elementos_intro1,
            "Ecossistema",
            "A comunidade inteira temacesso a publicação e o conhecimento é multiplicado."),
            IntroSlide(R.drawable.elementosintro4,
            "Seja responsável!",
            "Nada de respostas incompletas e que possam ser invasivas hein?!")

        )

        val introSlideAdapter = IntroSlideAdapter(items)

        slideViewPager.adapter = introSlideAdapter

        val indicators = findViewById<LinearLayout>(R.id.fragment_onboading_slide_indicator)
        // CRIAR QUANTIDADE CORRETA DE INDICADORES
        setupIndicators(indicators, introSlideAdapter.itemCount)
        // COLOCAR SELEçÃO NO PRIMEIRO ITEM
        setCurrentIndicator(indicators, 0)
        // PARA SABER A HORA DE EXIBIR O BOTÃO FINAL NO ONBOARDING SCREEN

        slideViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(indicators, position)
                if (introSlideAdapter.itemCount - 1 == position) {
                    binding.btnLogin.setVisible(true)
                }
            }
        })

        binding.btnLogin.setOnClickListener {

            IrMainActivity()

        }

    }

    private fun IrMainActivity(){

        val intent = Intent(this, MainActivity::class.java)

        startActivity(intent)
    }

    private fun setupIndicators(indicatorContainer: LinearLayout, indicatorCount: Int) {

        // CRIAR LISTA DE INDICADORES
        val indicators = arrayOfNulls<ImageView>(indicatorCount)

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // DIMENSIONAR OS INDICADORES
        val indicatorSpace = resources.getDimensionPixelSize(R.dimen.slide_indicator_space)
        params.setMargins(indicatorSpace, 0, 0, 0)

        // ATRIBUIR BACKGROUND AOS INDICADORES
        for (i in indicators.indices) {
            indicators[i] = ImageView(this)

            indicators[i]?.apply {
                this.setImageDrawable(getDrawable(R.drawable.indicator_unselected))
                this.layoutParams = params
            }

            indicatorContainer.addView(indicators[i])

        }
    }

    // SELECIONAR O INDICADOR DE ACORDO COM A PAGINA ATUAL
    private fun setCurrentIndicator(indicatorContainer: LinearLayout, index: Int) {

        for (i in 0 until indicatorContainer.childCount) {

            val img = indicatorContainer[i] as? ImageView

            when (index == i) {
                true -> img?.setImageDrawable(getDrawable(R.drawable.indicator_selected))
                else -> img?.setImageDrawable(getDrawable(R.drawable.indicator_unselected))
            }

        }
    }

    private fun salvarDados(dados: LoginResponse){

        val preferences = getSharedPreferences(
            "DADOS_CLIENTE",
            MODE_PRIVATE
        )

        val editor = preferences.edit();

        editor.putBoolean("autenticado", true)

        editor.apply()

        IrMainActivity()
    }
}


