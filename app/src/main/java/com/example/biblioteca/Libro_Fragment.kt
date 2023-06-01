package com.example.biblioteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.LibroLayoutBinding

class Libro_Fragment:Fragment() {
    private lateinit var binding: LibroLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LibroLayoutBinding.inflate(inflater)
        return binding.root
    }
}