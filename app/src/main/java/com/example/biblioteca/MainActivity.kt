package com.example.biblioteca

import NotificationScheduler
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES.O
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.biblioteca.databinding.ActivityMainBinding
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.database.LocalDBHelper
import com.example.biblioteca.home.HamburgerMenu
import com.example.biblioteca.home.Home_Fragment
import com.example.biblioteca.info.Info_Fragment
import com.example.biblioteca.user.Login_Fragment
import com.example.biblioteca.user.Profile_Fragment
import com.example.biblioteca.user.Register_Fragment
import java.util.Calendar


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbManager: DBManager
    private lateinit var user:LocalDBHelper

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val home_binding = binding.tastoHome
        val info_binding = binding.tastoInfo
        val profile_bindng = binding.tastoUser
        val debug = binding.tastoDebugBackstack
        val manager=supportFragmentManager

        dbManager = DBManager(this)
        dbManager.open()

        home_binding.setOnClickListener{

            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica is Home_Fragment) {
            }else{
                transaction.replace(R.id.fragmentMain, Home_Fragment())
            }
            transaction.commit()
        }

        info_binding.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica is Info_Fragment) {
            }else{
                transaction.replace(R.id.fragmentMain, Info_Fragment())
            }
            transaction.commit()
        }

        profile_bindng.setOnClickListener{
            val user=dbManager.getUser()
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)

            if(user.count!=0) {
                if (verifica is Profile_Fragment) {
                }else {
                    transaction.replace(R.id.fragmentMain, Profile_Fragment())
                    Log.i("LOG-MainActivity","user.count() = ${user.count}")
                }
            }else{
                Log.i("LOG-MainActivity","user.count() = ${user.count}")
                transaction.replace(R.id.fragmentMain, Login_Fragment())
            }
            transaction.commit()
        }

        debug.setOnClickListener{
            val transaction = manager.beginTransaction()
            var verifica = manager.findFragmentById(R.id.fragmentMain)
            if(verifica is HamburgerMenu) {
            }else{
                transaction.replace(R.id.fragmentMain, HamburgerMenu())
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
        }else if(verifica is Info_Fragment || verifica is Profile_Fragment || verifica is Register_Fragment || verifica is Login_Fragment){
            transaction.replace(R.id.fragmentMain, Home_Fragment())
            transaction.commit()
        }else if(verifica is Libro_Fragment){
            transaction.replace(R.id.fragmentMain, Home_Fragment())
            transaction.commit()
        }else{
            super.onBackPressed()
        }
    }


    override fun onDestroy() {
        dbManager.close()
        super.onDestroy()
    }
}