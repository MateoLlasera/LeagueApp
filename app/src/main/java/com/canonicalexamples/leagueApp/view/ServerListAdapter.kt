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
    class ServerItemViewHolder(private val viewModel: ServerListViewModel, binding: ServerItemBinding): RecyclerView.ViewHolder(binding.root){
        //View.OnClickListener{
        val serverName = binding.buttonServer
        var serverNumber = -1
        var serverHost = ""

        init {
            //binding.root.setOnClickListener(this)
            itemView.setOnClickListener{
                val position: Int = adapterPosition
                Toast.makeText(itemView.context, "You clicked on item # ${viewModel.getItem(position).serverName}", Toast.LENGTH_SHORT).show()
                viewModel.serverButtonClicked()
            }
        }

        /*override fun onClick(p0: View?) {
            viewModel.onClickItem(layoutPosition)
        }*/
    }

    /**
     * Creacion cuando se llama, devuelve el objeto
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerItemViewHolder =
        ServerItemViewHolder(viewModel, ServerItemBinding.inflate(LayoutInflater.from(parent.context)))

    /**
     * Lo que se va a ensenar en el viewHolder
     */
    override fun onBindViewHolder(holder: ServerItemViewHolder, position: Int) {
        val item = viewModel.getItem(position)
        holder.serverName.text = item.serverName
        holder.serverNumber = item.serverId
        holder.serverHost = item.serverHost
    }

    override fun getItemCount(): Int = viewModel.numberOfItems
}