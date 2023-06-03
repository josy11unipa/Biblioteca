package com.example.biblioteca.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.databinding.CardViewBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject


class CustomAdapterLista(private val element: JsonArray):RecyclerView.Adapter<CustomAdapterLista.ViewHolder>(){
    class ViewHolder(binding: CardViewBinding):RecyclerView.ViewHolder(binding.root){
        val titolo=binding.titleBookCard
        val autore=binding.autorBookCard
        val image=binding.bookIconCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=CardViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return element.size()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oggetto: JsonObject = element.get(position) as JsonObject
        holder.autore.text=oggetto.get("autore").asString
        holder.titolo.text=oggetto.get("titolo").asString
        //holder.genere.text=oggetto.get("genere").asString


    }

}