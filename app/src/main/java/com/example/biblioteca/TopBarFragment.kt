package com.example.biblioteca

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.biblioteca.databinding.TopBarLayoutBinding

class TopBarFragment:Fragment() {
    private lateinit var binding : TopBarLayoutBinding
    var value:String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=TopBarLayoutBinding.inflate(inflater)
         setFragmentResultListener("key"){
             requestKey, bundle ->
             value = bundle.getString("keyBundle")
             binding.titolo.text = value.toString()
         }
        return binding.root
    }
}