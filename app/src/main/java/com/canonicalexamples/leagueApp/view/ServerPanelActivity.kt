package com.canonicalexamples.leagueApp.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.databinding.ActivityServerPanelBinding
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModel


class ServerPanelActivity : AppCompatActivity(){

    private lateinit var binding: ActivityServerPanelBinding
    private lateinit var viewModel: ServerListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_league)
        binding =  ActivityServerPanelBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ServerListViewModel::class.java)

        binding.recyclerViewServers.adapter = ServerListAdapter()
        binding.recyclerViewServers.setOnClickListener{
            viewModel.serverButtonClicked()
        }
        /*
        viewModel.navigate.observe() { navigate ->
            if (navigate) {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }*/
        //setSupportActionBar(findViewById(R.id.toolbar))
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //actionBar?.hide()
    }
}