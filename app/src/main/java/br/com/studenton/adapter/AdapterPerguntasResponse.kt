package br.com.studenton.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.domain.Publicacao

class AdapterPerguntasResponse(
    private val perguntas: List<Publicacao>,
    private val onclick: (mensagem: String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        inner class PerguntaHolder(
            private val itemView: View
        ) :
            RecyclerView.ViewHolder(itemView) {
            fun vincular (pergunta: Publicacao){

                //   val tvTitulo = itemView.findViewById<TextView>(R.id.txt_titulo_pergunta)
                //    val tvDescricao = itemView.findViewById<TextView>(R.id.txt_desc_pergunta)
                //     val tvData = itemView.findViewById<TextView>(R.id.txt_date_pergunta)
                     val cvPergunta = itemView.findViewById<CardView>(R.id.cvSalvo)

                //     tvTitulo.text = pergunta.titulo_pergunta
                //     tvDescricao.text = pergunta.descricao_pergunta
                //     tvData.text = pergunta.date_pergunta

                cvPergunta.setOnClickListener{
                    onclick("Você clicou no card X")
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val layoutDoCard = LayoutInflater.from(parent.context).inflate(R.layout.fragment_pergunta_simple_item, parent, false)
            return PerguntaHolder(layoutDoCard)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            (holder as PerguntaHolder).vincular(perguntas[position])
        }

        override fun getItemCount(): Int {
            return perguntas.size
        }
}