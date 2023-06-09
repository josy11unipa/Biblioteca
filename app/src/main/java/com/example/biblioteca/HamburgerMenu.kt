package com.example.biblioteca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.LibroLayoutBinding
import com.example.biblioteca.databinding.MenuLayoutBinding
import com.example.biblioteca.user.Profile_Fragment

class HamburgerMenu:Fragment() {
    private lateinit var binding: MenuLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=MenuLayoutBinding.inflate(inflater)
        val manager=parentFragmentManager
        val transaction=manager.beginTransaction()
        binding.buttonCronologia.setOnClickListener{
            transaction.replace(R.id.fragmentMain,Cronologia_Fragment());
            transaction.commit();
        }
        binding.button6.text= Profile_Fragment.isLogged.toString()

        return binding.root
    }
}