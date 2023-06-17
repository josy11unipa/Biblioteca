package com.example.biblioteca

import android.annotation.SuppressLint
import android.os.Bundle
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
        val manager=parentFragmentManager
        val transaction=manager.beginTransaction()
        val cursor=dbManager.getUser()
        val username = cursor.getString(cursor.getColumnIndex("username"))
        getCrono(username)

        binding.button.setOnClickListener{//per visualizzare le prenotazioni in corso
            transaction.replace(R.id.fragmentMain, Prenotazioni_Fragment())
            transaction.addToBackStack("prenotazioni")
            transaction.commit()
        }
        return binding.root
    }

    private fun getCrono(username:String){ //query per visualizzare la cronologia delle prenotazioni
        val query="select libro.titolo,prenotazione.dataInizio,prenotazione.dataFine, prenotazione.consegnato from prenotazione,persona,libro where persona.username=prenotazione.usernameU AND libro.id=prenotazione.idL AND '$username'=prenotazione.usernameU Order by prenotazione.dataFine;"
        ClientNetwork.retrofit.getCronologia(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                        val j = (response.body()?.get("queryset") as JsonArray)
                        if(j.size()==0){  //se non ci sono prenotazioni in corso
                            binding.textView5.visibility=View.VISIBLE
                            binding.textView5.text="Cronologia vuota!"
                        }
                        val adapter= CustomAdapterCrono(j)
                        binding.recyclerViewCrono.adapter=adapter
                        binding.recyclerViewCrono.layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerViewCrono.adapter = adapter
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                }
            })
    }
}