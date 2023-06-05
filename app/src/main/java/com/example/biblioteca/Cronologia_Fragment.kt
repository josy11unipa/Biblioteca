package com.example.biblioteca

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.CronologiaLayoutBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Cronologia_Fragment:Fragment() {
    private lateinit var binding:CronologiaLayoutBinding

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= CronologiaLayoutBinding.inflate(inflater)
        //val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        //val dataS=data.format(Date()).toString()

       val dbManager = DBManager(requireContext())
        dbManager.open()
        val cursor=dbManager.getUser()
        val username = cursor.getString(cursor.getColumnIndex("username"))
        getCrono(username)



        return binding.root
    }

    private fun getCrono(username:String){

        val query="select libro.titolo,prenotazione.dataInizio,prenotazione.dataFine, prenotazione.consegnato from prenotazione,libro where libro.id=prenotazione.idL AND $username=prenotazione.usernameU;"
        ClientNetwork.retrofit.getCronologia(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful) {
                        val crono = (response.body()?.get("queryset") as JsonArray)
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        val adapter = CustomAdapterCrono(crono)
                        binding.recyclerView.adapter = adapter

                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    //
                }
            })

    }
}