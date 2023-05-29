package com.example.biblioteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.HomeLayoutBinding
import com.example.biblioteca.databinding.InfoLayoutBinding

class Home_Fragment(): Fragment() {
    private lateinit var binding: HomeLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeLayoutBinding.inflate(inflater)
        return binding.root
    }
}