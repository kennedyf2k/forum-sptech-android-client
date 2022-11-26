package br.com.studenton.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import br.com.studenton.LoginActivity
import br.com.studenton.databinding.FragmentDialogSairBinding

class DialogSairFragment : DialogFragment(){

    private lateinit var binding: FragmentDialogSairBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogSairBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        binding.btnSair.setOnClickListener {

            val intent = Intent(activity, LoginActivity::class.java)

            startActivity(intent)
            activity?.finish()

        }

        binding.btnCancelar.setOnClickListener{

            dismiss()

        }


    }

}