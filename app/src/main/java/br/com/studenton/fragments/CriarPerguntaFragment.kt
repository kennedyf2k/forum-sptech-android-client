package br.com.studenton.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import br.com.studenton.R
import br.com.studenton.databinding.FragmentCriarPerguntaBinding

class CriarPerguntaFragment : Fragment() {

    private lateinit var binding: FragmentCriarPerguntaBinding
    private lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCriarPerguntaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setarDados()

        binding.ivArrowBack.setOnClickListener {

            val fragmentManager = activity?.supportFragmentManager

            val transaction = fragmentManager!!.beginTransaction()

            val feedFragment = FeedFragment()

            feedFragment.arguments = bundle

            transaction.replace(R.id.fragments_container, feedFragment)
            transaction.commit()

        }
    }

    private fun setarDados(){

        bundle = bundleOf(

            "id" to arguments?.getInt("id"),
            "nome" to arguments?.getString("nome"),
            "ra" to arguments?.getString("ra"),
            "curso" to arguments?.getString("curso"),
            "semestre" to arguments?.getInt("semestre"),
            "email" to arguments?.getString("email"),
            "urlFoto" to arguments?.getString("urlFoto"))

    }
}