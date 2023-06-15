package com.example.biblioteca.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.biblioteca.databinding.InfoLayoutBinding

class Info_Fragment : Fragment() {

    private lateinit var binding: InfoLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InfoLayoutBinding.inflate(inflater)
        val uri="geo:38.1051203,13.3454843?q=Viale+delle+Scienze,+8,+90128+Palermo+PA"
        binding.buttonMaps.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(i)
        }

        return binding.root
    }
}