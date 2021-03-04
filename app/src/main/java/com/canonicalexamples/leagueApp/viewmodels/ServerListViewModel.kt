package com.canonicalexamples.leagueApp.viewmodels

import androidx.lifecycle.*
import com.canonicalexamples.leagueApp.model.Server
import com.canonicalexamples.leagueApp.model.ServerDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ServerListViewModel(private val database: ServerDatabase): ViewModel() {
    private var _navigate: MutableLiveData<Boolean> = MutableLiveData(false)
    var navigate: LiveData<Boolean> = _navigate

    //private val serverList = listOf("Europe West", "Europe Nordic", "LA North", "Korea", "Oceania", "Russia", "Japan", "Brazil", "Turkey", "North America", "LA South")
    private var serverList: List<Server> = listOf()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            serverList = database.serverDao.fecthServers()
        }
    }

    val numberOfItems: Int
        get() = serverList.count()

    data class Item(
        val serverName: String,
        val serverId: Int,
        val serverHost: String
    )

    fun getItem(n: Int) = Item(serverName = serverList[n].serverName, serverId = serverList[n].serverId, serverHost = serverList[n].serverHost)

    fun serverButtonClicked(){
            _navigate.value = true
    }

    fun onClickItem(position: Int) {
        println("Item $position clicked")
    }
}

class ServerListViewModelFactory(private val database: ServerDatabase): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServerListViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ServerListViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}