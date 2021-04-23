package com.canonicalexamples.leagueApp.view

import android.graphics.drawable.Drawable
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.databinding.SummonerLayoutBinding
import com.canonicalexamples.leagueApp.viewmodels.SummonerFinderViewModel

class SummonerListAdapter(private val viewModel: SummonerFinderViewModel, private val context: Context?): RecyclerView.Adapter<SummonerListAdapter.SummonerItemViewHolder>() {

    class SummonerItemViewHolder(private val viewModel: SummonerFinderViewModel, binding: SummonerLayoutBinding): RecyclerView.ViewHolder(binding.root){
        val summonerName: TextView = binding.summonerName
        val summonerDesc: TextView = binding.summonerDesc
        val summonerIcon: ImageView = binding.summonerIcon
        val summonerLevel: TextView = binding.summonerLevel

        init {
            binding.goNext.setOnClickListener{
                val position: Int = adapterPosition
                Toast.makeText(itemView.context, "You clicked on item # ${viewModel.getItem(position).name}", Toast.LENGTH_SHORT).show()
                viewModel.selectedSummonerRecycler(position)
                viewModel.fromRecycler()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummonerListAdapter.SummonerItemViewHolder =
            SummonerListAdapter.SummonerItemViewHolder(viewModel, SummonerLayoutBinding.inflate(LayoutInflater.from(parent.context)))

    override fun onBindViewHolder(holder: SummonerListAdapter.SummonerItemViewHolder, position: Int) {
        val item = viewModel.getItem(position)
        val auxString = item.name + " - " + item.serverShort
        holder.summonerName.text = auxString
        //holder.summonerDesc.text = "testest"
        val aux: String = "icon_" + item.profileIconID
        val drawableResId = context?.resIdByName(aux, "drawable")
        if (drawableResId != null) {
            holder.summonerIcon.setImageResource(drawableResId)
        }
        holder.summonerLevel.text = item.summonerLevel.toString()


    }

    override fun getItemCount(): Int = viewModel.numberOfItems

    fun Context.resIdByName(resIdName: String?, resType: String): Int {
        resIdName?.let {
            return resources.getIdentifier(it, resType, packageName)
        }
        throw Resources.NotFoundException()
    }
}