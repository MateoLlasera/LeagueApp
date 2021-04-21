package com.canonicalexamples.leagueApp.viewmodels

import androidx.lifecycle.*
import com.canonicalexamples.leagueApp.model.ServerDatabase
import com.canonicalexamples.leagueApp.model.Summoner
import com.canonicalexamples.leagueApp.model.SummonerService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class LeagueActivityViewModel(private val database: ServerDatabase): ViewModel() {
    private var _serverSelected: MutableLiveData<String> = MutableLiveData("")
    var serverSelected: LiveData<String> = _serverSelected
    private var _serverSelectedName: MutableLiveData<String> = MutableLiveData("")
    var serverSelectedName: LiveData<String> = _serverSelectedName
    private var _serverSelectedHostShort: MutableLiveData<String> = MutableLiveData("")
    var serverSelectedHostShort: LiveData<String> = _serverSelectedHostShort

    private var control: String? = ""

    init{
        viewModelScope.launch(Dispatchers.IO) {
            val temporalHost = database.serverDao.get(1)?.serverHost
            val temporalHostName = database.serverDao.get(1)?.serverName
            val temporalHostShort = database.serverDao.get(1)?.serverShort
            launch(Dispatchers.Main) {
                if(control.isNullOrEmpty()){
                    _serverSelected.value = temporalHost
                    _serverSelectedName.value = temporalHostName
                    _serverSelectedHostShort.value = temporalHostShort
                }
            }
        }
    }

    fun loadData(savedServer: String?, savedName: String?, savedShort: String?){
        control = savedName

        _serverSelected.value = savedServer
        _serverSelectedName.value = savedName
        _serverSelectedHostShort.value = savedShort
        /*
        println("Server is " + savedServer)
        println("Name is " + savedName)
        println("Short is " + savedShort)
        */
    }

    fun setSelectedServer(serverHostString: String?){
        _serverSelected.value = serverHostString
        //println(_serverSelected.value)
    }

    fun hostToName(serverHostString: String){
        viewModelScope.launch(Dispatchers.IO) {
            val aux = database.serverDao.getByHost(serverHostString)
            launch(Dispatchers.Main) {
                if (aux != null) {
                    _serverSelectedName.value = aux.serverName
                    _serverSelectedHostShort.value = aux.serverShort
                }
            }
        }
    }
}

class LeagueActivityViewModelFactory(private val database: ServerDatabase): ViewModelProvider.Factory{
    //, private val summonerservice: SummonerService
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeagueActivityViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return LeagueActivityViewModel(database) as T
            //return ServerListViewModel(database, summonerservice) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}