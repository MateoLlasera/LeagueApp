package com.canonicalexamples.leagueApp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.databinding.ActivityServerPanelBinding
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModel
import com.google.android.material.snackbar.Snackbar


class ServerPanelActivity : AppCompatActivity(){

    private lateinit var binding: ActivityServerPanelBinding
    //private lateinit var viewModel: ServerListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_league)
        binding =  ActivityServerPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        viewModel = ViewModelProvider(this).get(ServerListViewModel::class.java)

        binding.recyclerViewServers.adapter = ServerListAdapter()

        binding.recyclerViewServers.setOnClickListener{
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            //viewModel.serverButtonClicked()
            val intent = Intent(this, LeagueActivity::class.java)
            startActivity(intent)
        }
        */

        /*viewModel.navigate.observe(viewLifecycleOwner){ navigate ->
            if (navigate){

            }
        }*/
        //setSupportActionBar(findViewById(R.id.toolbar))
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //actionBar?.hide()
    }
}