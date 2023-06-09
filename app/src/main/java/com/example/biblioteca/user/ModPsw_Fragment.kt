package com.example.biblioteca.user

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.R
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.ModPasswordBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//funzione che gestisce il fragment per la modifica della password

class ModPsw_Fragment: Fragment() {
    private lateinit var binding: ModPasswordBinding
    var vecchiaPass =""
    var nuovaPass1 = ""
    var nuovaPass2 = ""
    private lateinit var dbManager: DBManager

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbManager = DBManager(requireContext())
        dbManager.open()
        binding= ModPasswordBinding.inflate(inflater)

        binding.buttonConfermaNuovaPass.setOnClickListener{ //gestione evento pressione bottone di conferma
            if (binding.campoVecchiaPass.text.toString() != "" && binding.campoNuovaPass1.text.toString() != "" && binding.campoNuovaPass2.text.toString() != "") {
                vecchiaPass = binding.campoVecchiaPass.text.toString()
                nuovaPass1 = binding.campoNuovaPass1.text.toString()
                nuovaPass2 = binding.campoNuovaPass2.text.toString()
                val user=dbManager.getUser()
                verificaOldPsw(user.getString(user.getColumnIndex("username")),vecchiaPass, nuovaPass1, nuovaPass2)

            }else{
                Log.i("LOG-ModPsw_Fragment", "L'utente non ha inserito le credenziali di modifica password")
                Toast.makeText(requireContext(),"Compila tutti i campi", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun verificaOldPsw(username:String,oldPass:String, psw1:String, psw2:String){ //funzione che verifica la correttezza della vecchia password inserita
        val query="SELECT password FROM persona WHERE username='$username';"
        Log.i("LOG-verificaOldPsw", "query= $query")
        ClientNetwork.retrofit.oldPass(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    Log.i("onResponse", "Response be like: ${response.body()}")
                    if (response.isSuccessful) {
                        var n = ((response.body()?.get("queryset")as JsonArray).get(0) as JsonObject).get("password").asString
                        Log.i("LOG-verificaOldPsw-onResponse", "n= $n")
                        if(oldPass==n){
                            modPws(username,oldPass, psw1, psw2)
                        }else{
                            Toast.makeText(requireContext(), "Vecchia password errata", Toast.LENGTH_LONG).show()
                            Log.i("LOG-verificaOldPsw-onResponse", "Vecchia password errata")
                        }
                    }else {
                        Log.i("LOG-verificaOldPsw-onResponse", "Errore durante la verifica della vecchia password")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-verificaOldPsw-onFailure", "Errore durante la verifica della vecchia password: ${t.message}")
                    Toast.makeText(requireContext(), "Errore durante la verifica della vecchia password", Toast.LENGTH_SHORT).show()
                }
            }
        )

    }
    private fun modPws(username:String,oldPassword:String, psw1:String, psw2:String) {  //funzione che gestisce la modifica della password
        if (psw1 != psw2) {
            // Le due password non corrispondono
            Toast.makeText(requireContext(), "Le password non corrispondono", Toast.LENGTH_LONG).show()
            Log.i("LOG-modPws", "Le password non corrispondono")
            return
        }else if(oldPassword==psw1){
            //la vecchia e la nuova password corrispondono
            Toast.makeText(requireContext(), "La nuova password non puo' corrispondere alla vecchia password", Toast.LENGTH_LONG).show()
            Log.i("LOG-modPws", "La nuova password non puo' corrispondere alla vecchia password")
            return
        }

        val query = "UPDATE persona SET password = '$psw1' WHERE username = '$username' AND password = '$oldPassword';"
        //query che effettua l'aggiornamento della password
        ClientNetwork.retrofit.register(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        // modifica password effettuata con successo
                        Toast.makeText(requireContext(), "Modifica password effettuata con successo", Toast.LENGTH_LONG).show()
                        Log.i("LOG-modPws-onResponse", "Modifica password effettuata con successo")
                        dbManager.delete()//pulisco il db locale e effettuo il logout dell'utente
                        val fragmentmanager=parentFragmentManager
                        val transaction=fragmentmanager.beginTransaction()
                        transaction.replace(R.id.fragmentMain, Login_Fragment())
                        transaction.commit()
                    }else {
                        Toast.makeText(requireContext(), "Errore durante la modifica della password", Toast.LENGTH_LONG).show()
                        Log.i("LOG-modPws-onResponse", "Errore durante la modifica della password")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-modPws-onFailure", "Errore durante la modifica della password: ${t.message}")
                    Toast.makeText(requireContext(), "Errore durante la modifica della password: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}