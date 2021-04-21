package com.canonicalexamples.leagueApp.viewmodels

import androidx.lifecycle.*
import com.canonicalexamples.leagueApp.model.Server
import com.canonicalexamples.leagueApp.model.ServerDatabase
import com.canonicalexamples.leagueApp.model.SummonerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.await

class ServerListViewModel(private val database: ServerDatabase): ViewModel() {
    private var _serverSelected: MutableLiveData<String> = MutableLiveData("")
    var serverSelected: LiveData<String> = _serverSelected
    private var _navigate: MutableLiveData<Boolean> = MutableLiveData(false)
    var navigate: LiveData<Boolean> = _navigate

    private var serverList: List<Server> = listOf()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            serverList = database.serverDao.fetchServers()
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

    fun setSelectedServer(serverHostString: String){
        _serverSelected.value = serverHostString
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