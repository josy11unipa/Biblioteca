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
        setFragmentResultListener("keyId") { requestKey, bundle ->
            val prenotazione = bundle.getString("chiaveBundle")
            Log.i("BUNDLE", "$prenotazione")
            val prenotazioneS = JsonParser().parse(prenotazione) as JsonObject
            val titolo=prenotazioneS.get("titolo").asString
            val autore=prenotazioneS.get("autore").asString
            binding.titolo1.text=titolo.toString()
            binding.annop.text=prenotazioneS.get("anno").asString
            binding.genere.text=prenotazioneS.get("genere").asString
            binding.autore.text=autore
            val url: String = prenotazioneS.get("copertina").asString
            getImage(url)
            // val code=prenotazioneS.get("codeConsegna").asString  //codice di consegna
            val id=prenotazioneS.get("id").asInt

            binding.buttonConsegna.setOnClickListener {
                val codice = binding.codice.text.toString()
                if (codice != "") {
                    verificaCodice(id, codice)
                }else{
                    Toast.makeText(requireContext(),"inserisci il codice",Toast.LENGTH_LONG).show()
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
                    registraValutazione(rating,titolo,autore)

                }
            }
        }
        return binding.root
    }

    private fun registraValutazione(rating: Float, titolo: String?, autore: String?) {

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
    private fun getImage(url: String) {
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
                    //Toast.makeText(requireContext(),"onFailure2", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}