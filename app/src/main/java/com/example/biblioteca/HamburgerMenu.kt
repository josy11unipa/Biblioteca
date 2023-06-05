package com.example.biblioteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.LibroLayoutBinding
import com.example.biblioteca.databinding.MenuLayoutBinding

class HamburgerMenu:Fragment() {
    private lateinit var binding: MenuLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
}