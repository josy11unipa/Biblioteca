package com.example.biblioteca.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.Libro_Fragment
import com.example.biblioteca.R
import com.example.biblioteca.databinding.HomeLayoutBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Home_Fragment(): Fragment() {
    private lateinit var binding: HomeLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var query="Select * from libro order by titolo;"
        binding = HomeLayoutBinding.inflate(inflater)

        binding.searchBar.setOnClickListener{
            binding.searchBar.isIconified = false
        }

        binding.searchBar.setOnQueryTextListener (object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(parola: String?): Boolean {
                val query =
                    "select * from libro where autore LIKE '%$parola%' " +
                            "or titolo LIKE '%$parola%' " +
                            "or anno LIKE'%$parola%' " +
                            "or genere LIKE'$parola';"
                getbook(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        getbook(query)
        return binding.root
    }
    private fun getbook (query:String) {
        ClientNetwork.retrofit.getLibri(query).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    //Log.i("onResponse", "Sono dentro la onResponse e l'esito sarà: ${response.isSuccessful}")
                    if (response.isSuccessful) {
                        val j=(response.body()?.get("queryset")as JsonArray)
                        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
                        val adapter= CustomAdapterLista(j)
                        binding.recyclerView.adapter=adapter

                        adapter.setOnClickListener(object:
                            CustomAdapterLista.OnClickListener {
                            override fun onClick(position: Int, model: JsonObject) {
                                val manager=parentFragmentManager
                                setFragmentResult("keyId", bundleOf("keyBundleId" to model.toString() ))
                                val transaction=manager.beginTransaction()
                                transaction.replace(R.id.fragmentMain,Libro_Fragment())
                                transaction.addToBackStack("libri")
                                transaction.commit()
                            }
                        })
                    }
                }
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }


}