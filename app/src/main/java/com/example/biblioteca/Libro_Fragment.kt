package com.example.biblioteca

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.example.biblioteca.databinding.LibroLayoutBinding
import com.example.biblioteca.home.Home_Fragment
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.Gson

import com.google.gson.JsonParser
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Libro_Fragment:Fragment() {
    private lateinit var binding: LibroLayoutBinding
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=LibroLayoutBinding.inflate(inflater)

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
}