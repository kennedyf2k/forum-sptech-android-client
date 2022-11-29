package br.com.studenton.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.domain.Resposta
import com.bumptech.glide.Glide

class AdapterRespostaResponse(

    private val context: Context

): RecyclerView.Adapter<AdapterRespostaResponse.RespostaViewHolder>(){

    private var respostas: MutableList<Resposta> = mutableListOf()

    fun setData(list: List<Resposta>) {
        respostas.clear()
        respostas.addAll(list)
        notifyDataSetChanged()
    }

    inner class RespostaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imgProfile = itemView.findViewById<ImageView>(R.id.iv_profile_item_resposta)!!
        var nameUsuario = itemView.findViewById<TextView>(R.id.tv_nome_usuario)!!

        var textoResposta = itemView.findViewById<TextView>(R.id.tv_texto_resposta)!!


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RespostaViewHolder {

        val itemList = LayoutInflater.from(context)
            .inflate(R.layout.comentarios_bottomsheet_simple_item, parent, false)

        return RespostaViewHolder(itemList)

    }

    override fun onBindViewHolder(holder: RespostaViewHolder, position: Int) {

        Glide.with(context).load(respostas[position].fotoUsuario).into(holder.imgProfile)

        holder.textoResposta.text = respostas[position].texto

        holder.nameUsuario.text = respostas[position].nomeUsuario

    }

    override fun getItemCount() = respostas.size

}