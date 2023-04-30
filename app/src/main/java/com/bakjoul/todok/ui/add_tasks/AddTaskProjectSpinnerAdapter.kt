package com.bakjoul.todok.ui.add_tasks

import android.database.DataSetObservable
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ListAdapter
import com.bakjoul.todok.databinding.AddTaskProjectSpinnerItemBinding

class AddTaskProjectSpinnerAdapter : ListAdapter, Filterable {

    private val dataSetObservable = DataSetObservable()
    private var items = emptyList<AddTaskProjectItemViewState>()

    override fun registerDataSetObserver(observer: DataSetObserver?) {
        dataSetObservable.registerObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
        dataSetObservable.unregisterObserver(observer)
    }

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): AddTaskProjectItemViewState? = items.getOrNull(position)

    override fun getItemId(position: Int): Long = getItem(position)?.projectId ?: -1

    override fun hasStableIds(): Boolean = true

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = if (convertView != null) {
            AddTaskProjectSpinnerItemBinding.bind(convertView)
        } else {
            AddTaskProjectSpinnerItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        }
        getItem(position)?.let {
            binding.addTaskSpinnerItemProjectColor.setColorFilter(it.projectColor)
            binding.addTaskSpinnerProjectName.text = it.projectName
        }
        return binding.root
    }

    override fun getItemViewType(position: Int): Int = 0

    override fun getViewTypeCount(): Int = 1

    override fun isEmpty(): Boolean = count == 0

    override fun areAllItemsEnabled(): Boolean = true

    override fun isEnabled(position: Int): Boolean = true
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults = FilterResults()
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {}
            override fun convertResultToString(resultValue: Any?): CharSequence {
                return (resultValue as AddTaskProjectItemViewState).projectName
            }
        }
    }

    fun setData(items: List<AddTaskProjectItemViewState>) {
        this.items = items
        dataSetObservable.notifyChanged()
    }
}
