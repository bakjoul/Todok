package com.example.todok.ui.tasks

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.bakjoul.todok.R
import com.bakjoul.todok.databinding.TaskFragmentBinding

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setMenu()
        super.onViewCreated(view, savedInstanceState)

    }

    private fun setMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.actions, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.filter_alphabetical -> {
                        Log.d("test", "onMenuItemSelected: 1")
                        true
                    }
                    R.id.filter_alphabetical_inverted -> {
                        Log.d("test", "onMenuItemSelected: 2")
                        true
                    }
                    R.id.filter_oldest_first -> {
                        Log.d("test", "onMenuItemSelected: 3")
                        true
                    }
                    R.id.filter_recent_first -> {
                        Log.d("test", "onMenuItemSelected: 4")
                        true
                    }
                    else -> false
                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}