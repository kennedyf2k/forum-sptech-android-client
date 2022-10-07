package br.com.studenton

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        feedFragment = FeedFragment();
        perfilFragment = PerfilFragment();
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

}