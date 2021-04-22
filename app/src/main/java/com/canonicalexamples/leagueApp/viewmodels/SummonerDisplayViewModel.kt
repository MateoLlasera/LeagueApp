package com.canonicalexamples.leagueApp.viewmodels

import androidx.lifecycle.*
import com.canonicalexamples.leagueApp.model.Keys
import com.canonicalexamples.leagueApp.model.ServerDatabase
import com.canonicalexamples.leagueApp.model.Summoner
import com.canonicalexamples.leagueApp.model.SummonerService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SummonerDisplayViewModel(private val database: ServerDatabase): ViewModel() {
    private var _serverSelected: MutableLiveData<String> = MutableLiveData("")
    var serverSelected: LiveData<String> = _serverSelected

    private var _serverSelectedHostShort: MutableLiveData<String> = MutableLiveData("")
    var serverSelectedHostShort: LiveData<String> = _serverSelectedHostShort

    private var _summonerName: MutableLiveData<String> = MutableLiveData("")
    var summonerName: LiveData<String> = _summonerName

    private var summonerList: List<Summoner> = listOf()

    init{
        viewModelScope.launch(Dispatchers.IO) {
            summonerList = database.summonerDao.fetchSummoners()
        }
    }

    private fun createSummonerService(serverHost: String?): SummonerService {
        val summonerservice by lazy {
            Retrofit.Builder()
                //TODO Necesito anadir el host a la url del servicio
                .baseUrl("https://$serverHost")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(SummonerService::class.java)
            /*create the webservice*/
        }
        return summonerservice
    }

    fun findSummoner(givenSummoner: String){
        //TODO mostrar la informacion de un SUMMONER
        val summonerService = createSummonerService(serverSelected.value)
        var searchSummoner: Summoner?
        var contained: Boolean = false
        val API = Keys.apiKey()
        summonerService.getSummonerAPI(givenSummoner, API).enqueue(object: Callback<Summoner> {
            override fun onResponse(call: Call<Summoner>?, response: Response<Summoner>?) {
                searchSummoner = response?.body()
                //println(searchSummoner)
                //println("Error Code: ${response?.code()}")
                //println("Nombre: ${response?.body()?.name}")
                //println(response?.body()?.name.toString())

                if(response?.code() != 200){
                    println("Error Code: ${response?.code()}")
                }else{
                    val givenSummonerAux = givenSummoner.replace("\\s".toRegex(), "")
                    for (summo in summonerList){
                        var auxName = summo.name.replace("\\s".toRegex(), "")
                        if(auxName.equals(givenSummonerAux, ignoreCase = true) && summo.region == serverSelectedHostShort.value) {
                            contained = true
                            break
                        }
                    }
                    if(!contained){
                        val newSummoner = Summoner(searchSummoner?.name.toString(), searchSummoner?.id.toString(), searchSummoner?.accountId.toString(),
                            searchSummoner?.puuid.toString(), searchSummoner?.summonerLevel, searchSummoner?.profileIconId, region = serverSelectedHostShort.value as String)

                        viewModelScope.launch(Dispatchers.IO) {
                            database.summonerDao.create(newSummoner)
                            launch(Dispatchers.Main) {

                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<Summoner>, t: Throwable) {
                t?.printStackTrace()
            }
        })
    }

    fun loadData(savedServer: String?, savedShort: String?, savedName: String?){
        _serverSelected.value = savedServer
        _serverSelectedHostShort.value = savedShort
        _summonerName.value = savedName
    }
}

class SummonerDisplayViewModelFactory(private val database: ServerDatabase): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummonerDisplayViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SummonerDisplayViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}