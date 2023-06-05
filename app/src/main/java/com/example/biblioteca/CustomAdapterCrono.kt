package com.example.biblioteca

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.databinding.CardViewBinding
import com.example.biblioteca.databinding.CardViewTabellaBinding
import com.example.biblioteca.home.CustomAdapterLista
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class CustomAdapterCrono(private val element: JsonArray) :RecyclerView.Adapter<CustomAdapterCrono.ViewHolder>(){
    class ViewHolder(binding: CardViewTabellaBinding) : RecyclerView.ViewHolder(binding.root) {
        val titolo=binding.titolo
        val dataI=binding.dataI
        val dataF=binding.dataF
        val consegna=binding.consegnato

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewTabellaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomAdapterCrono.ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return  element.size()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oggetto: JsonObject = element.get(position) as JsonObject
        holder.titolo.text=oggetto.get("titolo").asString
        holder.dataI.text=oggetto.get("dataInizio").asString
        holder.dataF.text=oggetto.get("dataFine").asString
        holder.consegna.text=oggetto.get("consegnato").asString
    }

}