package com.example.biblioteca

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.PrenotazioniLayoutBinding
import com.example.biblioteca.home.CustomAdapterLista
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Prenotazioni_Fragment:Fragment() {
    private lateinit var binding:PrenotazioniLayoutBinding
    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=PrenotazioniLayoutBinding.inflate(inflater)

        val dbManager = DBManager(requireContext())
        dbManager.open()
        val cursor=dbManager.getUser()
        val username = cursor.getString(cursor.getColumnIndex("username"))
        getPrenotazioni(username)
        return binding.root
    }

    private fun getPrenotazioni(username: String?) {
        val query="select prenotazione.id,prenotazione.codeConsegna,libro.titolo,prenotazione.dataInizio,prenotazione.dataFine, prenotazione.consegnato,libro.anno,libro.genere,libro.autore,libro.copertina from prenotazione,persona,libro where persona.username=prenotazione.usernameU AND libro.id=prenotazione.idL AND '$username'=prenotazione.usernameU AND prenotazione.consegnato=0;"
        ClientNetwork.retrofit.getPrenotazione(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("TAG-prenotazione", "response=${response.isSuccessful}")
                    Log.i("TAG-prenotazione", "response=$username")

                    if (response.isSuccessful) {
                        val j = (response.body()?.get("queryset") as JsonArray)
                        val adapter=CustomAdapterPrenotazione(j)

                        adapter.setOnClickListener(object:
                            CustomAdapterPrenotazione.OnClickListener {
                            override fun onClick(position: Int, model: JsonObject) {
                                val manager=parentFragmentManager
                                setFragmentResult("keyId", bundleOf("chiaveBundle" to model.toString() ))
                                val transaction=manager.beginTransaction()
                                transaction.replace(R.id.fragmentMain,Consegna_Fragment())
                                transaction.addToBackStack("prenotazioni")
                                transaction.commit()
                            }
                        })
                        binding.recyclePrenotazioni.adapter=adapter
                        binding.recyclePrenotazioni.layoutManager=LinearLayoutManager(requireContext())


                    }
                }


                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("TAG-CRONOLOGIA", "sono nella onFailure = ${t.message}")
                }
            })
    }
}