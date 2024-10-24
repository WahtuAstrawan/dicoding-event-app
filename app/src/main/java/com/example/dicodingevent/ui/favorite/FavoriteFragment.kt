package com.example.dicodingevent.ui.favorite

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.ui.ViewModelFactory
import com.example.dicodingevent.ui.adapter.EventAdapter

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null

    private val binding get() = _binding!!
    private val adapter = EventAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorite.layoutManager = layoutManager
        binding.rvFavorite.adapter = adapter

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val favoriteViewModel: FavoriteViewModel by viewModels {
            factory
        }

        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { favEvents ->
            binding.progressBar.isVisible = false
            adapter.submitList(favEvents)
            binding.tvEmpty.isVisible = favEvents.isEmpty()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}