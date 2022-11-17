package br.com.studenton.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.R
import br.com.studenton.adapter.tracker.CategoriaDetails
import br.com.studenton.domain.Categoria

class AdapterCategoriaResponse(

    private val context: Context,
    private val categorias: MutableList<Categoria>,
    val onclick: (idPublicacao: Int) -> Unit

    ): RecyclerView.Adapter<AdapterCategoriaResponse.CategoriaViewHolder>() {

    lateinit var selectionTracker: SelectionTracker<Long>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {

        val itemLista = LayoutInflater.from(context)
            .inflate(R.layout.fragment_feed_simple_item_categorias, parent, false)

        return CategoriaViewHolder(itemLista)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {

        holder.setCategoia( categorias[position], position )

    }

    override fun getItemCount(): Int = categorias.size


    inner class CategoriaViewHolder(

        itemView: View

    ) : RecyclerView.ViewHolder(itemView){

        private var button = itemView.findViewById<Button>(R.id.btn_categoria)
        var categoriaDetails = CategoriaDetails()

        fun setCategoia( categoria: Categoria, position: Int){

            button.text = categoria.categoria

            categoriaDetails.categoria = categoria
            categoriaDetails.adapterPosition = position

            if(selectionTracker.isSelected( categoriaDetails.selectionKey )){

                button.setBackgroundColor( ContextCompat.getColor(itemView.context, R.color.feed_button_categoria_selected) )
                itemView.isActivated = true
                Log.i("Cliquei", "ID: ${categoriaDetails.selectionKey}")
                Log.i("Cliquei", "ID: ${selectionTracker.selection}")
                onclick.invoke(categoriaDetails.selectionKey.toInt())

            }else{

                button.setBackgroundColor( ContextCompat.getColor(itemView.context, R.color.feed_button_categoria_not_selected) )
                itemView.isActivated = false
                Log.i("desCliquei", "ID: ${selectionTracker.selection}")

                if(selectionTracker.selection.size() == 0){

                    val idTodas = -1

                    selectionTracker.select(idTodas.toLong())
                    onclick.invoke(idTodas)
                }

            }
        }
    }
}