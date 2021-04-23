package com.canonicalexamples.leagueApp.viewmodels

import android.content.Context
import android.os.SystemClock
import android.widget.Toast
import androidx.lifecycle.*
import com.canonicalexamples.leagueApp.model.Keys
import com.canonicalexamples.leagueApp.model.ServerDatabase
import com.canonicalexamples.leagueApp.model.Summoner
import com.canonicalexamples.leagueApp.model.SummonerService
import com.canonicalexamples.leagueApp.util.Event
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SummonerFinderViewModel(private val database: ServerDatabase): ViewModel() {
    private var _serverSelected: MutableLiveData<String> = MutableLiveData("")
    var serverSelected: LiveData<String> = _serverSelected

    private var _serverSelectedHostShort: MutableLiveData<String> = MutableLiveData("")
    var serverSelectedHostShort: LiveData<String> = _serverSelectedHostShort

    private var _auxShort: MutableLiveData<String> = MutableLiveData("")
    var auxShort: LiveData<String> = _auxShort

    private val waitDataBase = MutableLiveData<Event<Boolean>>()

    private var _navigate: MutableLiveData<Boolean> = MutableLiveData(false)
    var navigate: LiveData<Boolean> = _navigate

    private var _wrongServerOrSummoner: MutableLiveData<Boolean> = MutableLiveData(false)
    var wrongServerOrSummoner: LiveData<Boolean> = _wrongServerOrSummoner

    private var _fromRecycler: MutableLiveData<Boolean> = MutableLiveData(false)
    var fromRecycler: LiveData<Boolean> = _fromRecycler

    private var summonerList: List<Summoner> = listOf()
    private lateinit var selectedSummoner: Summoner

    init{
        viewModelScope.launch(Dispatchers.IO) {
            summonerList = database.summonerDao.fetchSummoners()
        }
    }

    fun printSummonersOnDb(){
        if(summonerList.isEmpty()){
            println("No summoners found")
        }else{
            for (summoner in summonerList){
                println(summoner.name)
            }
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

    fun goToSummoner(){
        _navigate.value = true
    }

    fun toastWrong(){
        _wrongServerOrSummoner.value = true
    }

    fun fromRecycler(){
        _fromRecycler.value = true
    }

    fun getNameAux(): String{
        return selectedSummoner.name
    }

    fun selectedSummonerRecycler(position: Int){
        selectedSummoner = summonerList[position]
        hostToName(selectedSummoner.region)
    }

    fun hostToName(serverHostString: String){
        viewModelScope.launch(Dispatchers.IO) {
            val aux = database.serverDao.getByShort(serverHostString)
            launch(Dispatchers.Main) {
                _auxShort.value = aux?.serverHost
            }
        }
    }

    fun containsSummoner(givenSummoner: String): Boolean {
        var contained: Boolean = false
        val givenSummonerAux = givenSummoner.replace("\\s".toRegex(), "")
        for (summo in summonerList){
            var auxName = summo.name.replace("\\s".toRegex(), "")
            if(auxName.equals(givenSummonerAux, ignoreCase = true) && summo.region == serverSelectedHostShort.value) {
                contained = true
                break
            }
        }
        return contained
    }

    suspend fun updateList(){
        summonerList = database.summonerDao.fetchSummoners()
    }

    fun insertSummoner(newSummoner:Summoner){
        viewModelScope.launch(Dispatchers.IO){
            database.summonerDao.create(newSummoner)
            withContext(Dispatchers.Main){
                waitDataBase.value = Event(true)
            }
        }
    }

    fun getWaitDataBase(): LiveData<Event<Boolean>>{
        return waitDataBase
    }

    fun findSummoner(givenSummoner: String){
        //TODO mostrar la informacion de un SUMMONER
        val summonerService = createSummonerService(serverSelected.value)
        var searchSummoner: Summoner?
        var contained = false
        val api = Keys.apiKey()
        summonerService.getSummonerAPI(givenSummoner, api).enqueue(object: Callback<Summoner> {
            override fun onResponse(call: Call<Summoner>?, response: Response<Summoner>?) {
                searchSummoner = response?.body()
                //println(searchSummoner)
                //println("Error Code: ${response?.code()}")
                //println("Nombre: ${response?.body()?.name}")
                //println(response?.body()?.name.toString())

                if(response?.code() != 200){
                    toastWrong()
                    println("Error Code: ${response?.code()}")
                }else{
                    val givenSummonerAux = givenSummoner.replace("\\s".toRegex(), "")
                    for (summo in summonerList){
                        var auxName = summo.name.replace("\\s".toRegex(), "")
                        if(auxName.equals(givenSummonerAux, ignoreCase = true) && summo.region == serverSelectedHostShort.value) {
                            contained = true
                            goToSummoner()
                            break
                        }
                    }
                    if(!contained){
                        val newSummoner = Summoner(searchSummoner?.name.toString(), searchSummoner?.id.toString(), searchSummoner?.accountId.toString(),
                            searchSummoner?.puuid.toString(), searchSummoner?.summonerLevel, searchSummoner?.profileIconId, region = serverSelectedHostShort.value as String)

                        viewModelScope.launch(Dispatchers.IO) {
                            insertSummoner(newSummoner)
                            updateList()
                            launch(Dispatchers.Main) {
                                //printSummonersOnDb()
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

    /* Datos para el adaptador */
    val numberOfItems: Int
        get() = summonerList.count()

    data class ItemSumm(
        val name: String,
        val serverShort: String,
        val profileIconID: Int?,
        val summonerLevel: Long?
    )

    fun getItem(n: Int) = ItemSumm(name = summonerList[n].name, serverShort = summonerList[n].region, profileIconID = summonerList[n].profileIconId, summonerLevel = summonerList[n].summonerLevel)

    fun loadData(savedServer: String?, savedShort: String?){
        _serverSelected.value = savedServer
        _serverSelectedHostShort.value = savedShort
    }
}

class SummonerFinderViewModelFactory(private val database: ServerDatabase): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SummonerFinderViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SummonerFinderViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}