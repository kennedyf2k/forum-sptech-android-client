package br.com.studenton.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.models.response.CategoriaResponse

class AdapterCategoriaResponse( private val context: Context, private val categorias: MutableList<CategoriaResponse> ): RecyclerView.Adapter<AdapterCategoriaResponse.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {

        val itemLista = LayoutInflater.from(context).inflate(R.layout.fragment_feed_simple_item_categorias, parent, false)

        val holder = CategoriaViewHolder(itemLista);

        return holder;
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {

        holder.button.setText(categorias[position].categoria)

        holder.button.setOnClickListener {



        }

    }

    override fun getItemCount(): Int = categorias.size

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val button = itemView.findViewById<TextView>(R.id.btn_categoria)

    }

    fun desativarBotoes(tamanho: Int, holder: CategoriaViewHolder){

        for(i in 0..tamanho){



        }

    }

}