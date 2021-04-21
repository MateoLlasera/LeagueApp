package com.canonicalexamples.leagueApp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.canonicalexamples.leagueApp.model.SummonerService
import com.canonicalexamples.leagueApp.viewmodels.LeagueActivityViewModel
import com.canonicalexamples.leagueApp.viewmodels.LeagueActivityViewModelFactory
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModel
import com.canonicalexamples.leagueApp.viewmodels.ServerListViewModelFactory

class LeagueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeagueBinding


    /*private val viewModel: ServerListViewModel by viewModels{
        val app = application as LeagueAppApp
        ServerListViewModelFactory(app.database, app.summonerservice)
    }*/
    private val viewModel: LeagueActivityViewModel by viewModels{
        val app = application as LeagueAppApp
        LeagueActivityViewModelFactory(app.database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_league)
        binding =  ActivityLeagueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadData()
        //deleteData()

        /*navegar a serverDisplay*/
        binding.serverDisplay.setOnClickListener{
            val intent = Intent(this, ServerPanelActivity::class.java)
            startActivityForResult(intent, 1)
            //onActivityResult()
        }

        /*Mostrar el servidor en pantalla*/
        viewModel.serverSelectedName.observe(this) {
            binding.test.text = viewModel.serverSelectedName.value
        }
        /*Actualizar datos en shared cuando cambie el hostshort*/
        viewModel.serverSelectedHostShort.observe(this){
            saveData()
        }
        /*Navegar a SummonerFinder*/
        binding.buttonTest.setOnClickListener{
            val intent = Intent(this, SummonerFinderActivity::class.java)
            //intent.putExtra("serverHost", viewModel.serverSelected.value)
            //intent.putExtra("serverHostShort", viewModel.serverSelectedHostShort.value)
            startActivity(intent)
        }
        //binding.test.text = viewModel.serverSelected.toString()
        //setSupportActionBar(findViewById(R.id.toolbar))
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //actionBar?.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            if(resultCode == Activity.RESULT_OK){
                val returnedHost = data.getSerializableExtra("host_name")
                viewModel.setSelectedServer(returnedHost as String)
                viewModel.hostToName(returnedHost)
            }else{
                //Ignore
            }
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

    private fun saveData(){
        val serverSave = viewModel.serverSelected.value
        val serverNameSave = viewModel.serverSelectedName.value
        val serverShortSave = viewModel.serverSelectedHostShort.value

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val sharedEditor = sharedPreferences.edit()
        sharedEditor.apply{
            putString("SERVER_KEY", serverSave)
            putString("NAME_KEY", serverNameSave)
            putString("SHORT_KEY", serverShortSave)
        }.apply()
    }

    private fun loadData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val savedServer = sharedPreferences.getString("SERVER_KEY", null)
        val savedName = sharedPreferences.getString("NAME_KEY", null)
        val savedShort = sharedPreferences.getString("SHORT_KEY", null)

        //viewModel.setSelectedServer(savedServer)
        //viewModel.setSelectedServerName(savedName)
        //viewModel.setSelectedServerShort(savedShort)

        /*println("LOAD DATA FROM THE ACTIVITY NOT THE VIEWMODEL")
        println("Server is " + savedServer)
        println("Name is " + savedName)
        println("Short is " + savedShort)
        println("DATA SENT TO THE VIEWMODEL")*/
        viewModel.loadData(savedServer,savedName,savedShort)
    }

    private fun deleteData(){
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().commit()
    }

}
