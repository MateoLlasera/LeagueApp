package com.canonicalexamples.leagueApp.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.databinding.ActivityLeagueBinding

class LeagueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLeagueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_league)
        binding =  ActivityLeagueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        
        //setSupportActionBar(findViewById(R.id.toolbar))
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //actionBar?.hide()
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