package com.example.biblioteca.bibliotecario

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.databinding.DaConsegnareBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


//Classe che gestisce il fragment riguardante la lista dei libri da conseganre (lato Bibliotecario)
class LibriDaConsegnare:Fragment() {
    private lateinit var binding: DaConsegnareBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DaConsegnareBinding.inflate(inflater)
        val query="SELECT persona.nome,persona.cognome,prenotazione.codeConsegna,libro.titolo FROM persona,libro,prenotazione WHERE prenotazione.idL=libro.id AND prenotazione.usernameU=persona.username AND prenotazione.consegnato=0;"
        //Query che restituisce tutti i libri che non sono stati ancora consegnati
        ClientNetwork.retrofit.getPrenotazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        val j = (response.body()?.get("queryset") as JsonArray)
                        if(j.size()==0){//Nel caso non ci siano libri non ancora consegnati
                            binding.ListaVuotaText.visibility=View.VISIBLE
                            binding.ListaVuotaText.text="Non ci sono libri non ancora consegnati"
                        }else {
                            val adapter = CustomAdapterBibliotecario(j)
                            binding.recycleBibliotecario.adapter=adapter
                            binding.recycleBibliotecario.layoutManager= LinearLayoutManager(requireContext())
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("TAG-CRONOLOGIA", "sono nella onFailure = ${t.message}")
                }
            })
        return binding.root
    }
}