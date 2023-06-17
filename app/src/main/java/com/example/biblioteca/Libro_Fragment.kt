package com.example.biblioteca

import NotificationScheduler
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.database.LocalDBHelper
import com.example.biblioteca.databinding.LibroLayoutBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.time.LocalDate
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import kotlin.random.Random

class Libro_Fragment:Fragment() {
    private lateinit var binding: LibroLayoutBinding
    private lateinit var dbManager: DBManager
    private lateinit var notificationScheduler: NotificationScheduler
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LibroLayoutBinding.inflate(inflater)
        dbManager = DBManager(requireContext())
        dbManager.open()
        var button = binding.buttonPrenota
        setFragmentResultListener("keyId"){ requestKey, bundle ->
            val libroS=bundle.getString("keyBundleId")
            val libro=JsonParser().parse(libroS) as JsonObject
            binding.autore.text=libro.get("autore").asString
            binding.titolo1.text=libro.get("titolo").asString
            binding.annop.text=libro.get("anno").asString
            binding.genere.text=libro.get("genere").asString
            binding.descrizione1.text=libro.get("descrizione").asString
            binding.ratingBar.rating = libro.get("valutazione").asFloat
            binding.nCopie.text = "Copie rimanenti : " + libro.get("nCopie").asInt.toString()
            val url: String = libro.get("copertina").asString
            getImage(url)
            val idL = libro.get("id").asInt
            button.setOnClickListener{
                val user=dbManager.getUser()
                if(user.count !=0){
                    val query = "SELECT nCopie FROM libro WHERE id = '$idL';"
                    Log.i("TAG-Prenotazione", "queryP be like: $query")
                    ClientNetwork.retrofit.login(query).enqueue(
                        object : Callback<JsonObject> {
                            @SuppressLint("Range")
                            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                                Log.i("LOG-Login_Fragment-onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                                if (response.isSuccessful) {
                                    //Profile_Fragment.isLogged=true
                                    if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                                        //Log.i("LOG-Login_Fragment-onResponse", "Sono dentro il secondo if. e chiamo la getImageProfilo")
                                        var n = ((response.body()?.get("queryset")as JsonArray).get(0) as JsonObject).get("nCopie").asInt
                                        Log.i("LOG-Libro_Fragment-onResponse", "nCopie: $n")
                                        if(n>=1){
                                            alreadyTake(idL,user.getString(user.getColumnIndex("username")))
                                        }else{
                                            Log.i("LOG-Libro_Fragment-onResponse", "Copie esaurite")
                                        }
                                    }else {
                                        Log.i("LOG-Libro_Fragment-onResponse", "Copie non disponibili")
                                        Toast.makeText(requireContext(),"Copie non disponibili", Toast.LENGTH_LONG).show()
                                    }
                                }else{
                                    //Toast.makeText(requireContext(),"Errore richiestaDB", Toast.LENGTH_LONG).show()
                                    Log.i("LOG-Libro_Fragment-onResponse", "Errore richiestaDB")
                                }
                            }
                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                                Log.i("LOG-Login_Fragment-onFailure", "Errore prenotazione: ${t.message}")
                                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }else{
                    Log.i("LOG-Libro_Fragment-onFailure", "Utente non autenticato")
                    Toast.makeText(requireContext(), "Autenticati per prenotare", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return binding.root
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

    private fun alreadyTake(idL: Int, usernameUtente:String){
        Log.i("LOG-alreadyTake", "idL: $idL")
        val currentDate = LocalDate.now()
        Log.i("LOG-alreadyTake", "LocalDate: $currentDate")

        val query = "SELECT * FROM prenotazione WHERE '$idL' = idL AND '$usernameUtente' = usernameU AND consegnato=0;"
        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("LOG-alreadyTake", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        if ((response.body()?.get("queryset") as JsonArray).size() >0) {
                            Log.i("LOG-alreadyTake", "Hai già una prenotazione in corso per questo libro")
                            Log.i("LOG-alreadyTake", "reponse.body: ${response.body()}")
                            Toast.makeText(requireContext(),"Hai già una prenotazione in corso per questo libro", Toast.LENGTH_LONG).show()
                        } else {
                            effettuaPrenotazione(idL,usernameUtente)
                        }
                    }else{
                        //Toast.makeText(requireContext(),"Errore richiestaDB", Toast.LENGTH_LONG).show()
                        Log.i("LOG-Login_Fragment-onResponse", "Errore richiestaDB")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-Login_Fragment-onFailure", "Errore prenotazione: ${t.message}")
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )


    }
    private fun effettuaPrenotazione(idL: Int,usernameUtente:String){
        val currentDate = LocalDate.now()
        val minuti= LocalTime.now().minute
        val dataFinePrestito = currentDate.plusDays(15)
        val anno=dataFinePrestito.year
        val mese=dataFinePrestito.monthValue
        val giorno=dataFinePrestito.dayOfMonth
        crea_Notifica(anno,mese,giorno)
        val codice=generaCodiceCasuale(usernameUtente,minuti)
        val query = "INSERT INTO prenotazione (usernameU, dataInizio, dataFine, prenotazione.idL,codeConsegna) VALUES ('$usernameUtente', '$currentDate', '$dataFinePrestito', '$idL','$codice');"
        Log.i("LOG-effettuaPrenotazione", "QUERY: $query")
        ClientNetwork.retrofit.register(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("LOG-effettuaPrenotazione-onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    Log.i("LOG-effettuaPrenotazione-onResponse", "Response be like: ${response.body()}")
                    if (response.isSuccessful) {
                        modCopie(idL)
                        Toast.makeText(requireContext(), "Registrazione della prenotazione effettuata effettuata con successo", Toast.LENGTH_LONG).show()
                        Log.i("LOG-effettuaPrenotazione-onResponse", "Registrazione della prenotazione effettuata effettuata con successo")
                    } else {
                        Toast.makeText(requireContext(), "Errore durante la registrazione della prenotazione", Toast.LENGTH_LONG).show()
                        Log.i("LOG-effettuaPrenotazione-onResponse", "Errore durante la registrazione della prenotazione")
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-effettuaPrenotazione-onFailure", "Errore durante la registrazione: ${t.message}")
                    Toast.makeText(requireContext(), "Errore durante la registrazione: ${t.message}", Toast.LENGTH_SHORT).show()
                }

            }
        )
    }

    private fun modCopie(idl:Int){
        val query = "UPDATE libro SET nCopie = nCopie - 1 WHERE id = $idl"

        ClientNetwork.retrofit.register(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Log.i("LOG-modCopie-onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    Log.i("LOG-modCopie-onResponse", "Response be like: ${response.body()}")
                    if (response.isSuccessful) {
                        Log.i("LOG-modCopie-onResponse", "Diminuzione delle copie effettuata effettuata con successo")
                    } else {
                        Log.i("LOG-modCopie-onResponse", "Errore durante la diminuzione delle copie")
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Log.i("LOG-modCopie-onFailure", "Errore durante la diminuzione delle copie: ${t.message}")
                    Toast.makeText(requireContext(), "Errore durante la diminuzione delle copie: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun crea_Notifica(anno:Int, mese: Int, giorno:Int){
        notificationScheduler = NotificationScheduler(requireContext())
        notificationScheduler.registerNotificationReceiver()
        notificationScheduler.scheduleNotification(anno,mese,giorno)

    }

    private fun generaCodiceCasuale(user:String,minuto :Int):String{
        val random = Random(user.hashCode() + minuto)
        val dimensione=user.length*minuto
        val alfanumerico = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..dimensione.coerceAtMost(5))
            .map { alfanumerico[random.nextInt(alfanumerico.size)] }
            .joinToString("")
    }


}