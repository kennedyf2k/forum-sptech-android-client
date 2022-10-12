package br.com.studenton

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import br.com.studenton.fragments.FeedFragment
import br.com.studenton.fragments.PerfilFragment
import br.com.studenton.fragments.PerguntasFragment
import br.com.studenton.fragments.SalvosFragment
import br.com.studenton.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;

    private lateinit var feedFragment: FeedFragment;
    private lateinit var perfilFragment: PerfilFragment;
    private lateinit var perguntasFragment: PerguntasFragment;
    private lateinit var salvosFragment: SalvosFragment;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val preferences = getSharedPreferences(
            "DADOS_CLIENTE",
            AppCompatActivity.MODE_PRIVATE
        )

        val nome = preferences.getString("nome", null)
        val ra = preferences.getString("ra", null)
        val curso = preferences.getString("curso", null)
        val semestre = preferences.getInt("semestre", 0)
        val email = preferences.getString("email", null)
        val urlFoto = preferences.getString("fotoPerfil", null)

        validarDados(nome)

        feedFragment = FeedFragment();
        perfilFragment = PerfilFragment(nome!!, ra!!, curso!!, semestre, email!!, urlFoto!!);
        perguntasFragment = PerguntasFragment();
        salvosFragment = SalvosFragment();


        setFragment(feedFragment)

        binding.bottomNav.setOnItemSelectedListener {

            when(it.itemId){

                R.id.menu_feed -> {

                    setFragment(feedFragment);

                }

                R.id.menu_perguntas -> {

                    setFragment(perguntasFragment);

                }

                R.id.menu_salvos -> {

                    setFragment(salvosFragment);

                }

                R.id.menu_perfil -> {

                    setFragment(perfilFragment);

                }

            }
            true
        }

    }

    private fun setFragment(fragment: Fragment){

        val fragmentTransaction = supportFragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.frame_fragments, fragment)

        fragmentTransaction.commit();
    }

    private fun validarDados(nome: String?){

        val intent = Intent(this, LoginActivity::class.java)


        if(nome.isNullOrEmpty()){

            startActivity(intent)

        }

    }

}