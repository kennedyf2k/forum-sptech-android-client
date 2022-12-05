package br.com.studenton

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isInvisible
import br.com.studenton.R
import br.com.studenton.databinding.FragmentVisualizarPerguntaBinding
import br.com.studenton.domain.Publicacao
import br.com.studenton.fragments.DialogExcluirPostagem
import br.com.studenton.repository.Rest
import br.com.studenton.services.PerguntasService
import br.com.studenton.services.PublicacaoService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VisualizarPerguntaFragment : Fragment() {

    private lateinit var binding: FragmentVisualizarPerguntaBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVisualizarPerguntaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val idPergunta = arguments?.getInt("id")
        val status = arguments?.getInt("status")
        val acesso = arguments?.getInt("acesso")

        binding.btnExcluir.setOnClickListener {

            var fragmentDialog = DialogExcluirPostagem()

            val fragmentManager = activity?.supportFragmentManager

            fragmentDialog.arguments = bundleOf()

            fragmentDialog.show(fragmentManager!!, "")

        }


        Rest.getInstance<PublicacaoService>().getPublicacao(idPergunta!!)
            .enqueue(object: Callback<Publicacao>{
                override fun onResponse(call: Call<Publicacao>, response: Response<Publicacao>) {

                    when (response.body()!!.tipoPublicacao) {

                        //Postagem - resposta
                        1 -> {

                            when (status){

                                2 -> {
                                    // Postagem em análise
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
                                    binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                    binding.tvNamePosition2.text = ""
                                    binding.tvStatus.setText(R.string.status_analise_pergunta)
                                    binding.linearConteudoResposta.visibility = View.GONE
                                    binding.tvTituloResposta.visibility = View.GONE

                                    binding.btnEditar.setOnClickListener {
                                        carregarPublicacaoEditar(response.body()!!.idPublicacao, response.body()!!.titulo,
                                            response.body()!!.texto, response.body()!!.fkCategoria )
                                    }

                                }

                                3 -> {
                                    // Postagem aprovada
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
                                    binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                    binding.tvNamePosition2.text = ""
                                    binding.linearBotoesEditarExcluir.visibility = View.GONE
                                    binding.tvNomeResposta.text = response.body()!!.respostasByIdPublicacao[0].nomeUsuario
                                    binding.tvRespostaPergunta.text = response.body()!!.respostasByIdPublicacao[0].texto
                                    binding.tvStatus.visibility = View.GONE
                                }

                                else -> {
                                    // Postagem recusada
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
                                    binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                    binding.tvNamePosition2.text = ""
                                    binding.tvStatus.setText(R.string.status_pergunta_recusado)
                                    binding.tvStatus.setTextColor(
                                        ContextCompat.getColor(activity!!.baseContext,
                                            R.color.vermelho))
                                    binding.btnEditar.visibility = View.GONE
                                    binding.tvTituloResposta.visibility = View.GONE
                                    binding.linearConteudoResposta.visibility = View.GONE
                                }

                            }


                        }

                        // Dúvida
                        2 -> {

                            when (acesso){

                                1 -> when (status) {

                                    1 -> {
                                        binding.tvNamePosition2.text = ""
                                        binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                        binding.tvPositionFixed.setText(R.string.feed_item_simple_item_publicou_duvida)
                                        binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                                        binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                                        binding.tvCategoriaPost.setTextColor(
                                            ContextCompat.getColor(activity!!.baseContext,
                                                R.color.feed_item_feed_name_categoria_laranja))
                                        binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                                        binding.tvTituloResposta.visibility = View.GONE
                                        binding.linearConteudoResposta.visibility = View.GONE

                                        binding.btnEditar.setOnClickListener {
                                            carregarPublicacaoEditar(response.body()!!.idPublicacao, response.body()!!.titulo,
                                                response.body()!!.texto, response.body()!!.fkCategoria )
                                        }
                                    }

                                    2 -> {
                                        binding.tvNamePosition2.text = ""
                                        binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                        binding.tvPositionFixed.setText(R.string.feed_item_simple_item_publicou_duvida)
                                        binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                                        binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                                        binding.tvCategoriaPost.setTextColor(
                                            ContextCompat.getColor(activity!!.baseContext,
                                                R.color.feed_item_feed_name_categoria_laranja))
                                        binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                                        binding.tvStatus.setText(R.string.status_analise_pergunta)
                                        binding.linearBotoesEditarExcluir.visibility = View.GONE
                                        binding.linearConteudoResposta.visibility = View.GONE
                                        binding.tvTituloResposta.visibility = View.GONE

                                        binding.btnEditar.setOnClickListener {
                                            carregarPublicacaoEditar(response.body()!!.idPublicacao, response.body()!!.titulo,
                                                response.body()!!.texto, response.body()!!.fkCategoria )
                                        }
                                    }

                                    3 ->{
                                        binding.tvNamePosition2.text = ""
                                        binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                        binding.tvPositionFixed.setText(R.string.feed_item_simple_item_publicou_duvida)
                                        binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                                        binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                                        binding.tvCategoriaPost.setTextColor(
                                            ContextCompat.getColor(activity!!.baseContext,
                                                R.color.feed_item_feed_name_categoria_laranja))
                                        binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                                        binding.linearBotoesEditarExcluir.visibility = View.GONE
                                        binding.tvNomeResposta.text =
                                            response.body()!!.respostasByIdPublicacao[0].nomeUsuario
                                        binding.tvRespostaPergunta.text =
                                            response.body()!!.respostasByIdPublicacao[0].texto
                                        binding.tvStatus.visibility = View.GONE
                                    }

                                    else -> {
                                        binding.tvNamePosition2.text = ""
                                        binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                        binding.tvPositionFixed.setText(R.string.feed_item_simple_item_publicou_duvida)
                                        binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                                        binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                                        binding.tvCategoriaPost.setTextColor(
                                            ContextCompat.getColor(activity!!.baseContext,
                                                R.color.feed_item_feed_name_categoria_laranja))
                                        binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                                        binding.tvStatus.setText(R.string.status_pergunta_recusado)
                                        binding.tvStatus.setTextColor(
                                            ContextCompat.getColor(activity!!.baseContext,
                                                R.color.vermelho))
                                        binding.btnEditar.visibility = View.GONE
                                        binding.tvTituloResposta.visibility = View.GONE
                                        binding.linearConteudoResposta.visibility = View.GONE
                                    }
                                }

                                2 -> {

                                    when(status) {

                                        2 -> {
                                            // Resposta em análise
                                            binding.tvNamePosition2.text = ""
                                            binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                            binding.tvPositionFixed.setText(R.string.feed_item_simple_item_publicou_duvida)
                                            binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                                            binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                                            binding.tvCategoriaPost.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_item_feed_name_categoria_laranja))
                                            binding.tvTituloBox.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_button_categoria_not_selected_text))
                                            binding.tvTextoBox.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_button_categoria_not_selected_text))
                                            binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                                            binding.tvStatus.setText(R.string.status_analise_pergunta)
                                            binding.tvTituloResposta.setText(R.string.text_sua_resposta)
                                            binding.tvNomeResposta.text =
                                                response.body()!!.respostasByIdPublicacao[0].nomeUsuario
                                            binding.tvRespostaPergunta.text =
                                                response.body()!!.respostasByIdPublicacao[0].texto

                                            binding.btnEditar.setOnClickListener {
                                                carregarPublicacaoEditar(response.body()!!.idPublicacao, response.body()!!.titulo,
                                                    response.body()!!.texto, response.body()!!.fkCategoria )
                                            }
                                        }

                                        3 -> {
                                            // Resposta aprovada
                                            binding.tvNamePosition2.text = ""
                                            binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                            binding.tvPositionFixed.setText(R.string.feed_item_simple_item_publicou_duvida)
                                            binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                                            binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                                            binding.tvCategoriaPost.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_item_feed_name_categoria_laranja))
                                            binding.tvTituloBox.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_button_categoria_not_selected_text))
                                            binding.tvTextoBox.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_button_categoria_not_selected_text))
                                            binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                                            binding.tvTituloResposta.setText(R.string.text_sua_resposta)
                                            binding.tvNomeResposta.text =
                                                response.body()!!.respostasByIdPublicacao[0].nomeUsuario
                                            binding.tvRespostaPergunta.text =
                                                response.body()!!.respostasByIdPublicacao[0].texto
                                            Glide.with(activity!!.baseContext).load(response.body()!!.respostasByIdPublicacao[0].fotoUsuario).into(binding.ivProfileItemResposta)

                                        }

                                        else -> {
                                            // Resposta recusada
                                            binding.tvNamePosition2.text = ""
                                            binding.tvNamePosition1.text = response.body()!!.nomeUsuario
                                            binding.tvPositionFixed.setText(R.string.feed_item_simple_item_publicou_duvida)
                                            binding.tvTipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                                            binding.tvTipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                                            binding.tvCategoriaPost.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_item_feed_name_categoria_laranja))
                                            binding.tvTituloBox.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_button_categoria_not_selected_text))
                                            binding.tvTextoBox.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.feed_button_categoria_not_selected_text))
                                            binding.tvCategoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                                            binding.tvStatus.setText(R.string.status_pergunta_recusado)
                                            binding.tvStatus.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.vermelho))
                                            binding.btnEditar.visibility = View.GONE
                                            binding.tvTituloResposta.setText(R.string.text_sua_resposta)
                                            binding.tvNomeResposta.text =
                                                response.body()!!.respostasByIdPublicacao[0].nomeUsuario
                                            binding.tvRespostaPergunta.text =
                                                response.body()!!.respostasByIdPublicacao[0].texto
                                        }

                                    }

                                }
                            }

                        }
                    }

                    if(acesso == 2){
                        //Carolina Costa, validar quando deve aparecer a segunda foto para não encerrar o app quando abrir a publi na apresentação
                        Glide.with(activity!!.baseContext).load(response.body()!!.respostasByIdPublicacao[0].fotoUsuario).into(binding.ivProfileItemResposta)
                    }

                    Glide.with(activity!!.baseContext).load(response.body()!!.fotoUsuario).into(binding.ivProfileItem)
                    binding.tvHorasAtras.text = "Há ${response.body()!!.diasAtras.toString()} dias"
                    binding.tvCategoriaPost.text = response.body()!!.categoria
                    binding.tvTituloBox.text = response.body()!!.titulo
                    binding.tvTextoBox.text = response.body()!!.texto
                }

                override fun onFailure(call: Call<Publicacao>, t: Throwable) {
                    TODO("Not yet implemented")
                }


            })
    }

    private fun carregarPublicacaoEditar(idPublicacao: Int, titulo: String, texto: String, categoria: Int){

        val fragmentManager = activity?.supportFragmentManager

        val transaction = fragmentManager!!.beginTransaction()

        val editarPergunta = EditarPerguntaFragment()

        editarPergunta.arguments = bundleOf("id" to idPublicacao, "titulo" to titulo, "texto" to texto, "categoria" to categoria)

        transaction.replace(R.id.fragments_container, editarPergunta)

        transaction.commit()
    }

}