package com.canonicalexamples.leagueApp.view

import android.app.Activity
import android.content.Context
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
import com.canonicalexamples.leagueApp.viewmodels.*

class SummonerDisplayActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySummonerDisplayBinding

    private val viewModel: SummonerDisplayViewModel by viewModels{
        val app = application as LeagueAppApp
        SummonerDisplayViewModelFactory(app.database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_league)
        binding =  ActivitySummonerDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Extraigo el servidor y lo introduzco en el viewmodel*/
        loadData()

        println("*******************************************************")
        println("Server and host: " + viewModel.serverSelected.value + " " + viewModel.serverSelectedHostShort.value)
        println(viewModel.summonerName.value)

        /*Setup some view data*/

        binding.returnFinder.setOnClickListener{
            val intent = Intent(this, SummonerFinderActivity::class.java)
            startActivity(intent)
        }

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

    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedServer = sharedPreferences.getString("SERVER_KEY", null)
        val savedShort = sharedPreferences.getString("SHORT_KEY", null)
        val savedSummonerName = sharedPreferences.getString("SNAME_KEY", null)

        viewModel.loadData(savedServer, savedShort, savedSummonerName)
    }
    private fun loadSummonerName(): String?{
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedSummonerName = sharedPreferences.getString("SNAME_KEY", null)
        return savedSummonerName
    }
}

