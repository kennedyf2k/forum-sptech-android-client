package br.com.studenton.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable.ConstantState
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.domain.Publicacao
import br.com.studenton.repository.Rest
import br.com.studenton.services.CurtirService
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AdapterPublicacaoResponse(

    private val context: Context,
    val publicacoes: MutableList<Publicacao>,
    val idUsuario: Int,
    val onclickCurtir: (idPublicacao: Int) -> Unit,
    val onclickFavorito: (idPublicacao: Int) -> Unit

    ) : RecyclerView.Adapter<AdapterPublicacaoResponse.PublicacaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacaoViewHolder {

        val itemList = LayoutInflater.from(context)
            .inflate(R.layout.fragment_feed_simple_item_feed, parent, false)

        val holder = PublicacaoViewHolder(itemList)

        return holder
    }

    override fun onBindViewHolder(holder: PublicacaoViewHolder, position: Int) {

        var contadorCurtidas = publicacoes[position].countCurtidas

        Glide.with(context).load(publicacoes[position].fotoUsuario).into(holder.img_profile)
        holder.name_position_1.setText(publicacoes[position].nomeUsuario)

        when (publicacoes[position].diasAtras) {

            0 -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_0)
            1 -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_1)
            2 -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_2)
            3 -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_3)
            4 -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_4)
            5 -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_5)
            6 -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_6)
            7 -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_7)
            else -> holder.horas_atras.setText(R.string.feed_item_simple_item_dias_atras_else)

        }

        when (publicacoes[position].tipoPublicacao) {

            1 -> {

                holder.texto_fixo.setText(R.string.feed_item_simple_item_meio_publicou)
                holder.tipo_post.setText(R.string.feed_item_simple_item_tipo_publicacao_1)
                holder.tipo_post.setBackgroundResource(R.drawable.feed_item_shape_informacao_rosa)
                holder.categoria_post.setTextColor(R.color.feed_item_feed_name_categoria_rosa.toInt())
                holder.categoria_post.setBackgroundResource(R.drawable.feed_item_shape_categoria_rosa)
                holder.name_position_2.text = ""

            }
            else -> {

                holder.texto_fixo.setText(R.string.feed_item_simple_item_meio_respondeu)
                holder.tipo_post.setText(R.string.feed_item_simple_item_tipo_publicacao_2)
                holder.tipo_post.setBackgroundResource(R.drawable.feed_item_shape_duvida_laranja)
                holder.categoria_post.setTextColor(R.color.feed_item_feed_name_categoria_laranja.toInt())
                holder.categoria_post.setBackgroundResource(R.drawable.feed_item_shape_categoria_laranja)
                holder.name_position_2.setText(publicacoes[position].respostasByIdPublicacao[0].nomeUsuario)

            }

        }

        holder.categoria_post.setText(publicacoes[position].categoria.uppercase())
        holder.titulo_box.setText(publicacoes[position].titulo)
        holder.texto_box.setText(publicacoes[position].texto)
        holder.numero_curtidas.setText(contadorCurtidas.toString())
        holder.numero_comentarios.setText(publicacoes[position].respostasByIdPublicacao.size.toString())

        var foiCurtido = publicacoes[position].usuariosCurtidas.contains(idUsuario)
        var foiSalvo = publicacoes[position].usuariosSalvos.contains(idUsuario)

            if (foiCurtido) {

                holder.img_curtir.setImageResource(R.drawable.feed_item_img_curtir_marcado)

            } else {

                holder.img_curtir.setImageResource(R.drawable.feed_item_img_curtir_desmarcado)

            }

            holder.img_curtir.setOnClickListener {

                onclickCurtir.invoke(publicacoes[position].idPublicacao)

                if(foiCurtido) {

                    foiCurtido = false
                    holder.img_curtir.setImageResource(R.drawable.feed_item_img_curtir_desmarcado)
                    contadorCurtidas--
                    holder.numero_curtidas.setText(contadorCurtidas.toString())

                }else{

                    foiCurtido = true
                    holder.img_curtir.setImageResource(R.drawable.feed_item_img_curtir_marcado)
                    contadorCurtidas++
                    holder.numero_curtidas.setText(contadorCurtidas.toString())
                }
            }

            if(foiSalvo){

                holder.img_salvar.setImageResource(R.drawable.feed_item_img_salvar_marcado)

            }else{

                holder.img_salvar.setImageResource(R.drawable.feed_item_img_salvar)

            }

        holder.img_salvar.setOnClickListener {

            onclickFavorito.invoke(publicacoes[position].idPublicacao)

            if(foiSalvo){

                foiSalvo = false
                holder.img_salvar.setImageResource(R.drawable.feed_item_img_salvar)

            }else{

                foiSalvo = true
                holder.img_salvar.setImageResource(R.drawable.feed_item_img_salvar_marcado)

            }

        }

    }

    override fun getItemCount(): Int = publicacoes.size

    inner class PublicacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val img_profile = itemView.findViewById<ImageView>(R.id.iv_profile_item)
        val name_position_1 = itemView.findViewById<TextView>(R.id.tv_name_position_1)
        val name_position_2 = itemView.findViewById<TextView>(R.id.tv_name_position_2)
        val horas_atras = itemView.findViewById<TextView>(R.id.tv_horas_atras)
        val tipo_post = itemView.findViewById<TextView>(R.id.tv_tipo_post)
        val categoria_post = itemView.findViewById<TextView>(R.id.tv_categoria_post)
        val titulo_box = itemView.findViewById<TextView>(R.id.tv_titulo_box)
        val texto_box = itemView.findViewById<TextView>(R.id.tv_texto_box)
        val numero_curtidas = itemView.findViewById<TextView>(R.id.tv_numero_curtidas)
        val numero_comentarios = itemView.findViewById<TextView>(R.id.tv_numero_comentarios)
        val texto_fixo = itemView.findViewById<TextView>(R.id.tv_position_fixed)

        val img_curtir = itemView.findViewById<ImageView>(R.id.iv_curtir)
        val img_salvar = itemView.findViewById<ImageView>(R.id.iv_salvar)

    }

}