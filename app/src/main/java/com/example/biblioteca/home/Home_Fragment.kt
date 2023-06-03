package com.example.biblioteca.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.RequestLogin
import com.example.biblioteca.databinding.HomeLayoutBinding
import com.example.biblioteca.databinding.InfoLayoutBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Home_Fragment(): Fragment() {
    private lateinit var binding: HomeLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomeLayoutBinding.inflate(inflater)
        getbook()
        return binding.root
    }
    private fun getbook () {

        val query =
            "select * from libro;"
        Log.i("LOG", "Query creata:$query ")

        ClientNetwork.retrofit.getLibri(query).enqueue(
            object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    //Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        val j=(response.body()?.get("queryset")as JsonArray)
                        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                        binding.recyclerView.adapter = CustomAdapterLista(j)
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

}