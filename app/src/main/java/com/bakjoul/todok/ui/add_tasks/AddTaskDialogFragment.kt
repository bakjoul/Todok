package com.bakjoul.todok.ui.add_tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bakjoul.todok.databinding.AddTaskDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddTaskDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = AddTaskDialogFragment()
    }

    private var _binding: AddTaskDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AddTaskViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddTaskDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AddTaskProjectSpinnerAdapter()
        binding.addTaskProjectSpinnerActv.setAdapter(adapter)
        binding.addTaskProjectSpinnerActv.setOnItemClickListener { _: AdapterView<*>, _: View, position: Int, _: Long ->
            adapter.getItem(position)
                ?.let { project -> viewModel.onProjectSelected(project.projectId) }
        }

        binding.addTaskDescriptionEdit.doAfterTextChanged {
            viewModel.onTaskDescriptionChanged(it?.toString())
        }

        binding.addTaskAddButton.setOnClickListener {
            viewModel.onAddButtonClicked()
        }

        viewModel.viewStateLiveData.observe(viewLifecycleOwner) { viewState ->
            adapter.setData(viewState.items)
            binding.addTaskDescription.error = viewState.taskDescriptionError
            binding.addTaskProjectSpinnerLayout.error = viewState.projectError
        }

        viewModel.singleLiveEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is AddTaskEvent.Dismiss -> dismiss()
                is AddTaskEvent.Toast -> Toast.makeText(
                    requireContext(),
                    getString(event.stringRes),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        _binding = null
    }
}
