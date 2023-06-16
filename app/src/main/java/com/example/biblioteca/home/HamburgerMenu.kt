package com.example.biblioteca.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.biblioteca.Cronologia_Fragment
import com.example.biblioteca.Prenotazioni_Fragment
import com.example.biblioteca.R
import com.example.biblioteca.bibliotecario.Librarian_Fragment
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.database.LocalDBHelper
import com.example.biblioteca.databinding.MenuLayoutBinding
import com.example.biblioteca.user.ModDati_Fragment
import com.example.biblioteca.user.ModPsw_Fragment

class HamburgerMenu:Fragment() {
    private lateinit var binding: MenuLayoutBinding
    private lateinit var dbManager: DBManager
    private lateinit var user: LocalDBHelper
    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=MenuLayoutBinding.inflate(inflater)
        dbManager = DBManager(requireContext())
        dbManager.open()
        val user=dbManager.getUser()
        val manager=parentFragmentManager
        val transaction=manager.beginTransaction()
        if(user.count!=0 && user.getString(user.getColumnIndex("type"))=="A"){
            binding.admin.visibility=View.VISIBLE
            binding.admin.isClickable=true
        }else{
            binding.admin.visibility=View.GONE
            binding.admin.isClickable=false
        }
        binding.buttonCronologia.setOnClickListener{

            if(user.count !=0){
                transaction.replace(R.id.fragmentMain, Cronologia_Fragment())
                transaction.addToBackStack("crono")
                transaction.commit()
            }else{
                Log.i("LOG-Hamburger", "Effettua l'accesso per accedere alla cronologia")
                Toast.makeText(requireContext(),"Effettua l'accesso per accedere alla cronologia", Toast.LENGTH_LONG).show()
            }
        }
        binding.buttonPrestiti.setOnClickListener{
            if(user.count !=0) {
                transaction.replace(R.id.fragmentMain, Prenotazioni_Fragment())
                transaction.addToBackStack("prestiti")
                transaction.commit()
            }else{
                Log.i("LOG-Hamburger", "Effettua l'accesso per accedere ai prestiti")
                Toast.makeText(requireContext(),"Effettua l'accesso per accedere ai prestiti", Toast.LENGTH_LONG).show()
            }

        }
        binding.admin.setOnClickListener{
            transaction.replace(R.id.fragmentMain,Librarian_Fragment())
            transaction.addToBackStack("Librarian")
            transaction.commit()
        }

        binding.modificaPassword.setOnClickListener {
            if(user.count !=0) {
                transaction.replace(R.id.fragmentMain, ModPsw_Fragment())
                transaction.addToBackStack("ModPsw_Fragment")
                transaction.commit()
            }else{
                Log.i("LOG-Hamburger", "Effettua l'accesso per accedere alla modifica della password")
                Toast.makeText(requireContext(),"Effettua l'accesso per accedere alla modifica della password", Toast.LENGTH_LONG).show()
            }
        }
        binding.buttonModificaDati.setOnClickListener {
            if(user.count !=0) {
                transaction.replace(R.id.fragmentMain, ModDati_Fragment())
                transaction.addToBackStack("ModificaDati_Fragment")
                transaction.commit()
            }else{
                Log.i("LOG-Hamburger", "Effettua l'accesso per accedere alla modifica dei dati")
                Toast.makeText(requireContext(),"Effettua l'accesso per accedere alla modifica dei dati", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }
}