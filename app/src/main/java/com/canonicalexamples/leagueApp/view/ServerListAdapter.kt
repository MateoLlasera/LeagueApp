package com.canonicalexamples.leagueApp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.databinding.ServerItemBinding
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModel

class ServerListAdapter(private val viewModel: ServerListViewModel): RecyclerView.Adapter<ServerListAdapter.ServerItemViewHolder>(){

    /**
     * Variables del objeto
     */
    inner class ServerItemViewHolder(binding: ServerItemBinding): RecyclerView.ViewHolder(binding.root){
        val serverName = binding.buttonServer
        var serverNumber = -1

        init {
            itemView.setOnClickListener{ v: View ->
                val position: Int = adapterPosition
                Toast.makeText(itemView.context, "You clicked on item # ${position + 1}", Toast.LENGTH_SHORT).show()
                viewModel.serverButtonClicked()
            }
        }
    }

    /**
     * Creacion cuando se llama, devuelve el objeto
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerItemViewHolder =
        ServerItemViewHolder(ServerItemBinding.inflate(LayoutInflater.from(parent.context)))

    /**
     * Lo que se va a ensenar en el viewHolder
     */
    override fun onBindViewHolder(holder: ServerItemViewHolder, position: Int) {
        val item = viewModel.getItem(position)
        holder.serverName.text = item.serverName
        holder.serverNumber = item.serverNumber
    }

    override fun getItemCount(): Int = viewModel.numberOfItems
}