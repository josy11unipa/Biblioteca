package com.example.biblioteca

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.databinding.CardViewBinding
import com.example.biblioteca.databinding.CardViewTabellaBinding
import com.example.biblioteca.home.CustomAdapterLista
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class CustomAdapterCrono(private val element: JsonArray, private val listener: OnItemClickListener) :RecyclerView.Adapter<CustomAdapterCrono.ViewHolder>(){
    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
    class ViewHolder(binding: CardViewTabellaBinding, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(binding.root) {
        val titolo = binding.titolo
        val dataI = binding.dataI
        val dataF = binding.dataF
        val consegna = binding.consegnato

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position)
                }
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewTabellaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view, listener)
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
        holder.consegna.text=oggetto.get("consegnato").asString
    }

}