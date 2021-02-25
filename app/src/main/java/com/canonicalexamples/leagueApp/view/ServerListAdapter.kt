package com.canonicalexamples.leagueApp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.leagueApp.databinding.ServerItemBinding

class ServerListAdapter: RecyclerView.Adapter<ServerListAdapter.ServerItemViewHolder>() {
    private val serverList = listOf("EUW", "EUN", "JP", "KR")

    class ServerItemViewHolder(binding: ServerItemBinding): RecyclerView.ViewHolder(binding.root) {
        val serverName = binding.serverName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerItemViewHolder =
        ServerItemViewHolder(ServerItemBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: ServerItemViewHolder, position: Int) {
        holder.serverName.text = serverList[position]
    }

    override fun getItemCount(): Int = serverList.count()
}