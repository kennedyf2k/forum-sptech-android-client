package br.com.studenton.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.domain.Publicacao
import br.com.studenton.domain.Salvo

class AdapterSalvoResponse(
    private val salvos: List<Publicacao>,
    var onclick: (idPublicacao: Int) -> Unit
    ) : RecyclerView.Adapter<AdapterSalvoResponse.SalvoHolder>(){

        inner class SalvoHolder(
            private val itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            fun vincular(publicacao: Publicacao) {

                val tvTitulo = itemView.findViewById<TextView>(R.id.txt_titulo_salvo)
                val tvDescricao = itemView.findViewById<TextView>(R.id.txt_desc_salvo)
                val tvData = itemView.findViewById<TextView>(R.id.txt_date)
                val cvSalvo = itemView.findViewById<CardView>(R.id.cvSalvo)

                var diasAtras: String = "Há ${publicacao.diasAtras.toString()} dias"

                tvTitulo.text = publicacao.titulo.substring(0, 23) + "..."
                tvDescricao.text = publicacao.texto
                tvData.text = diasAtras

                //Abrir segunda tela de Salvos onde está efetivamente o conteúdo da publicação que foi salva
                cvSalvo.setOnClickListener {
                    onclick.invoke(publicacao.idPublicacao)
                }
            }
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterSalvoResponse.SalvoHolder {
        val layoutDoCard = LayoutInflater.from(parent.context).inflate(R.layout.fragment_salvo_simple_item, parent, false)
        return SalvoHolder(layoutDoCard)
    }

    override fun onBindViewHolder(holder: AdapterSalvoResponse.SalvoHolder, position: Int) {
        holder.vincular(salvos[position])

    }

    override fun getItemCount(): Int {
        return salvos.size
    }
}