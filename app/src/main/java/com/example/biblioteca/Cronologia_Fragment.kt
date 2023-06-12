package com.example.biblioteca

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

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
       val dbManager = DBManager(requireContext())
        dbManager.open()
        val cursor=dbManager.getUser()
        val username = cursor.getString(cursor.getColumnIndex("username"))
        getCrono(username)


        return binding.root
    }

    private fun getCrono(username:String){

        val query="select libro.titolo,prenotazione.dataInizio,prenotazione.dataFine, prenotazione.consegnato from prenotazione,persona,libro where persona.username=prenotazione.usernameU AND libro.id=prenotazione.idL AND '$username'=prenotazione.usernameU;"
        ClientNetwork.retrofit.getCronologia(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("TAG-CRONOLOGIA", "response=${response.isSuccessful}")
                    Log.i("TAG-CRONOLOGIA", "response=$username")

                    if (response.isSuccessful) {
                        val crono = (response.body()?.get("queryset") as JsonArray)
                        val adapter = CustomAdapterCrono(crono, object : CustomAdapterCrono.OnItemClickListener {
                            override fun onItemClick(position: Int) {
                                val oggetto: JsonObject = crono.get(position) as JsonObject
                                Toast.makeText(requireContext(),"Hai premuto: ${oggetto.get("titolo")}", Toast.LENGTH_LONG).show()
                                Log.i("TAG-OGGETTO CLICCATO", "OGGETTO CLICCATO: $oggetto")
                            }
                        })
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerView.adapter = adapter

                    }
                }


                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("TAG-CRONOLOGIA", "sono nella onFailure = ${t.message}")
                }
            })

    }
}