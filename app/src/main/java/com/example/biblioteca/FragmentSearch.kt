package com.example.biblioteca

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import com.example.biblioteca.databinding.FragmentSearchBinding
import com.example.biblioteca.home.Home_Fragment
import kotlinx.coroutines.selects.select

class FragmentSearch(): Fragment() {
    private lateinit var binding: FragmentSearchBinding
    companion object{
        var text : CharSequence= ""
    }
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
                text = binding.searchBar.query.toString()
                Log.i("TEXT","parola cercata: ${text}")
                val query =
                    "select * from libro where autore LIKE '%$text%' or titolo LIKE '%$text%' or anno LIKE'%$text%' or genere LIKE'$text';"

                setFragmentResult("queryK", bundleOf("queryB" to query))
                Log.i("FragmentSearch","query: $query text: $text")
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return binding.root
    }
}