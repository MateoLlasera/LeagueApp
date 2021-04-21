package com.canonicalexamples.leagueApp.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.app.LeagueAppApp
import com.canonicalexamples.leagueApp.databinding.ActivityLeagueBinding
import com.canonicalexamples.leagueApp.databinding.ActivitySummonerDisplayBinding
import com.canonicalexamples.leagueApp.model.SummonerService
import com.canonicalexamples.leagueApp.viewmodels.LeagueActivityViewModel
import com.canonicalexamples.leagueApp.viewmodels.LeagueActivityViewModelFactory
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModel
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModelFactory

class SummonerDisplayActivity : AppCompatActivity() {
    lateinit var serverHost: String
    lateinit var serverHostShort: String
    private lateinit var binding: ActivitySummonerDisplayBinding
    /*
    private val viewModel: LeagueActivityViewModel by viewModels{
        val app = application as LeagueAppApp
        LeagueActivityViewModelFactory(app.database)
    }
    */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_league)
        binding =  ActivitySummonerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.getStringExtra("serverHost").isNullOrEmpty()){

        }else{
            serverHost = intent.getStringExtra("serverHost") as String
            serverHostShort = intent.getStringExtra("serverHostShort") as String
            println("Server and host: " + serverHost + " " + serverHostShort)
        }

        /*navegar a summoner selector*/
        binding.buttonReturn.setOnClickListener {
            val intent = Intent(this, SummonerFinderActivity::class.java)
            intent.putExtra("serverHost", serverHost)
            intent.putExtra("serverHostShort", serverHostShort)
            startActivity(intent)
            //onActivityResult()
        }

        var actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}

