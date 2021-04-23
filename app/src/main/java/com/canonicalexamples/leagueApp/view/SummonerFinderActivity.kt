package com.canonicalexamples.leagueApp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.app.LeagueAppApp
import com.canonicalexamples.leagueApp.databinding.ActivitySummonerFinderBinding
import com.canonicalexamples.leagueApp.viewmodels.SummonerFinderViewModel
import com.canonicalexamples.leagueApp.viewmodels.SummonerFinderViewModelFactory

class SummonerFinderActivity: AppCompatActivity(){
    private lateinit var binding: ActivitySummonerFinderBinding

    private val viewModel: SummonerFinderViewModel by viewModels{
        val app = application as LeagueAppApp
        SummonerFinderViewModelFactory(app.database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =  ActivitySummonerFinderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Extraigo el servidor y lo introduzco en el viewmodel*/
        loadData()
        //println("*******************************************************")
        //println("Server and host: " + viewModel.serverSelected.value + " " + viewModel.serverSelectedHostShort.value)

        viewModel.getWaitDataBase().observe(this, Observer {
            it.getContentIfNotHandled()?.let {showSplash ->
                if(showSplash != null && showSplash) {
                    val intent = Intent(this, SummonerDisplayActivity::class.java)
                    startActivity(intent)
                }
            }
        })

        viewModel.navigate.observe(this){ navigate ->
            if(navigate){
                val intent = Intent(this, SummonerDisplayActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.fromRecycler.observe(this){ fromRecycler ->
            if(fromRecycler){
                saveDataRecycler(viewModel.auxShort.value, viewModel.getNameAux())
                val intent = Intent(this, SummonerDisplayActivity::class.java)
                startActivity(intent)
            }
        }

        viewModel.wrongServerOrSummoner.observe(this){ wrongServerOrSummoner ->
            if(wrongServerOrSummoner){
                Toast.makeText(applicationContext, "Summoner does not exist / wrong server selected", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.nav_menu, menu)

        val search = menu?.findItem(R.id.nav_search)
        val searchView = search?.actionView as SearchView
        searchView.queryHint = "Search a summoner..."
        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                /*llamar a la API*/
                if (!p0.isNullOrEmpty()) {
                    saveSummonerName(p0)
                    viewModel.findSummoner(p0)
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })
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

        viewModel.loadData(savedServer, savedShort)

        sharedPreferences.edit().remove("SNAME_KEY").apply()
        sharedPreferences.edit().remove("AUX_SERVER_KEY").apply()
        sharedPreferences.edit().remove("AUX_NAME_KEY").apply()
        /*
        println("Server is " + savedServer)
        println("Name is " + savedName)
        println("Short is " + savedShort)
        */
    }
    private fun saveSummonerName(summonerName: String?){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val sharedEditor = sharedPreferences.edit()
        sharedEditor.apply{
            putString("SNAME_KEY", summonerName)
        }.apply()
    }
    private fun loadSummonerName(): String?{
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedSummonerName = sharedPreferences.getString("SNAME_KEY", null)
        return savedSummonerName
    }

    private fun saveDataRecycler(serverHost: String?, summonerName: String?){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val sharedEditor = sharedPreferences.edit()
        sharedEditor.apply{
            putString("AUX_SERVER_KEY", serverHost)
            putString("AUX_NAME_KEY", summonerName)
        }.apply()
    }
}