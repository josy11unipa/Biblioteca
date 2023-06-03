package com.example.biblioteca.user

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.R
import com.example.biblioteca.RequestLogin
import com.example.biblioteca.TopBarFragment
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.LoginLayoutBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login_Fragment : Fragment() {

    private lateinit var coinImageView: ImageView
    private lateinit var loginFieldsLayout: LinearLayout
    private lateinit var binding: LoginLayoutBinding
    private lateinit var dbManager: DBManager
    var username =""
    var password = ""


    var flag=true

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
        val result = "Login"
        setFragmentResult("key", bundleOf("keyBundle" to result))
      //per cambiare searchBar

        binding.button2.setOnClickListener {
            if (binding.campoUsername.text.toString() != ""  && binding.campoPassword.text.toString() != ""){
                username = binding.campoUsername.text.toString()
                password = binding.campoPassword.text.toString()
                val loginRequestLogin = RequestLogin(username=username, password=password)
                Log.i("LOG", "chiamo la fun loginUtente passando: $loginRequestLogin ")
                loginUtente(loginRequestLogin)
            }
        }
        return binding.root
    }
    fun login(){
        val fragmentmanager=parentFragmentManager
        val transaction=fragmentmanager.beginTransaction()
        transaction.replace(R.id.fragmentSearchBar, TopBarFragment())
        transaction.replace(R.id.fragmentMain, Profile_Fragment())
        transaction.commit()
    }

    private fun loginUtente (requestLogin: RequestLogin){

        val query = "select * from persona where username = '${requestLogin.username}' and password = '${requestLogin.password}';"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.login(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    //Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        getUser((response.body()?.get("queryset")as JsonArray).get(0) as JsonObject)
                        login()
                        //Log.i("onResponse", "Sono dentro il primo if. dim response: ${(response.body()?.get("queryset") as JsonArray).size()}")
                        if ((response.body()?.get("queryset") as JsonArray).size() == 1) {
                            Log.i("onResponse", "Sono dentro il secondo if. e chiamo la getImageProfilo")
                        } else {
                            Toast.makeText(requireContext(),"credenziali errate", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    private fun getUser(jsonObject: JsonObject){
        val username=jsonObject.get("username").asString
        val nome=jsonObject.get("nome").asString
        val cognome=jsonObject.get("cognome").asString
        val qr=jsonObject.get("qr").asString
        val type=jsonObject.get("type").asString
        dbManager.insert(username,nome,cognome,qr,type) //Test db locale


    }

    private fun getImageProfilo(jsonObject: JsonObject){
        val url: String = jsonObject.get("image").asString
        ClientNetwork.retrofit.getAvatar(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        if (response.body()!=null) {
                            Log.i("LOGGATO", "LOGGATO")
                        }
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(requireContext(),"onFailure2", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

}
