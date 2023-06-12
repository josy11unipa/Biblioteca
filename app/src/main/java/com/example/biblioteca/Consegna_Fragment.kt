package com.example.biblioteca

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.biblioteca.databinding.ConsegnaLayoutBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Consegna_Fragment:Fragment() {
    lateinit var binding: ConsegnaLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= ConsegnaLayoutBinding.inflate(inflater)
        setFragmentResultListener("keyId") { requestKey, bundle ->
            val prenotazione = bundle.getString("chiaveBundle")
            Log.i("BUNDLE","$prenotazione")
            val prenotazioneS = JsonParser().parse(prenotazione) as JsonObject
           // val code=prenotazioneS.get("codeConsegna").asString  //codice di consegna
            //this.id=prenotazioneS.get("id").asInt
        }
        binding.button3.setOnClickListener{
           val codice= binding.editTextText.text.toString()
            if(codice != ""){
                verificaCodice(1,codice)
            }
        }
        return binding.root
    }

    private fun verificaCodice(id:Int,codice: String) {
        val query="SELECT * From prenotazione where prenotazione.id=$id and prenotazione.codeConsegna='$codice' ;"

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            Toast.makeText(requireContext(),"libro consegnato",Toast.LENGTH_SHORT).show()


                        }else {
                            Toast.makeText(requireContext(),"codice errato",Toast.LENGTH_SHORT).show()


                        }
                    }else{

                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Login_Fragment-onFailure", "Errore : ${t.message}")

                }

            })

    }
}