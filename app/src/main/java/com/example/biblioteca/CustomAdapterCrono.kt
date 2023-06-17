package com.example.biblioteca

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.databinding.CardViewTabellaBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject
//Custom adapter cronologia
class CustomAdapterCrono(private val element: JsonArray) :RecyclerView.Adapter<CustomAdapterCrono.ViewHolder>(){

    class ViewHolder(binding: CardViewTabellaBinding) : RecyclerView.ViewHolder(binding.root) {
        val titolo = binding.titolo
        val dataI = binding.dataI
        val dataF = binding.dataF
        val consegna = binding.consegnato
        val card=binding.cardViewT
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewTabellaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }
    override fun getItemCount(): Int {
      return  element.size()
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oggetto: JsonObject = element.get(position) as JsonObject
        Log.i("TAG-CRONO", "oggetto be like: $oggetto")
        holder.titolo.text=oggetto.get("titolo").asString
        holder.dataI.text="Data Ritiro:  "+oggetto.get("dataInizio").asString
        holder.dataF.text=oggetto.get("dataFine").asString
        if(oggetto.get("consegnato").asInt==0) {
            holder.consegna.text = "Prestito in corso"
        }else{
            holder.consegna.text="Consegnato"
        }
    }
}