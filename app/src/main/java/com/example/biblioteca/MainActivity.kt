package com.example.biblioteca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.example.biblioteca.databinding.ActivityMainBinding
import androidx.fragment.app.setFragmentResult


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
                transaction.replace(R.id.fragmentSearchBar,FragmentSearch())

            }
            transaction.commit()
        }

        info_binding.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica is Info_Fragment) {
            }else{
                transaction.replace(R.id.fragmentSearchBar,TopBarFragment())
                transaction.replace(R.id.fragmentMain,Info_Fragment())

            }
            transaction.commit()
        }

        profile_bindng.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica is Profile_Fragment) {
            }else{
                transaction.replace(R.id.fragmentSearchBar,TopBarFragment())
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

            val dialogBuilder = android.app.AlertDialog.Builder(this)
            dialogBuilder.setMessage("Vuoi uscire dall'app?")
            dialogBuilder.setTitle("Uscita autorizzata")
            dialogBuilder.setPositiveButton("Si"){
                    _, _ ->
                super.onBackPressed()
            }
            dialogBuilder.setNegativeButton("No"){
                    _, _ ->
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }else{
            transaction.replace(R.id.fragmentMain,Home_Fragment())
            transaction.replace(R.id.fragmentSearchBar,FragmentSearch())
            transaction.commit()
        }
    }
}






