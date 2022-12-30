package com.maheshchukka.rickandmorty.ui.characters

import CharacterItemListener
import CharactersAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.maheshchukka.rickandmorty.R
import com.maheshchukka.rickandmorty.databinding.FragmentCharactersBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null

    private val charactersViewModel: CharactersViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.swipeContainer.setOnRefreshListener { charactersViewModel.onEvent(CharacterEvent.Refresh) }
        setupObserver()

        return root
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                charactersViewModel.state.collect { state ->
                    if (state.characters.isNotEmpty()) {
                        binding.progressBar.visibility = GONE
                        binding.emptyList.visibility = GONE
                        binding.retryButton.visibility = GONE
                        binding.list.visibility = VISIBLE

                        val adapter = CharactersAdapter(
                            CharacterItemListener { characterId ->
                                this@CharactersFragment.findNavController().navigate(
                                    CharactersFragmentDirections.actionCharactersFragmentToCharacterDetails(
                                        characterId = characterId
                                    )
                                )
                            }
                        )
                        adapter.addHeaderAndSubmitList(state.characters)
                        binding.list.addItemDecoration(
                            DividerItemDecoration(
                                binding.list.context,
                                (binding.list.layoutManager as LinearLayoutManager).orientation
                            )
                        )
                        binding.list.layoutManager = LinearLayoutManager(requireContext())
                        binding.list.adapter = adapter
                    } else if (state.isLoading) {
                        binding.progressBar.visibility = VISIBLE
                        binding.emptyList.visibility = GONE
                        binding.retryButton.visibility = GONE
                        binding.list.visibility = GONE
                    } else if (state.isRefreshing) {
                        binding.swipeContainer.isRefreshing = state.isRefreshing
                    } else {
                        binding.progressBar.visibility = GONE
                        binding.list.visibility = GONE
                        binding.emptyList.visibility = VISIBLE
                        binding.retryButton.visibility = VISIBLE
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener { charactersViewModel.onEvent(CharacterEvent.Refresh) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
