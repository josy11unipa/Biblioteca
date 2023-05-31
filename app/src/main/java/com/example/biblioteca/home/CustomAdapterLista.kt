package com.example.biblioteca.home

import android.database.Cursor
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.biblioteca.databinding.CardViewBinding

class CustomAdapterLista(private val cursor: Cursor):RecyclerView.Adapter<CustomAdapterLista.ViewHolder>(){
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
        return cursor.count
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("da implementare")
    }

}