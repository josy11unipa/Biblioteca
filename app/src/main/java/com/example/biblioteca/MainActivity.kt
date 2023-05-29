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
            if(verifica!=null){
                if(!fragmentExsist("Home_fragment")){
                    transaction.replace(R.id.fragmentMain,Home_Fragment())
                    transaction.addToBackStack("Home_fragment")
                }
            }else {
                transaction.add(R.id.fragmentMain, Home_Fragment())
                transaction.addToBackStack("Home_fragment")
            }
            transaction.commit()
        }


        info_binding.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica!=null){
                if(!fragmentExsist("Info_fragment")){
                    transaction.replace(R.id.fragmentMain,Info_Fragment())
                    transaction.addToBackStack("Info_fragment")
                }
            }else {
                transaction.add(R.id.fragmentMain, Info_Fragment())
                transaction.addToBackStack("Info_fragment")
            }
            transaction.commit()
        }

        profile_bindng.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica!=null){
                if(!fragmentExsist("Profile_fragment")) {
                    transaction.replace(R.id.fragmentMain, Profile_Fragment())
                    transaction.addToBackStack("Profile_fragment")
                }
            }else {
                transaction.add(R.id.fragmentMain, Profile_Fragment())
                transaction.addToBackStack("Profile_fragment")
            }
            transaction.commit()
        }
    }

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val backStackCount = fragmentManager.backStackEntryCount

        if (backStackCount > 0) {                       //si usa l'if per "tornare indietro", quindi per fare la pop si una singola posizione all'interno del backstack
            fragmentManager.popBackStack()
        } else {
            //super.onBackPressed()
            TODO("Inserire richiesta uscita app")
        }
    }

    private fun fragmentExsist(tag_c:String): Boolean{
        var flag = false
        val manager=supportFragmentManager
        val backStackCount = manager.backStackEntryCount
        for (i in 0 until backStackCount) {
            val entry = manager.getBackStackEntryAt(i)
            if (entry.name == tag_c) {
                Log.i("TAG", "${entry.name} $tag_c")
                return true
            }else{
                Log.i("TAG", "${entry.name} $tag_c")
                flag = false
            }
        }
        return flag
    }
}






