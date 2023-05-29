package com.example.biblioteca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.biblioteca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val home_binding = binding.tastoHome
        val info_binding = binding.tastoInfo
        val profile_bindng = binding.tastoUser

        val manager=supportFragmentManager


        home_binding.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica is Home_Fragment) {
            }else{
                transaction.replace(R.id.fragmentMain,Home_Fragment())
            }
            transaction.commit()
        }


        info_binding.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica is Info_Fragment) {

            }else{
                transaction.replace(R.id.fragmentMain,Info_Fragment())
            }
            transaction.commit()
        }

        profile_bindng.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica is Profile_Fragment) {
            }else{
                transaction.replace(R.id.fragmentMain,Profile_Fragment())
            }
            transaction.commit()
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val verifica=fragmentManager.findFragmentById(R.id.fragmentMain)
        if(verifica is Home_Fragment){

            super.onBackPressed()
        }else{
            transaction.replace(R.id.fragmentMain,Home_Fragment())
            transaction.commit()
        }
    }


}






