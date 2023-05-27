package com.example.biblioteca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.biblioteca.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val home_binding = binding.tastoHome
        home_binding.setOnClickListener{
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentMain,Home_Fragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val info_binding = binding.tastoInfo
        info_binding.setOnClickListener{
            val fragmentManager = supportFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentMain,Info_Fragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }





}