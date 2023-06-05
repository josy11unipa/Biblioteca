package com.example.biblioteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.biblioteca.databinding.FragmentSearchBinding
import kotlinx.coroutines.selects.select

class FragmentSearch(): Fragment() {
    private lateinit var binding: FragmentSearchBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSearchBinding.inflate(inflater)
        binding.searchBar.setOnClickListener{
            binding.searchBar.isIconified = false
        }

        binding.searchBar.setOnQueryTextListener (object :SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val text = binding.searchBar.query
                val query =
                    "select * from libro where autore LIKE '%$text%' or titolo LIKE '%$text%' or anno LIKE'%$text%' or genere LIKE'$text';"
                setFragmentResult("queryK", bundleOf("queryB" to query))
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return binding.root
    }
}