package com.example.biblioteca

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.PrenotazioniLayoutBinding
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
        val query="select prenotazione.id,prenotazione.codeConsegna,libro.titolo,prenotazione.dataInizio,prenotazione.dataFine, prenotazione.consegnato,libro.anno,libro.genere,libro.autore,libro.copertina,libro.valutazione,libro.nValutazioni ,prenotazione.idL,prenotazione.posticipato from prenotazione,persona,libro where persona.username=prenotazione.usernameU AND libro.id=prenotazione.idL AND '$username'=prenotazione.usernameU AND prenotazione.consegnato=0;"
        ClientNetwork.retrofit.getPrenotazione(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {

                        val j = (response.body()?.get("queryset") as JsonArray)
                        if(j.size()==0){
                            binding.textView.visibility=View.VISIBLE
                            binding.textView.text="Non hai prenotazioni in corso"
                        }
                        val adapter=CustomAdapterPrenotazione(j)

                        adapter.setOnClickListener(object:
                            CustomAdapterPrenotazione.OnClickListener {
                            override fun onClick(position: Int, model: JsonObject) {
                                val consegna=JsonObject()//filtro il risultato della query precedente
                                consegna.addProperty("titolo",model.get("titolo").asString)
                                consegna.addProperty("autore",model.get("autore").asString)
                                consegna.addProperty("anno",model.get("anno").asString)
                                consegna.addProperty("genere",model.get("genere").asString)
                                consegna.addProperty("codeConsegna",model.get("codeConsegna").asString)
                                consegna.addProperty("copertina",model.get("copertina").asString)
                                consegna.addProperty("id",model.get("id").asString)
                                consegna.addProperty("idL",model.get("idL").asString)
                                consegna.addProperty("valutazione",model.get("valutazione").asString)
                                consegna.addProperty("nValutazioni",model.get("nValutazioni").asString)
                                consegna.addProperty("posticipato",model.get("posticipato").asString)
                                val manager=parentFragmentManager
                                setFragmentResult("keyId", bundleOf("chiaveBundle" to consegna.toString() ))
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
                }
            })
    }
}