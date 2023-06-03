package com.example.biblioteca

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.biblioteca.databinding.LibroLayoutBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.Gson

import com.google.gson.JsonParser

class Libro_Fragment:Fragment() {
    private lateinit var binding: LibroLayoutBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LibroLayoutBinding.inflate(inflater)

        setFragmentResultListener("keyId"){ requestKey, bundle ->
           val libroS=bundle.getString("keyBundleId")
            val parser=JsonParser()
            val libro:JsonObject =libroS as JsonObject
            binding.autore.text= libro.get("autore").asString
        }
        return binding.root
    }
}