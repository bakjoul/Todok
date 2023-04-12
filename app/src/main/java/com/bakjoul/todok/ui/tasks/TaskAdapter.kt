package com.bakjoul.todok.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bakjoul.todok.databinding.TaskItemBinding

class TaskAdapter : ListAdapter<TaskViewStateItem, TaskAdapter.ViewHolder>(TaskDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TaskViewStateItem) {
            binding.apply {
                // TODO
            }
        }
    }

    object TaskDiffCallback : DiffUtil.ItemCallback<TaskViewStateItem>() {
        override fun areItemsTheSame(
            oldItem: TaskViewStateItem,
            newItem: TaskViewStateItem
        ): Boolean {
            return oldItem.taskId == newItem.taskId
        }

        override fun areContentsTheSame(
            oldItem: TaskViewStateItem,
            newItem: TaskViewStateItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}