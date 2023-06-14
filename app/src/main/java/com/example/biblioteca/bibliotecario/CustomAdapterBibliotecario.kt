package com.example.biblioteca.bibliotecario

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.CustomAdapterPrenotazione
import com.example.biblioteca.databinding.CardViewBibliotecarioBinding
import com.example.biblioteca.databinding.CardViewTabellaBinding
import com.google.gson.JsonArray
import com.google.gson.JsonObject

class CustomAdapterBibliotecario(private val element:JsonArray) :
    RecyclerView.Adapter<CustomAdapterBibliotecario.ViewHolder>() {


    class ViewHolder(binding: CardViewBibliotecarioBinding) : RecyclerView.ViewHolder(binding.root) {
        val titolo = binding.titolo
        val nome=binding.nome
        val codice=binding.codice



    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = CardViewBibliotecarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }



    override fun getItemCount(): Int {
        return  element.size()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val oggetto: JsonObject = element.get(position) as JsonObject
        holder.titolo.text="Titolo: "+oggetto.get("titolo").asString
        val nome=oggetto.get("nome").asString
        val cognome=oggetto.get("cognome").asString
        holder.nome.text="Utente: "+nome +" "+cognome
        holder.codice.text="Codice: "+oggetto.get("codeConsegna").asString

    }


}
