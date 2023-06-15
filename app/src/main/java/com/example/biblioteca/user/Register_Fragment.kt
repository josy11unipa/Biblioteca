package com.example.biblioteca.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.R
import com.example.biblioteca.com.example.biblioteca.RequestRegister
import com.example.biblioteca.databinding.RegisterLayoutBinding
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Register_Fragment: Fragment() {
    private lateinit var binding: RegisterLayoutBinding
    var nome =""
    var cognome = ""
    var username =""
    var password1 = ""
    var password2 = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= RegisterLayoutBinding.inflate(inflater)

        binding.buttonRegister.setOnClickListener {
            if (binding.campoRegisterNome.text.toString() != "" && binding.campoRegisterCognome.text.toString() != "" && binding.campoRegisterUsername.text.toString() != "" && binding.campoRegisterPassword1.text.toString() != "" && binding.campoRegisterPassword2.text.toString() != "") {
                nome=binding.campoRegisterNome.text.toString()
                cognome=binding.campoRegisterCognome.text.toString()
                username = binding.campoRegisterUsername.text.toString()
                password1 = binding.campoRegisterPassword1.text.toString()
                password2 = binding.campoRegisterPassword2.text.toString()

                val registerRequest = RequestRegister(nome=nome, cognome=cognome, username=username, password1=password1, password2=password2)
                Log.i("LOG-Register_Fragment", "chiamo la fun registerUtente passando: $registerRequest ")
                registerUtente(registerRequest)
            }else{
                Log.i("LOG-Login_Fragment", "L'utente non ha inserito le credenziali di regisgtrazione")
                Toast.makeText(requireContext(),"Compila tutti i campi", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
    }

    private fun registerUtente(registerRequest: RequestRegister) {//METTERE IL CONTROLLO SULL'UNICITà DELL'USERNAME
        if (registerRequest.password1 != registerRequest.password2) {
            // Le due password non corrispondono
            Toast.makeText(requireContext(), "Le password non corrispondono", Toast.LENGTH_LONG).show()
            Log.i("LOG-Login_Fragment", "Le password non corrispondono")
            return
        }
        val query = "INSERT INTO persona (username, password, nome, cognome, image, qr, type) VALUES ('${registerRequest.username}', '${registerRequest.password1}', '${registerRequest.nome}', '${registerRequest.cognome}', 'media/images/Immagine.png', 'HelloWord', 'u');"
        Log.i("LOG-Register_Fragment", "Insert creata: $query")

        ClientNetwork.retrofit.register(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    Log.i("onResponse", "Response be like: ${response.body()}")
                    if (response.isSuccessful) {
                        // Registrazione effettuata con successo
                        Toast.makeText(requireContext(), "Registrazione effettuata con successo", Toast.LENGTH_LONG).show()
                        Log.i("LOG-Register_Fragment-onResponse", "Registrazione effettuata con successo")
                        changeFrag()
                    } else {
                        Toast.makeText(requireContext(), "Errore durante la registrazione, utente già esistente", Toast.LENGTH_LONG).show()
                        Log.i("LOG-Register_Fragment-onResponse", "Errore durante la registrazione")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Register_Fragment-onFailure", "Errore durante la registrazione: ${t.message}")
                    Toast.makeText(requireContext(), "Errore durante la registrazione: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }
    fun changeFrag(){
        val fragmentmanager=parentFragmentManager
        val transaction=fragmentmanager.beginTransaction()
        transaction.replace(R.id.fragmentMain, Login_Fragment())
        transaction.commit()
    }
}