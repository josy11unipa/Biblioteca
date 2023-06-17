package com.example.biblioteca.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.R
import com.example.biblioteca.database.DBManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.fragment.app.Fragment
import com.example.biblioteca.databinding.ModDatiBinding

//funzione che gestisce il fragment per la modifica dei dati
class ModDati_Fragment :Fragment() {
    private lateinit var binding: ModDatiBinding
    var password =""
    var nuovoNome = ""
    var nuovoCognome = ""
    private lateinit var dbManager: DBManager

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbManager = DBManager(requireContext())
        dbManager.open()
        binding= ModDatiBinding.inflate(inflater)

        binding.buttonConferma.setOnClickListener{ //gestione evento pressione bottone di conferma
            if (binding.campoPass.text.toString() != "" && binding.campoNuovoNome.text.toString() != "" && binding.campoNuovoCognome.text.toString() != "") {
                password = binding.campoPass.text.toString()
                nuovoNome = binding.campoNuovoNome.text.toString()
                nuovoCognome = binding.campoNuovoCognome.text.toString()
                val user=dbManager.getUser()
                verificaPsw(user.getString(user.getColumnIndex("username")),password, nuovoNome, nuovoCognome)

            }else{
                Log.i("LOG-ModPsw_Fragment", "L'utente non ha inserito le credenziali di modifica password")
                Toast.makeText(requireContext(),"Compila tutti i campi", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun verificaPsw(username:String,pass:String, nuovoNome:String, nuovoCognome:String){ //funzione che verifica la correttezza della password inserita
        val query="SELECT password FROM persona WHERE username='$username';"
        ClientNetwork.retrofit.oldPass(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    Log.i("onResponse", "Response be like: ${response.body()}")
                    if (response.isSuccessful) {
                        var n = ((response.body()?.get("queryset")as JsonArray).get(0) as JsonObject).get("password").asString
                        if(pass==n){//se la password è corretta chiamo la funzione per modificare i dati
                            modDati(username, nuovoNome, nuovoCognome)
                        }else{
                            Toast.makeText(requireContext(), "Password errata", Toast.LENGTH_LONG).show()
                            Log.i("LOG-verificaPsw-onResponse", "Password errata")
                        }
                    }else {
                        Log.i("LOG-verificaPsw-onResponse", "Errore durante la verifica della password")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-verificaOldPsw-onFailure", "Errore durante la verifica della password: ${t.message}")
                    Toast.makeText(requireContext(), "Errore durante la verifica della password", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    private fun modDati(username:String, nuovoNome:String, nuovoCognome:String) { //funzione che gestisce la modifica dei dati
        if (nuovoNome == nuovoCognome) {
            Toast.makeText(requireContext(), "Il nome e il cognome no possono corrispondere", Toast.LENGTH_LONG).show()
            return
        }
        val query = "UPDATE persona SET nome = '$nuovoNome', cognome = '$nuovoCognome' WHERE username = '$username';"
        //query che effettua l'aggiornamento dei dati
        ClientNetwork.retrofit.register(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    Log.i("onResponse", "Response be like: ${response.body()}")
                    if (response.isSuccessful) {
                        // modifica dati effettuata con successo
                        Toast.makeText(requireContext(), "Modifica dei dati effettuata con successo", Toast.LENGTH_LONG).show()
                        dbManager.delete()  //pulisco il db locale e effettuo il logout dell'utente
                        val fragmentmanager=parentFragmentManager
                        val transaction=fragmentmanager.beginTransaction()
                        transaction.replace(R.id.fragmentMain, Login_Fragment())
                        transaction.commit()
                    }else {
                        Toast.makeText(requireContext(), "Errore durante la modifica dei dati", Toast.LENGTH_LONG).show()
                        Log.i("LOG-modPws-onResponse", "Errore durante la modifica dei dati")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-modPws-onFailure", "Errore durante la modifica dei dati: ${t.message}")
                    Toast.makeText(requireContext(), "Errore durante la modifica dei dati: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}