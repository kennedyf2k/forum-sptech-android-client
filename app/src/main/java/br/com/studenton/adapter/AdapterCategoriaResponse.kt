package br.com.studenton.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.tracker.CategoriaDetails
import br.com.studenton.domain.Categoria

class AdapterCategoriaResponse(

    private val context: Context,
    private val categorias: MutableList<Categoria>

    ): RecyclerView.Adapter<AdapterCategoriaResponse.CategoriaViewHolder>() {

    lateinit var selectionTracker: SelectionTracker<Long>
    var contador = itemCount

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {

        val itemLista = LayoutInflater.from(context)
            .inflate(R.layout.fragment_feed_simple_item_categorias, parent, false)

        return CategoriaViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {

        holder.setCategoia( categorias[position], position )

    }

    override fun getItemCount(): Int = categorias.size

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private var button: Button
        var categoriaDetails = CategoriaDetails()

        init {

            button = itemView.findViewById<Button>(R.id.btn_categoria)

        }

        fun setCategoia( categoria: Categoria, position: Int){

            button.text = categoria.categoria

            categoriaDetails.categoria = categoria
            categoriaDetails.adapterPosition = position

//            if(categoria.idCategoria == contador){
//
//                button.setBackgroundColor( ContextCompat.getColor(itemView.context, R.color.feed_button_categoria_selected) )
//                itemView.isActivated = true
//
//            }
            if(selectionTracker.isSelected( categoriaDetails.selectionKey )){

                button.setBackgroundColor( ContextCompat.getColor(itemView.context, R.color.feed_button_categoria_selected) )
                itemView.isActivated = true

            }else{

                button.setBackgroundColor( ContextCompat.getColor(itemView.context, R.color.feed_button_categoria_not_selected) )
                itemView.isActivated = false

            }

        }

    }
}