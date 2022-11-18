package br.com.studenton.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.domain.Salvo

class AdapterSalvoResponse(
    private val salvos: List<Salvo>,
    private val onclick: (mensagem: String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        inner class SalvoHolder(
            private val itemView: View
        ) :
            RecyclerView.ViewHolder(itemView) {
            fun vincular (salvo: Salvo){

                val tvTitulo = itemView.findViewById<TextView>(R.id.txt_titulo_salvo)
                val tvDescricao = itemView.findViewById<TextView>(R.id.txt_desc_salvo)
                val tvData = itemView.findViewById<TextView>(R.id.txt_date)
                val cvSalvo = itemView.findViewById<CardView>(R.id.cvSalvo)

                tvTitulo.text = salvo.titulo_publicacao
                tvDescricao.text = salvo.descricao_publicacao
                tvData.text = salvo.date

                cvSalvo.setOnClickListener{
                    onclick("Você clicou no card X")
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layoutDoCard = LayoutInflater.from(parent.context).inflate(R.layout.fragment_salvo_simple_item, parent, false)
            return SalvoHolder(layoutDoCard)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as SalvoHolder).vincular(salvos[position])
        }

        override fun getItemCount(): Int {
            return salvos.size
        }
}