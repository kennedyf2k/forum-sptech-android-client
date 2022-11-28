package br.com.studenton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.com.studenton.fragments.FeedFragment
import br.com.studenton.fragments.PerfilFragment
import br.com.studenton.fragments.PerguntasFragment
import br.com.studenton.fragments.SalvosFragment
import br.com.studenton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var feedFragment: FeedFragment
    private lateinit var perfilFragment: PerfilFragment
    private lateinit var perguntasFragment: PerguntasFragment
    private lateinit var salvosFragment: SalvosFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val preferences = getSharedPreferences(
            "DADOS_CLIENTE",
            MODE_PRIVATE
        )

        val id = preferences.getInt("idUsuario", -1)
        val nome = preferences.getString("nome", null)
        val ra = preferences.getString("ra", null)
        val curso = preferences.getString("curso", null)
        val semestre = preferences.getInt("semestre", -1)
        val email = preferences.getString("email", null)
        val urlFoto = preferences.getString("fotoPerfil", null)
        var acesso = preferences.getInt("acesso", 0)

        validarDados(nome)

        val bundlePerfil = bundleOf(

            "id" to id,
            "nome" to nome,
            "ra" to ra,
            "curso" to curso,
            "semestre" to semestre,
            "email" to email,
            "urlFoto" to urlFoto,
            "acesso" to  acesso
        )


        feedFragment = FeedFragment()
        perfilFragment = PerfilFragment()
        perguntasFragment = PerguntasFragment()
        salvosFragment = SalvosFragment()

        binding.bottomNav.setOnItemSelectedListener {

            when(it.itemId){

                R.id.menu_feed -> {

                    feedFragment.arguments = bundlePerfil
                    setFragment(feedFragment)

                }

                R.id.menu_perguntas -> {
                    perguntasFragment.arguments = bundlePerfil
                    setFragment(perguntasFragment)

                }

                R.id.menu_salvos -> {
                    salvosFragment.arguments = bundlePerfil
                    setFragment(salvosFragment)

                }

                R.id.menu_perfil -> {

                    perfilFragment.arguments = bundlePerfil
                    setFragment(perfilFragment)

                }
            }
            true
        }

        feedFragment.arguments = bundlePerfil
        setFragment(feedFragment)
    }

    private fun setFragment(fragment: Fragment){

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.replace(binding.fragmentsContainer.id, fragment)

        fragmentTransaction.commit()
    }

    private fun validarDados(nome: String?){

        val intent = Intent(this, LoginActivity::class.java)

        if(nome.isNullOrEmpty()){

            startActivity(intent)

        }

    }

}