package br.com.studenton.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.studenton.databinding.FragmentPerfilBinding
import com.bumptech.glide.Glide
import java.net.URL


class PerfilFragment(

    val nome: String,
    val ra: String,
    val curso: String,
    val semestre: Int,
    val email: String,
    val urlImagem: String

) : Fragment() {

    private lateinit var binding: FragmentPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)

        setarDados()

        return binding.root
    }

    private fun setarDados(){

        val semestreTexto = "$semestre° Semestre"

        binding.tvNome.setText(nome)
        binding.tvRa.setText(ra)
        binding.tvCurso.setText(curso)
        binding.tvSemestre.setText(semestreTexto)
        binding.tvEmail.setText(email)
        Glide.with(this).load(urlImagem).into(binding.ivProfile);

    }

}