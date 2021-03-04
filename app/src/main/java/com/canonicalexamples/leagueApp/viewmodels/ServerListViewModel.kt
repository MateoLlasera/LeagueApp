package com.canonicalexamples.leagueApp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServerListViewModel: ViewModel() {
    private var _navigate: MutableLiveData<Boolean> = MutableLiveData(false)
    var navigate: LiveData<Boolean> = _navigate

    private val serverList = listOf("Europe West", "Europe Nordic", "LA North", "Korea", "Oceania", "Russia", "Japan", "Brazil", "Turkey", "North America", "LA South")
    val numberOfItems: Int
        get() = serverList.count()

    data class Item(
        val serverName: String,
        val serverNumber: Int
    )

    fun getItem(n: Int) = Item(serverName = serverList[n], serverNumber = n)

    fun serverButtonClicked(){
            _navigate.value = true
    }
}