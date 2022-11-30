package br.com.studenton.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import br.com.studenton.R
import br.com.studenton.databinding.FragmentSalvosBinding
import br.com.studenton.databinding.FragmentVisualizarSalvoBinding
import br.com.studenton.domain.Publicacao
import br.com.studenton.repository.Rest
import br.com.studenton.services.PublicacaoService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisualizarSalvoFragment : Fragment() {

    private lateinit var binding: FragmentVisualizarSalvoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVisualizarSalvoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val publicacao = arguments?.getInt("id")

        Rest.getInstance<PublicacaoService>().getPublicacao(publicacao!!)
            .enqueue(object : Callback<Publicacao>{

                override fun onResponse(call: Call<Publicacao>, response: Response<Publicacao>) {
                    Log.i("Publicacao salva", response.toString())

                    when (response.body()!!.tipoPublicacao) {

                        1 -> {

                            binding.tvPositionFixed.setText(R.string.feed_item_simple_item_meio_publicou)
                            binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_1)
                            binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_informacao_rosa)
                            binding.tvCategoriaPost.setTextColor(
                                ContextCompat.getColor(
                                    activity!!.baseContext,
                                    R.color.feed_item_feed_name_categoria_rosa
                                )
                            )
                            binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_rosa)
                            binding.tvNamePosition2.text = ""

                        }
                        else -> {


                            binding.tvPositionFixed.setText(R.string.feed_item_simple_item_meio_respondeu)
                            binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                            binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                            binding.tvCategoriaPost.setTextColor(ContextCompat.getColor(activity!!.baseContext,
                                R.color.feed_item_feed_name_categoria_laranja))
                            binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                            binding.tvNamePosition1.text =
                                response.body()!!.respostasByIdPublicacao[0].nomeUsuario

                        }
                    }
                        Glide.with(activity!!.baseContext).load(response.body()!!.fotoUsuario).into(binding.ivProfileItem)
            }

                override fun onFailure(call: Call<Publicacao>, t: Throwable) {
                    Log.i("Erro ao carregar a publicação salva!", t.stackTraceToString())
                }

            })

}
}