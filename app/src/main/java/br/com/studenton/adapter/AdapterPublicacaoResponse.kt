package br.com.studenton.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.models.response.PublicacaoResponse
import com.bumptech.glide.Glide

class AdapterPublicacaoResponse(private val context: Context, private val publicacoes: MutableList<PublicacaoResponse>): RecyclerView.Adapter<AdapterPublicacaoResponse.PublicacaoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PublicacaoViewHolder {

        val itemList = LayoutInflater.from(context).inflate(R.layout.fragment_feed_simple_item_feed, parent,false)

        val holder = PublicacaoViewHolder(itemList)

        return holder
    }

    override fun onBindViewHolder(holder: PublicacaoViewHolder, position: Int) {

        Glide.with(context).load(publicacoes[position].fotoUsuario).into(holder.img_profile);
        holder.name_position_1.setText(publicacoes[position].nomeUsuario)
        holder.name_position_2.setText(publicacoes[position].nomeUsuario)

        when(publicacoes[position].diasAtras){

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

        when(publicacoes[position].tipoPublicacao){

            1 -> holder.tipo_post.setText(R.string.feed_item_simple_item_tipo_publicacao_1)
            else -> holder.tipo_post.setText(R.string.feed_item_simple_item_tipo_publicacao_2)

        }

        holder.categoria_post.setText(publicacoes[position].categoria.uppercase())
        holder.titulo_box.setText(publicacoes[position].titulo)
        holder.texto_box.setText(publicacoes[position].texto)
        holder.numero_curtidas.setText(publicacoes[position].countCurtidas.toString())
        holder.numero_comentarios.setText(publicacoes[position].respostasByIdPublicacao.size.toString())

    }

    override fun getItemCount(): Int = publicacoes.size

    inner class PublicacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

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

    }

}