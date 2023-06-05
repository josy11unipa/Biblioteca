package com.example.biblioteca

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import com.example.biblioteca.database.DBManager
import com.example.biblioteca.databinding.CronologiaLayoutBinding
import com.example.biblioteca.home.CustomAdapterLista
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class Cronologia_Fragment:Fragment() {
    private lateinit var binding:CronologiaLayoutBinding

    @SuppressLint("Range")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= CronologiaLayoutBinding.inflate(inflater)
        val data = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dataS=data.format(Date()).toString()

       val dbManager = DBManager(requireContext())
        dbManager.open()
        val cursor=dbManager.getUser()
        val username = cursor.getString(cursor.getColumnIndex("username"))
        getCrono(dataS,username)



        return binding.root
    }

    private fun getCrono(data:String,username:String){


        ClientNetwork.retrofit.getCronologia(data).enqueue(
            object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful) {
                        val j = (response.body()?.get("queryset") as JsonArray)
                        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                        val adapter = CustomAdapterLista(j)
                        binding.recyclerView.adapter = adapter

                        adapter.setOnClickListener(object :
                            CustomAdapterLista.OnClickListener {
                            override fun onClick(position: Int, model: JsonObject) {
                                val manager = parentFragmentManager
                                setFragmentResult(
                                    "keyId",
                                    bundleOf("keyBundleId" to model.toString())
                                )
                                val transaction = manager.beginTransaction()
                                transaction.replace(R.id.fragmentMain, Libro_Fragment())
                                transaction.addToBackStack("libri")
                                transaction.commit()
                            }
                        })
                    }
                }
            })

    }
}