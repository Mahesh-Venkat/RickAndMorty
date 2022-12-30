package com.maheshchukka.rickandmorty.ui.locations.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maheshchukka.rickandmorty.R
import com.maheshchukka.rickandmorty.databinding.FragmentLocationDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationDetailsFragment : Fragment() {
    private var _binding: FragmentLocationDetailsBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val infoUrl = LocationDetailsFragmentArgs.fromBundle(requireArguments()).infoUrl
        activity?.actionBar?.setDisplayHomeAsUpEnabled(true)
        activity?.actionBar?.setHomeAsUpIndicator(R.drawable.ic_back_arrow)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
