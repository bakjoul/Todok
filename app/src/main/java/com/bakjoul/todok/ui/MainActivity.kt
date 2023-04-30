package com.bakjoul.todok.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bakjoul.todok.R
import com.bakjoul.todok.databinding.ActivityMainBinding
import com.example.todok.ui.add_tasks.AddTaskDialogFragment
import com.bakjoul.todok.ui.tasks.TasksFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.mainLayout.id, TasksFragment.newInstance())
                .commitNow()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }

    override fun displayAddTaskDialog() {
        AddTaskDialogFragment.newInstance().show(supportFragmentManager, null)
    }
}
