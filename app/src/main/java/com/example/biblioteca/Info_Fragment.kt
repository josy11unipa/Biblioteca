package com.example.biblioteca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.InfoLayoutBinding

class Info_Fragment : Fragment() {

    private lateinit var binding: InfoLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InfoLayoutBinding.inflate(inflater)

        val uri="https://www.google.com/maps/dir//Viale+delle+Scienze,+8,+90128+Palermo+PA/@38.1051203,13.3454843,17z/data=!4m8!4m7!1m0!1m5!1m1!1s0x1319ef7a384ba497:0x8fcd066cfdddd06f!2m2!1d13.3480592!2d38.1051203?entry=ttu"
        binding.buttonMaps.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(i)
        }

        return binding.root
    }
}