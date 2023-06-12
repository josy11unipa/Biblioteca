package com.example.biblioteca

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.database.LocalDBHelper
import com.example.biblioteca.databinding.LibroLayoutBinding
import com.example.biblioteca.databinding.MenuLayoutBinding
import com.example.biblioteca.user.Profile_Fragment

class HamburgerMenu:Fragment() {
    private lateinit var binding: MenuLayoutBinding
    private lateinit var dbManager: DBManager
    private lateinit var user: LocalDBHelper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=MenuLayoutBinding.inflate(inflater)
        dbManager = DBManager(requireContext())
        dbManager.open()
        val manager=parentFragmentManager
        val transaction=manager.beginTransaction()
        binding.buttonCronologia.setOnClickListener{
            val user=dbManager.getUser()
            if(user.count !=0){
                transaction.replace(R.id.fragmentMain,Cronologia_Fragment())
                transaction.commit()
            }else{
                Log.i("LOG-Hamburger", "Effettua l'accesso per accedere alla cronologia")
                Toast.makeText(requireContext(),"Effettua l'accesso per accedere alla cronologia", Toast.LENGTH_LONG).show()
            }
        }
        binding.button6.text= Profile_Fragment.isLogged.toString()

        return binding.root
    }
}