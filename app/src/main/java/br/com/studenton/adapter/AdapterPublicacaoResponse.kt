package br.com.studenton.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.domain.Publicacao
import com.bumptech.glide.Glide


class AdapterPublicacaoResponse(

    private val context: Context,
    private val idUsuario: Int,
    private val onclickCurtir: (idPublicacao: Int) -> Unit,
    private val onclickFavorito: (idPublicacao: Int) -> Unit

    ) : RecyclerView.Adapter<AdapterPublicacaoResponse.PublicacaoViewHolder>() {

    private var publicacoes: MutableList<Publicacao> = mutableListOf()

    fun setData(list: List<Publicacao>) {
        publicacoes.clear()
        publicacoes.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacaoViewHolder {

        val itemList = LayoutInflater.from(context)
            .inflate(R.layout.fragment_feed_simple_item_feed, parent, false)

        return PublicacaoViewHolder(itemList)
    }

    override fun onBindViewHolder(holder: PublicacaoViewHolder, position: Int) {

        var contadorCurtidas = publicacoes[position].countCurtidas

        holder.numeroCurtidas.text = contadorCurtidas.toString()

        Glide.with(context).load(publicacoes[position].fotoUsuario).into(holder.imgProfile)
        holder.namePosition1.text = publicacoes[position].nomeUsuario

        when (publicacoes[position].diasAtras) {

            0 -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_0)
            1 -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_1)
            2 -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_2)
            3 -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_3)
            4 -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_4)
            5 -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_5)
            6 -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_6)
            7 -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_7)
            else -> holder.horasAtras.setText(R.string.feed_item_simple_item_dias_atras_else)

        }

        when (publicacoes[position].tipoPublicacao) {

            1 -> {

                holder.textoFixo.setText(R.string.feed_item_simple_item_meio_publicou)
                holder.tipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_1)
                holder.tipoPost.setBackgroundResource(R.drawable.feed_item_shape_informacao_rosa)
                holder.categoriaPost.setTextColor(ContextCompat.getColor(context, R.color.feed_item_feed_name_categoria_rosa))
                holder.categoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_rosa)
                holder.namePosition2.text = ""

            }
            else -> {

                holder.textoFixo.setText(R.string.feed_item_simple_item_meio_respondeu)
                holder.tipoPost.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                holder.tipoPost.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                holder.categoriaPost.setTextColor(ContextCompat.getColor(context, R.color.feed_item_feed_name_categoria_laranja))
                holder.categoriaPost.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                holder.namePosition2.text = publicacoes[position].respostasByIdPublicacao[0].nomeUsuario

            }

        }

        holder.categoriaPost.text = publicacoes[position].categoria.uppercase()
        holder.tituloBox.text = publicacoes[position].titulo
        holder.textoBox.text = publicacoes[position].texto
        holder.numeroComentarios.text = publicacoes[position].respostasByIdPublicacao.size.toString()

        var foiCurtido = publicacoes[position].usuariosCurtidas.contains(idUsuario)
        var foiSalvo = publicacoes[position].usuariosSalvos.contains(idUsuario)

            if (foiCurtido) {

                holder.imgCurtir.setImageResource(R.drawable.feed_item_img_curtir_marcado)

            } else {

                holder.imgCurtir.setImageResource(R.drawable.feed_item_img_curtir_desmarcado)

            }

            holder.imgCurtir.setOnClickListener {

                onclickCurtir.invoke(publicacoes[position].idPublicacao)

                if(foiCurtido) {

                    foiCurtido = false
                    holder.imgCurtir.setImageResource(R.drawable.feed_item_img_curtir_desmarcado)
                    contadorCurtidas--
                    holder.numeroCurtidas.text = contadorCurtidas.toString()

                }else{

                    foiCurtido = true
                    holder.imgCurtir.setImageResource(R.drawable.feed_item_img_curtir_marcado)
                    contadorCurtidas++
                    holder.numeroCurtidas.text = contadorCurtidas.toString()

                }
            }



            if(foiSalvo){

                holder.imgSalvar.setImageResource(R.drawable.feed_item_img_salvar_marcado)

            }else{

                holder.imgSalvar.setImageResource(R.drawable.feed_item_img_salvar)

            }

        holder.imgSalvar.setOnClickListener {

            onclickFavorito.invoke(publicacoes[position].idPublicacao)

            if(foiSalvo){

                foiSalvo = false
                holder.imgSalvar.setImageResource(R.drawable.feed_item_img_salvar)

            }else{

                foiSalvo = true
                holder.imgSalvar.setImageResource(R.drawable.feed_item_img_salvar_marcado)

            }
        }
    }

    override fun getItemCount(): Int = publicacoes.size

    inner class PublicacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val imgProfile = itemView.findViewById<ImageView>(R.id.iv_profile_item)!!
        val namePosition1 = itemView.findViewById<TextView>(R.id.tv_name_position_1)!!
        val namePosition2 = itemView.findViewById<TextView>(R.id.tv_name_position_2)!!
        val horasAtras = itemView.findViewById<TextView>(R.id.tv_horas_atras)!!
        val tipoPost = itemView.findViewById<TextView>(R.id.tv_tipo_post)!!
        val categoriaPost = itemView.findViewById<TextView>(R.id.tv_categoria_post)!!
        val tituloBox = itemView.findViewById<TextView>(R.id.tv_titulo_box)!!
        val textoBox = itemView.findViewById<TextView>(R.id.tv_texto_box)!!
        val numeroCurtidas = itemView.findViewById<TextView>(R.id.tv_numero_curtidas)!!
        val numeroComentarios = itemView.findViewById<TextView>(R.id.tv_numero_comentarios)!!
        val textoFixo = itemView.findViewById<TextView>(R.id.tv_position_fixed)!!

        val imgCurtir = itemView.findViewById<ImageView>(R.id.iv_curtir)!!
        val imgSalvar = itemView.findViewById<ImageView>(R.id.iv_salvar)!!

    }

}