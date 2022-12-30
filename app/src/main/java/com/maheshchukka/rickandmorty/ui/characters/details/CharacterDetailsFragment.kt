package com.maheshchukka.rickandmorty.ui.characters.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.maheshchukka.rickandmorty.R
import com.maheshchukka.rickandmorty.databinding.FragmentCharactersDetailsBinding
import com.maheshchukka.rickandmorty.ui.characters.CharacterEvent
import com.maheshchukka.rickandmorty.ui.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CharacterDetailsFragment : Fragment() {
    private var _binding: FragmentCharactersDetailsBinding? = null

    private val viewModel: CharacterDetailsViewModel by viewModels()

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        setupObserver()

        return root
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    if (state.isLoading || state.isRefreshing) {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.emptyList.visibility = View.GONE
                        binding.retryButton.visibility = View.GONE
                        binding.charactersGroup.visibility = View.GONE
                    } else if (state.error != null && state.error.isBlank()) {
                        binding.progressBar.visibility = View.GONE
                        binding.charactersGroup.visibility = View.GONE
                        binding.emptyList.visibility = View.VISIBLE
                        binding.retryButton.visibility = View.VISIBLE
                    } else {
                        val characterModel = state.character

                        binding.progressBar.visibility = View.GONE
                        binding.emptyList.visibility = View.GONE
                        binding.retryButton.visibility = View.GONE
                        binding.charactersGroup.visibility = View.VISIBLE

                        binding.characterImage.loadImage(characterModel.imageUrl)
                        binding.characterName.text =
                            characterModel.name ?: getString(R.string.shared_label_unavailable)
                        binding.characterStatus.text =
                            characterModel.status ?: getString(R.string.shared_label_unavailable)
                        binding.characterOrigin.text =
                            characterModel.origin ?: getString(R.string.shared_label_unavailable)
                        binding.characterLocation.text =
                            characterModel.location ?: getString(R.string.shared_label_unavailable)
                        binding.characterSpecies.text =
                            characterModel.species ?: getString(R.string.shared_label_unavailable)
                        binding.characterGender.text =
                            characterModel.gender ?: getString(R.string.shared_label_unavailable)
                    }
                }
            }
        }
        binding.retryButton.setOnClickListener { viewModel.onEvent(CharacterEvent.Refresh) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            findNavController().navigateUp()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}