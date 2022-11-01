package br.com.studenton.adapter.tracker

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import br.com.studenton.adapter.AdapterCategoriaResponse

class CategoriaLockup( val rvCategoria: RecyclerView ): ItemDetailsLookup<Long>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {

        val view = rvCategoria.findChildViewUnder( e.x, e.y )

        if( view != null ){

            val holder = rvCategoria.getChildViewHolder( view )

            return (holder as AdapterCategoriaResponse.CategoriaViewHolder).categoriaDetails

        }

        return null

    }


}