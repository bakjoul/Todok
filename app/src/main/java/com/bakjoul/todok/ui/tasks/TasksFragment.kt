package com.example.todok.ui.tasks

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.bakjoul.todok.R
import com.bakjoul.todok.databinding.TaskFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TasksFragment : Fragment() {

    companion object {
        fun newInstance() = TasksFragment()
    }

    private var _binding: TaskFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TaskFragmentBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.actions, menu)
    }

    // TODO Add menu item click handling
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.filter_alphabetical -> {
                true
            }
            R.id.filter_alphabetical_inverted -> {
                true
            }
            R.id.filter_oldest_first -> {
                true
            }
            R.id.filter_recent_first -> {
                true
            }
            else -> false
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}