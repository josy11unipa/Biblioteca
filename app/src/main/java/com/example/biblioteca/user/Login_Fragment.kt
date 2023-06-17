package com.example.biblioteca.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.R
import com.example.biblioteca.RequestLogin
import com.example.biblioteca.bibliotecario.Librarian_Fragment
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.LoginLayoutBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//classe che gestisce il fragment per effettuare il login e la registrazione
class Login_Fragment : Fragment() {

    private lateinit var coinImageView: ImageView
    private lateinit var loginFieldsLayout: LinearLayout
    private lateinit var binding: LoginLayoutBinding
    private lateinit var dbManager: DBManager
    var username =""
    var password = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LoginLayoutBinding.inflate(inflater)
        dbManager = DBManager(requireContext())
        dbManager.open()  //apertura connessione al Database locale -->LocalDB

        coinImageView = binding.genericUserImg
        loginFieldsLayout = binding.loginFieldsLayout

        binding.button2.setOnClickListener { //pressione tasto di login
            if (binding.campoUsername.text.toString() != ""  && binding.campoPassword.text.toString() != ""){ //controllo che i campi di login non siano vuoti
                username = binding.campoUsername.text.toString()
                password = binding.campoPassword.text.toString()
                val loginRequestLogin = RequestLogin(username=username, password=password)
                loginUtente(loginRequestLogin)
            }else{
                Log.i("LOG-Login_Fragment", "L'utente non ha inserito le credenziali")
                Toast.makeText(requireContext(),"Inserisci le credenziali", Toast.LENGTH_LONG).show()
            }
        }
        binding.button4.setOnClickListener{ //pressione tasto per la registrazione
            val fragmentmanager=parentFragmentManager
            val transaction=fragmentmanager.beginTransaction()
            transaction.replace(R.id.fragmentMain, Register_Fragment())
            transaction.commit()
        }
        return binding.root
    }
    fun login(tipo:String){
        val fragmentmanager = parentFragmentManager
        val transaction = fragmentmanager.beginTransaction()
       if(tipo=="u") { //controllo il tipo di utente (u = utente normale, a = bibliotecario)
           transaction.replace(R.id.fragmentMain, Profile_Fragment())
       }else{
           transaction.replace(R.id.fragmentMain,Librarian_Fragment())
       }
        transaction.commit()
    }
    private fun loginUtente (requestLogin: RequestLogin){ //funzione che gestisce il login dell'utente
        val query = "select * from persona where username = '${requestLogin.username}' and password = '${requestLogin.password}';"
        //query che restituisce tutte le tuple aventi come username e password i dati inseriti nei campi dall'utente
        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful && (response.body()?.get("queryset") as JsonArray).size() == 1) {//nel caso username e password siano corretti verrà restituita una e una sola tupla
                        val tipo= getUser((response.body()?.get("queryset")as JsonArray).get(0) as JsonObject) //memorizzo il tipo dell'utente
                        login(tipo)
                        Log.i("LOG-Login_Fragment-onResponse", "LOGGATO")
                    }else{
                        Toast.makeText(requireContext(),"Credenziali errate", Toast.LENGTH_LONG).show()
                        Log.i("LOG-Login_Fragment-onResponse", "CREDENZIALI ERRATE")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Login_Fragment-onFailure", "Errore accesso ${t.message}")
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
    private fun getUser(jsonObject: JsonObject):String{ //funzione che memorizza nel db locale le informazioni dell'utente che ha effettuato il login
        val username=jsonObject.get("username").asString
        val nome=jsonObject.get("nome").asString
        val cognome=jsonObject.get("cognome").asString
        val qr=jsonObject.get("qr").asString
        val type=jsonObject.get("type").asString
        dbManager.insert(username,nome,cognome,qr,type) //insert nel db locale
        return type
    }
}
