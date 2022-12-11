package br.com.studenton.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import br.com.studenton.R
import br.com.studenton.databinding.FragmentVisualizarPerguntaBinding
import br.com.studenton.domain.Publicacao
import br.com.studenton.repository.Rest
import br.com.studenton.services.PublicacaoService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VisualizarPerguntaFragment : Fragment() {

    private lateinit var binding: FragmentVisualizarPerguntaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVisualizarPerguntaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.shimmerFrameLayout.startShimmerAnimation()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.cvBoxItemPergunta.visibility = View.GONE

        val idPergunta = arguments?.getInt("id")
        val status = arguments?.getInt("status")
        val acesso = arguments?.getInt("acesso")

        binding.btnExcluir.setOnClickListener {

            val fragmentDialog = DialogExcluirPostagem()

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
                            binding.linearConteudoResposta.visibility = View.GONE
                            binding.tvTituloResposta.visibility = View.GONE
                            binding.btnEditar.visibility = View.GONE
                            binding.btnExcluir.visibility = View.GONE

                            when (status){

                                2 -> {
                                    // Postagem em análise
                                    binding.tvStatus.setText(R.string.status_analise_pergunta)
                                    binding.btnEditar.visibility = View.VISIBLE
                                    binding.btnExcluir.visibility = View.VISIBLE
                                    binding.btnEditar.setOnClickListener {
                                        carregarPublicacaoEditar(response.body()!!.idPublicacao, response.body()!!.titulo,
                                            response.body()!!.texto, response.body()!!.fkCategoria )
                                    }

                                }

                                3 -> {

                                    // Postagem aprovada
                                    binding.tvStatus.visibility = View.GONE
                                    binding.linearConteudoResposta.visibility = View.VISIBLE
                                    binding.tvTituloResposta.visibility = View.VISIBLE
                                    binding.tvNomeResposta.text = response.body()!!.respostasByIdPublicacao[0].nomeUsuario
                                    binding.tvRespostaPergunta.text = response.body()!!.respostasByIdPublicacao[0].texto

                                }

                                else -> {

                                    // Postagem recusada
                                    binding.tvStatus.setText(R.string.status_pergunta_recusado)
                                    binding.tvStatus.setTextColor(
                                        ContextCompat.getColor(activity!!.baseContext,
                                            R.color.vermelho))

                                }
                            }
                        }

                        // Dúvida
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

                            binding.btnEditar.visibility = View.GONE
                            binding.btnExcluir.visibility = View.GONE

                            binding.tvStatus.visibility = View.GONE

                            when (acesso){

                                1 -> when (status) {

                                    1 -> {

                                        binding.tvTituloResposta.visibility = View.GONE
                                        binding.linearConteudoResposta.visibility = View.GONE

                                        binding.btnEditar.visibility = View.VISIBLE
                                        binding.btnExcluir.visibility = View.VISIBLE

                                        binding.btnEditar.setOnClickListener {
                                            carregarPublicacaoEditar(response.body()!!.idPublicacao, response.body()!!.titulo,
                                                response.body()!!.texto, response.body()!!.fkCategoria )
                                        }
                                    }

                                    2 -> {

                                        binding.tvStatus.visibility = View.VISIBLE
                                        binding.tvStatus.setText(R.string.status_analise_pergunta)
                                        binding.linearBotoesEditarExcluir.visibility = View.GONE
                                        binding.linearConteudoResposta.visibility = View.GONE
                                        binding.tvTituloResposta.visibility = View.GONE

                                        binding.btnEditar.visibility = View.VISIBLE
                                        binding.btnExcluir.visibility = View.VISIBLE

                                        binding.btnEditar.setOnClickListener {
                                            carregarPublicacaoEditar(response.body()!!.idPublicacao, response.body()!!.titulo,
                                                response.body()!!.texto, response.body()!!.fkCategoria )
                                        }
                                    }

                                    3 ->{

                                        binding.tvTituloResposta.visibility = View.VISIBLE
                                        binding.linearBotoesEditarExcluir.visibility = View.VISIBLE
                                        binding.tvNomeResposta.text =
                                            response.body()!!.respostasByIdPublicacao[0].nomeUsuario
                                        binding.tvRespostaPergunta.text =
                                            response.body()!!.respostasByIdPublicacao[0].texto
                                        binding.tvStatus.visibility = View.GONE

                                    }

                                    else -> {

                                        binding.tvStatus.visibility = View.VISIBLE
                                        binding.tvStatus.setText(R.string.status_pergunta_recusado)
                                        binding.tvStatus.setTextColor(
                                            ContextCompat.getColor(activity!!.baseContext,
                                                R.color.vermelho))
                                        binding.tvTituloResposta.visibility = View.GONE
                                        binding.linearConteudoResposta.visibility = View.GONE

                                    }
                                }

                                2 -> {

                                    binding.tvTituloBox.setTextColor(
                                        ContextCompat.getColor(activity!!.baseContext,
                                            R.color.feed_button_categoria_not_selected_text))

                                    binding.tvTextoBox.setTextColor(
                                        ContextCompat.getColor(activity!!.baseContext,
                                            R.color.feed_button_categoria_not_selected_text))
                                    binding.tvTituloResposta.setText(R.string.text_sua_resposta)

                                    binding.linearConteudoResposta.visibility = View.VISIBLE
                                    binding.tvNomeResposta.text =
                                        response.body()!!.respostasByIdPublicacao[0].nomeUsuario
                                    binding.tvRespostaPergunta.text =
                                        response.body()!!.respostasByIdPublicacao[0].texto

                                    when(status) {

                                        2 -> {
                                            // Resposta em análise

                                            binding.tvStatus.visibility = View.VISIBLE
                                            binding.tvStatus.setText(R.string.status_analise_pergunta)

                                            binding.btnEditar.visibility = View.VISIBLE
                                            binding.btnExcluir.visibility = View.VISIBLE

                                            binding.btnEditar.setOnClickListener {
                                                carregarPublicacaoEditar(response.body()!!.idPublicacao, response.body()!!.titulo,
                                                    response.body()!!.texto, response.body()!!.fkCategoria )
                                            }
                                        }

                                        3 -> {
                                            // Resposta aprovada

                                            Glide.with(activity!!.baseContext).load(response.body()!!.respostasByIdPublicacao[0].fotoUsuario).into(binding.ivProfileItemResposta)

                                        }

                                        else -> {
                                            // Resposta recusada

                                            binding.tvStatus.visibility = View.VISIBLE
                                            binding.tvStatus.setText(R.string.status_pergunta_recusado)

                                            binding.tvStatus.setTextColor(
                                                ContextCompat.getColor(activity!!.baseContext,
                                                    R.color.vermelho))

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

                    val textoDiasAtras = getString(R.string.dias_atras_position1) + " " +
                            response.body()!!.diasAtras.toString() + " " +
                            getString(R.string.dias_atras_position2)
                    binding.tvHorasAtras.text = textoDiasAtras

                    binding.tvCategoriaPost.text = response.body()!!.categoria
                    binding.tvTituloBox.text = response.body()!!.titulo
                    binding.tvTextoBox.text = response.body()!!.texto

                    binding.shimmerFrameLayout.stopShimmerAnimation()
                    binding.shimmerFrameLayout.visibility = View.GONE
                    binding.cvBoxItemPergunta.visibility = View.VISIBLE
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

        editarPergunta.arguments = bundleOf(

            "id" to idPublicacao,
            "titulo" to titulo,
            "texto" to texto,
            "categoria" to categoria

        )

        transaction.replace(R.id.fragments_container, editarPergunta)

        transaction.commit()
    }

}