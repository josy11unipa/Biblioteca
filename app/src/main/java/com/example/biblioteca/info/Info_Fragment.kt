package com.example.biblioteca.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.InfoLayoutBinding

//classe che gestisce il fragment riguardante le informazioni della biblioteca e relativa pressione del bottone

class Info_Fragment : Fragment() {

    private lateinit var binding: InfoLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InfoLayoutBinding.inflate(inflater)
        val uri="geo:38.1051203,13.3454843?q=Viale+delle+Scienze,+8,+90128+Palermo+PA"
        binding.premiPreMappeLayout.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(i)
        }
        return binding.root
    }
}