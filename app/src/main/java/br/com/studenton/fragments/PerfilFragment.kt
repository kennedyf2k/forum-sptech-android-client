package br.com.studenton.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import br.com.studenton.LoginActivity
import br.com.studenton.R
import br.com.studenton.databinding.FragmentPerfilBinding
import com.bumptech.glide.Glide

class PerfilFragment : Fragment() {

    private lateinit var binding: FragmentPerfilBinding
    private lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {

        binding = FragmentPerfilBinding.inflate(inflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.tvBtnSair.setOnClickListener {

            val intent = Intent(activity, LoginActivity::class.java)

            startActivity(intent)

        }

        setarDados()

        binding.tvEditar.setOnClickListener {

            val fragmentManager = activity?.supportFragmentManager

            val transaction = fragmentManager!!.beginTransaction()

            val fragmentConfigPerfil = ConfigurarPerfilFragment()

            fragmentConfigPerfil.arguments = bundle

            transaction.replace(R.id.fragments_container, fragmentConfigPerfil)

            transaction.commit()
        }


    }


    private fun setarDados(){

        val semestreTexto = "${arguments?.getInt("semestre")}° Semestre"

        binding.tvNome.text = arguments?.getString("nome")
        binding.tvRa.text = arguments?.getString("ra")
        binding.tvCurso.text = arguments?.getString("curso")
        binding.tvSemestre.text = semestreTexto
        binding.tvEmail.text = arguments?.getString("email")
        Glide.with(this).load(arguments?.getString("urlFoto")).into(binding.ivProfile)

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