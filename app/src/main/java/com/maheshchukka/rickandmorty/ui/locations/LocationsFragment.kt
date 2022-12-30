package com.maheshchukka.rickandmorty.ui.locations

import LocationItemListener
import LocationsAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.maheshchukka.rickandmorty.databinding.FragmentLocationsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocationsFragment : Fragment() {

    private var _binding: FragmentLocationsBinding? = null
    private val binding get() = _binding!!
    private val locationsViewModel: LocationsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupObserver()

        return root
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                locationsViewModel.state.collect { state ->
                    if (state.locations.isNotEmpty()) {
                        binding.progressBar.visibility = View.GONE
                        binding.emptyList.visibility = View.GONE
                        binding.retryButton.visibility = View.GONE
                        binding.list.visibility = View.VISIBLE

                        val adapter = LocationsAdapter(
                            LocationItemListener { infoUrl ->
                                Toast.makeText(context, "$infoUrl", Toast.LENGTH_LONG).show()
                            }
                        )
                        adapter.addHeaderAndSubmitList(state.locations)
                        binding.list.addItemDecoration(
                            DividerItemDecoration(
                                binding.list.context,
                                (binding.list.layoutManager as LinearLayoutManager).orientation
                            )
                        )
                        binding.list.layoutManager = LinearLayoutManager(requireContext())
                        binding.list.adapter = adapter
                    } else if (state.isLoading) {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.emptyList.visibility = View.GONE
                        binding.retryButton.visibility = View.GONE
                        binding.list.visibility = View.GONE
                    } else {
                        binding.progressBar.visibility = View.GONE
                        binding.list.visibility = View.GONE
                        binding.emptyList.visibility = View.VISIBLE
                        binding.retryButton.visibility = View.VISIBLE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener { locationsViewModel.onEvent(LocationEvent.Refresh) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
