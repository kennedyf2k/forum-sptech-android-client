package br.com.studenton.adapter.tracker

import androidx.recyclerview.selection.SelectionTracker

class CategoriaPredicate: SelectionTracker.SelectionPredicate<Long>() {

    override fun canSetStateForKey(key: Long, nextState: Boolean) = true

    override fun canSetStateAtPosition(position: Int, nextState: Boolean) = true

    override fun canSelectMultiple() = false

}
