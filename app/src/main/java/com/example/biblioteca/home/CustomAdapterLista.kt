package com.example.biblioteca.home

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.ClientNetwork
import com.example.biblioteca.databinding.CardViewBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomAdapterLista(private val element: JsonArray):RecyclerView.Adapter<CustomAdapterLista.ViewHolder>() {
    class ViewHolder(binding: CardViewBinding) : RecyclerView.ViewHolder(binding.root) {
        //val titolo = binding.titleBookCard
        //val autore = binding.autorBookCard
        val image = binding.bookIconCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return element.size()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oggetto: JsonObject = element.get(position) as JsonObject
        //holder.autore.text = oggetto.get("autore").asString
        //holder.titolo.text = oggetto.get("titolo").asString
        val url: String = oggetto.get("copertina").asString
        ClientNetwork.retrofit.getAvatar(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            val avatar = BitmapFactory.decodeStream(response.body()?.byteStream())
                            holder.image.setImageBitmap(avatar)

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

