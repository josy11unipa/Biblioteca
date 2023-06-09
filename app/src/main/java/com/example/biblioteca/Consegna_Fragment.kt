package com.example.biblioteca

import android.graphics.BitmapFactory
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
import okhttp3.ResponseBody
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
        setFragmentResultListener("keyId") { requestKey, bundle ->//ricevo le informazioni dal fragment Prenotazioni_Fragment
            val prenotazioneS = bundle.getString("chiaveBundle")
            Log.i("BUNDLE", "$prenotazioneS")
            val prenotazione = JsonParser().parse(prenotazioneS) as JsonObject
            val titolo=prenotazione.get("titolo").asString
            val idL=prenotazione.get("idL").asString
            val autore=prenotazione.get("autore").asString
            val valutazione=prenotazione.get("valutazione").asFloat
            val nValutazioni=prenotazione.get("nValutazioni").asInt
            val id=prenotazione.get("id").asInt

            val posticipato=prenotazione.get("posticipato").asInt
            binding.titolo1.text=titolo.toString()
            binding.annop.text=prenotazione.get("anno").asString
            binding.genere.text=prenotazione.get("genere").asString
            binding.autore.text=autore
            val url: String = prenotazione.get("copertina").asString
            getImage(url)
            binding.buttonConsegna.setOnClickListener {
                val codice = binding.codice.text.toString()
                if (codice != "") {
                    verificaCodice(id, codice,idL)
                }else{
                    Toast.makeText(requireContext(),"inserisci il codice",Toast.LENGTH_LONG).show()
                }
            }
            binding.buttonPosticipa.setOnClickListener{
                if(posticipato==1){
                   Toast.makeText(requireContext(),"Hai già posticipato la consegna",Toast.LENGTH_LONG).show()
                }else {
                    posticipa(id)
                }
            }
            binding.buttonVota.setOnClickListener{
                val rating = binding.ratingBar.rating
                if(rating==0.0f){
                    Toast.makeText(requireContext(),"dai una valutazione",Toast.LENGTH_LONG).show()
                }else {
                    binding.textValutazione.text = "Grazie per aver lasciato una valutazione a questo libro"
                    binding.buttonVota.visibility = View.GONE
                    binding.ratingBar.setIsIndicator(true)
                    Toast.makeText(requireContext(), "hai valutato : $rating", Toast.LENGTH_LONG).show()
                    registraValutazione(idL,rating,valutazione,nValutazioni)
                }
            }
        }
        return binding.root
    }

    private fun posticipa(id: Int) { //query per posticipare la consegna
        val query="UPDATE prenotazione SET dataFine = DATE_ADD(dataFine, INTERVAL 10 DAY),posticipato=1 WHERE prenotazione.id=$id AND prenotazione.posticipato=0;"
        ClientNetwork.retrofit.posticipa(query).enqueue(
            object :Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful()){
                        binding.textView6.text="-->CONSEGNA POSTICIPATA CON SUCCESSO<--"
                        binding.buttonPosticipa.visibility=View.GONE
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Login_Fragment-onFailure", "Errore : ${t.message}")
                }

            }
        )
    }

    private fun registraValutazione(idL: String?, nuovaValutazione: Float, valutazione: Float, nValutazioni: Int) { //registro la valutazione data dall utente per il libro
        val media= ((valutazione*nValutazioni)+nuovaValutazione)/(nValutazioni+1)
        val query="UPDATE libro SET valutazione=$media,nValutazioni=${nValutazioni+1} WHERE libro.id=$idL;"
        ClientNetwork.retrofit.modificaValutazione(query).enqueue(
            object :Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful()){
                        Log.i("VALUTAZIONE","VALUTAZIONE REGISTRATA")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Login_Fragment-onFailure", "Errore : ${t.message}")
                }
            }
        )
    }
    private fun verificaCodice(id:Int,codice: String,idL:String) { //verifico che il codice di riconsegna sia corretto
        val query="SELECT * From prenotazione where prenotazione.id=$id and prenotazione.codeConsegna='$codice' ;"

        ClientNetwork.retrofit.verificaCodice(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful){
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            Toast.makeText(requireContext(),"libro consegnato",Toast.LENGTH_SHORT).show()
                            incrementa_copie(idL)
                            consegnaLibro(id)
                        }else {
                            Toast.makeText(requireContext(),"codice errato",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-valutazione", "Errore : ${t.message}")
                }
            })
    }
    private fun consegnaLibro(id: Int) { //consegno il libro
        val query="UPDATE prenotazione SET consegnato=1 WHERE id=$id"
        ClientNetwork.retrofit.modificaValutazione(query).enqueue(
            object :Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        binding.LinearLayoutValutazione.visibility=View.VISIBLE
                        binding.Layout1.visibility=View.GONE
                        Log.i("TAG","consegnato")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-consegna errore", "Errore : ${t.message}")
                }
            }
        )
    }
    private fun incrementa_copie(id: String) {//incremento le copie dopo la riconsegna del libro
        val query="UPDATE libro SET nCopie=nCopie+1 WHERE id=$id"
        ClientNetwork.retrofit.incrementaCopie(query).enqueue(
            object :Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if(response.isSuccessful){
                        Log.i("TAG","copie incrementate")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-copie errore", "Errore : ${t.message}")
                }
            }
        )
    }
    private fun getImage(url: String) {//get immagine
        ClientNetwork.retrofit.getAvatar(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                            binding.copertina.setImageBitmap(avatar)
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }
            }
        )
    }
}