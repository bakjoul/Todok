package com.bakjoul.todok.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.bakjoul.todok.R
import com.bakjoul.todok.databinding.TaskFragmentBinding
import com.bakjoul.todok.ui.NavigationListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment() {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private var _binding: TaskFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<TasksViewModel>()

    private lateinit var navigationListener: NavigationListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigationListener = context as NavigationListener
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TaskFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = TaskAdapter()
        binding.tasksRecyclerView.adapter = adapter
        binding.tasksFabAdd.setOnClickListener { viewModel.onAddButtonClicked() }
        setMenu()

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { taskViewStates ->
            adapter.submitList(taskViewStates)
            if (taskViewStates.isEmpty()) binding.noTask.visibility = View.VISIBLE
            else binding.noTask.visibility = View.GONE
        }

        viewModel.singleLiveEvent.observe(viewLifecycleOwner) { tasksEvent ->
            when (tasksEvent) {
                TasksEvent.DisplayAddTaskDialog -> navigationListener.displayAddTaskDialog()
            }
        }

    }

    private fun setMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.actions, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = when (menuItem.itemId) {
                R.id.filter_alphabetical -> {
                    viewModel.onSortingTypeChanged(TaskSortingType.PROJECT_ALPHABETICAL)
                    true
                }

                R.id.filter_alphabetical_inverted -> {
                    viewModel.onSortingTypeChanged(TaskSortingType.PROJECT_REVERSE_ALPHABETICAL)
                    true
                }

                R.id.filter_oldest_first -> {
                    viewModel.onSortingTypeChanged(TaskSortingType.TASK_CHRONOLOGICAL)
                    true
                }

                R.id.filter_recent_first -> {
                    viewModel.onSortingTypeChanged(TaskSortingType.TASK_REVERSE_CHRONOLOGICAL)
                    true
                }

                else -> false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}
