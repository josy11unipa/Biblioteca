package com.example.biblioteca.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.biblioteca.R
import com.example.biblioteca.TopBarFragment
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.LoginLayoutBinding

class Login_Fragment : Fragment() {

    private lateinit var coinImageView: ImageView
    private lateinit var loginFieldsLayout: LinearLayout
    private lateinit var binding: LoginLayoutBinding
    private lateinit var dbManager: DBManager
    var username =""
    var password = ""

    var flag=true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LoginLayoutBinding.inflate(inflater)
        dbManager = DBManager(requireContext())
        dbManager.open()  //apertura connessione al Database locale -->LocalDB

        coinImageView = binding.coinImageView
        loginFieldsLayout = binding.loginFieldsLayout
        val result = "Login"
        setFragmentResult("key", bundleOf("keyBundle" to result))
      //per cambiare searchBar

        binding.button2.setOnClickListener {
            username = binding.campoUsername.text.toString()
            password = binding.campoPassword.text.toString()
            dbManager.insert(username,password,"U")
            if(true){
                login()
                //da implementare credenziali corrette o sbagliate
            }else{
                //da implementare
            }
            //Log.d("TAG", "Button clicked")
        }
        return binding.root
    }
    fun login(){
        val fragmentmanager=parentFragmentManager
        val transaction=fragmentmanager.beginTransaction()
        transaction.replace(R.id.fragmentSearchBar, TopBarFragment())
        transaction.replace(R.id.fragmentMain, Profile_Fragment())
        transaction.commit()
    }
}
