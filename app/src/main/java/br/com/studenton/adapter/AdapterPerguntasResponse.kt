package br.com.studenton.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.domain.Publicacao

class AdapterPerguntasResponse(
    private val acesso: Int,
    private val idUsuario: Int,
    var onclick: (idPublicacao: Int, status: Int, acesso: Int) -> Unit
) : RecyclerView.Adapter<AdapterPerguntasResponse.PerguntaHolder>(){

    private var perguntas : MutableList<Publicacao> = mutableListOf()

    fun setarDado(list: MutableList<Publicacao>){
        perguntas.clear()
        perguntas.addAll(list)
        notifyDataSetChanged()
    }

    inner class PerguntaHolder(
        private val itemView: View ) : RecyclerView.ViewHolder(itemView) {

        val titulo = itemView.findViewById<TextView>(R.id.txt_titulo_pergunta)
        val descricao = itemView.findViewById<TextView>(R.id.txt_desc_pergunta)
        val date_pg = itemView.findViewById<TextView>(R.id.txt_date_pergunta)
        val status_color = itemView.findViewById<TextView>(R.id.status)
        val card_pergunta = itemView.findViewById<CardView>(R.id.cvPergunta)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerguntaHolder {



        val layoutDoCard = LayoutInflater.from(parent.context).inflate(R.layout.fragment_pergunta_simple_item, parent, false)
        return PerguntaHolder(layoutDoCard)
    }

    override fun onBindViewHolder(holder: PerguntaHolder, position: Int) {

        var Dataa = perguntas[position].dataHora
        holder.titulo.text = perguntas[position].titulo



        var cor = perguntas[position].status
        println("status" + cor)



        when (perguntas[position].status) {


            1 ->
                holder.status_color.setBackgroundResource(R.color.login_esqueceu_senha);

            2 ->
                holder.status_color.setBackgroundResource(R.color.feed_item_feed_name_categoria_laranja);
            3 ->
                holder.status_color.setBackgroundResource(R.color.teal_200);
            4 ->
                holder.status_color.setBackgroundResource(R.color.vermelho);

            5 ->
                holder.status_color.setBackgroundResource(R.color.vermelho);

            6 ->
                holder.status_color.setBackgroundResource(R.color.vermelho);

        }

        when (perguntas[position].status) {


            1 ->holder.descricao.text = "Aguardando resposta"

            2 ->holder.descricao.text = "Aguardando análise"

            3 ->holder.descricao.text = perguntas[position].texto

            4 -> holder.descricao.text = "Recusada, aguardando remoção da resposta para receber uma nova resposta"

            5 -> holder.descricao.text = "Recusada, aguardando remoção da resposta para receber uma nova resposta"

            6 -> holder.descricao.text = "Recusada, aguardando remoção da resposta para receber uma nova resposta"

        }

        var formatDataa = Dataa

        var dia = formatDataa.subSequence(8,10)
        var mes = formatDataa.subSequence(5,7)
        var hora = formatDataa.subSequence(11,16)
        var diaMes = formatDataa.get(6).toString()


        if( diaMes == "0"){
            dia = formatDataa.subSequence(9,10)
        }else {
            dia = formatDataa.subSequence(8,10)
        }

        if(mes == "01"){
            mes = "Jan"
        }else if(mes == "02"){
            mes = "Fev"
        }else if(mes == "03"){
            mes = "Mar"
        }else if(mes == "04"){
            mes = "Abr"
        }else if(mes == "05"){
            mes = "Mai"
        }else if(mes == "06"){
            mes = "Jun"
        }else if(mes == "07"){
            mes = "Jul"
        }else if(mes == "08"){
            mes = "Ago"
        }else if(mes == "09"){
            mes = "Set"
        }else if(mes == "10"){
            mes = "Out"
        }else if(mes == "11"){
            mes = "Nov"
        }else if(mes == "12"){
            mes = "Dez"
        }


        var semanas = perguntas[position].diasAtras / 7

        if (perguntas[position].diasAtras <= 1){
            hora = formatDataa.subSequence(11,16).toString()
            holder.date_pg.text = hora
        }else if (perguntas[position].diasAtras <= 7){
            holder.date_pg.text = "${dia} ${mes}"
        }else if (perguntas[position].diasAtras > 7 &&  semanas < 5){
            if(semanas == 1){
                holder.date_pg.text = "${semanas} semana"
            }else {
                holder.date_pg.text = "${semanas} semanas"
            }
        }else if(semanas >= 5 ){
            holder.date_pg.text = "${dia} ${mes}"
        }

        holder.card_pergunta.setOnClickListener {
            onclick.invoke(perguntas[position].idPublicacao, perguntas[position].status, acesso)
        }

    }

    override fun getItemCount(): Int {
        return perguntas.size
    }



}