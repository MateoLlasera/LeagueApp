package com.canonicalexamples.leagueApp.view

import android.media.tv.TvContentRating
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.databinding.SummonerLayoutBinding
import com.canonicalexamples.leagueApp.viewmodels.SummonerFinderViewModel

class SummonerListAdapter(private val viewModel: SummonerFinderViewModel): RecyclerView.Adapter<SummonerListAdapter.SummonerItemViewHolder>() {

    class SummonerItemViewHolder(private val viewModel: SummonerFinderViewModel, binding: SummonerLayoutBinding): RecyclerView.ViewHolder(binding.root){
        val summonerName: TextView = binding.summonerName
        val summonerDesc: TextView = binding.summonerDesc
        val summonerIcon: ImageView = binding.summonerIcon

        init {
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummonerListAdapter.SummonerItemViewHolder =
            SummonerListAdapter.SummonerItemViewHolder(viewModel, SummonerLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: SummonerListAdapter.SummonerItemViewHolder, position: Int) {
        val item = viewModel.getItem(position)
        holder.summonerName.text = item.name
        holder.summonerDesc.text = "testest"
        holder.summonerIcon.setImageResource(R.mipmap.icon_0)
    }

    override fun getItemCount(): Int = viewModel.numberOfItems
}