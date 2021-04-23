package com.canonicalexamples.leagueApp.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.canonicalexamples.leagueApp.R
import com.canonicalexamples.leagueApp.app.LeagueAppApp
import com.canonicalexamples.leagueApp.databinding.ActivityLeagueBinding
import com.canonicalexamples.leagueApp.databinding.ActivitySummonerDisplayBinding
import com.canonicalexamples.leagueApp.model.SummonerService
import com.canonicalexamples.leagueApp.viewmodels.*
import javax.crypto.KeyGenerator

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

        /*recover newly added summoners*/
        viewModel.updateList()

        /*check if key exists*/
        if (!viewModel.checkKey()){
            val keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
            val keyGenParameterSpec = KeyGenParameterSpec
                    .Builder("MyKeyStore", KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .build()

            keyGenerator.init(keyGenParameterSpec)
            keyGenerator.generateKey()
        }

        /*wait summoner list update to load the summoner data*/
        viewModel.getReload().observe(this, Observer {
            it.getContentIfNotHandled()?.let {showSplash ->
                if(showSplash)
                    loadData()
            }
        })

        /*when everything is loaded, build ui*/
        viewModel.dataReady.observe(this) { dataReady ->
            if(dataReady){
                binding.summonerNameTag.text = viewModel.summonerName.value
                binding.summonerLevel.text = viewModel.getLevel().toString()

                val aux: String = "icon_" + viewModel.getIcon().toString()
                val drawableResId = this.resIdByName(aux, "drawable")
                binding.summonerIcon.setImageResource(drawableResId)

                viewModel.findSummonerRank()
            }
        }
        /*When rankStatus has been fetched and saved, display it*/
        viewModel.statusUpdated.observe(this) { statusUpdated ->
            if(statusUpdated){
                if(viewModel.getSoloTier().isNullOrEmpty() && viewModel.getFlexTier().isNullOrEmpty()){
                    binding.soloQueueRank.text = "UNRANKED"
                    binding.soloQueueEmblem.setImageResource(R.drawable.emblem_unranked)
                }else{
                    /*soloQ*/
                    var aux: String = "emblem_" + viewModel.getSoloTier()
                    val drawableResId = this.resIdByName(aux.toLowerCase(), "drawable")
                    binding.soloQueueEmblem.setImageResource(drawableResId)
                    aux = viewModel.getSoloTier() + " " + viewModel.getSoloRank()
                    binding.soloQueueRank.text = aux
                }

                if(viewModel.getFlexTier().isNullOrEmpty()){
                    binding.flexQueueRank.text = "UNRANKED"
                    binding.flexQueueEmblem.setImageResource(R.drawable.emblem_unranked)
                }else {
                    /*flexQ*/
                    var aux = "emblem_" + viewModel.getFlexTier()
                    val drawableResId = this.resIdByName(aux.toLowerCase(), "drawable")
                    binding.flexQueueEmblem.setImageResource(drawableResId)
                    aux = viewModel.getFlexTier() + " " + viewModel.getFlexRank()
                    binding.flexQueueRank.text = aux
                }

            }
        }

        /*Returns to finder activity*/
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
        //val auxServer = sharedPreferences.getString("AUX_SERVER_KEY", null)
        //val auxName = sharedPreferences.getString("AUX_NAME_KEY", null)

        viewModel.loadData(savedServer, savedShort, savedSummonerName)
    }

    fun Context.resIdByName(resIdName: String?, resType: String): Int {
        resIdName?.let {
            return resources.getIdentifier(it, resType, packageName)
        }
        throw Resources.NotFoundException()
    }
}

