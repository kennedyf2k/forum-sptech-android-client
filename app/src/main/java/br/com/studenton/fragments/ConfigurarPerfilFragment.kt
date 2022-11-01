package br.com.studenton.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import br.com.studenton.R
import br.com.studenton.databinding.FragmentConfigurarPerfilBinding
import com.bumptech.glide.Glide

class ConfigurarPerfilFragment : Fragment() {

    private lateinit var binding: FragmentConfigurarPerfilBinding
    private lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentConfigurarPerfilBinding.inflate(inflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setarDados()

        binding.btnVoltar.setOnClickListener {

            val fragmentManager = activity?.supportFragmentManager

            val transaction = fragmentManager!!.beginTransaction()

            var fragmentPerfil = PerfilFragment()

            fragmentPerfil.arguments = bundle

            transaction.replace(R.id.fragments_container, fragmentPerfil)

            transaction.commit()

        }

        binding.tvLabelAlterar.setOnClickListener {

            val fragmentManager = activity?.supportFragmentManager

            val transaction = fragmentManager!!.beginTransaction()

            var fragmentAlterarSenha = PerfilAlterarSenhaFragment()

            fragmentAlterarSenha.arguments = bundle

            transaction.replace(R.id.fragments_container, fragmentAlterarSenha)

            transaction.commit()


        }
    }

    private fun setarDados(){

        binding.tvName.setText(arguments?.getString("nome"))
        Glide.with(this).load(arguments?.getString("urlFoto")).into(binding.ivProfile);

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