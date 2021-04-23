package com.canonicalexamples.leagueApp.viewmodels

import android.R.attr.password
import android.util.Base64
import androidx.lifecycle.*
import com.canonicalexamples.leagueApp.model.*
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
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec


class SummonerDisplayViewModel(private val database: ServerDatabase): ViewModel() {
    private var _serverSelected: MutableLiveData<String> = MutableLiveData("")
    var serverSelected: LiveData<String> = _serverSelected

    private var _serverSelectedHostShort: MutableLiveData<String> = MutableLiveData("")
    var serverSelectedHostShort: LiveData<String> = _serverSelectedHostShort

    private var _summonerName: MutableLiveData<String> = MutableLiveData("")
    var summonerName: LiveData<String> = _summonerName

    private var _dataReady: MutableLiveData<Boolean> = MutableLiveData(false)
    var dataReady: LiveData<Boolean> = _dataReady

    private var _statusUpdated: MutableLiveData<Boolean> = MutableLiveData(false)
    var statusUpdated: LiveData<Boolean> = _statusUpdated

    private val reloadList = MutableLiveData<Event<Boolean>>()

    private var summonerList: List<Summoner> = listOf()
    private lateinit var rankList: RankStatus
    private lateinit var summoner: Summoner

    init{
        viewModelScope.launch(Dispatchers.IO) {
            summonerList = database.summonerDao.fetchSummoners()
        }
    }

    fun getLevel(): Long?{
        return summoner.summonerLevel
    }

    fun getIcon(): Int?{
        return summoner.profileIconId
    }

    fun getReload(): LiveData<Event<Boolean>>{
        return reloadList
    }

    fun dataIsReady(){
        _dataReady.value = true
    }

    fun statusIsUpdated(){
        _statusUpdated.value = true
    }

    fun getSoloRank(): String?{
        /*decrypt and return content*/
        if(rankList.rankSoloIv.isNullOrEmpty()){
            return null
        }else{
            val iv: ByteArray = Base64.decode(rankList.rankSoloIv, Base64.DEFAULT)
            val text: ByteArray = Base64.decode(rankList.encryptedRankSolo, Base64.DEFAULT)
            return decryptData(iv, text)
        }
        //return rankList.rankSolo
    }

    fun getSoloTier(): String?{
        /*decrypt and return content*/
        if(rankList.tierSoloIv.isNullOrEmpty()){
            return null
        }else {
            val iv: ByteArray = Base64.decode(rankList.tierSoloIv, Base64.DEFAULT)
            val text: ByteArray = Base64.decode(rankList.encryptedTierSolo, Base64.DEFAULT)
            return decryptData(iv, text)
        }
        //return rankList.tierSolo
    }

    fun getFlexRank(): String?{
        return rankList.rankFlex
    }

    fun getFlexTier(): String?{
        return rankList.tierFlex
    }

    fun updateList(){
        viewModelScope.launch(Dispatchers.IO){
            summonerList = database.summonerDao.fetchSummoners()
            withContext(Dispatchers.Main){
                reloadList.value = Event(true)
            }
        }
    }

    fun updateRank(status: RankStatus){
        viewModelScope.launch(Dispatchers.IO){
            var auxStats = database.rankStatusDao.get(summoner.id)
            withContext(Dispatchers.Main){
                if(auxStats == null){
                    insertRank(status)
                }else{
                    editRank(status)
                }
            }
        }
    }

    fun insertRank(status: RankStatus){
        viewModelScope.launch(Dispatchers.IO){
            database.rankStatusDao.create(status)
            withContext(Dispatchers.Main){
                rankList = status
                statusIsUpdated()
            }
        }
    }

    fun editRank(status: RankStatus){
        viewModelScope.launch(Dispatchers.IO){
            database.rankStatusDao.updateRank(status)
            withContext(Dispatchers.Main){
                rankList = status
                statusIsUpdated()
            }
        }
    }

    private fun createSummonerService(serverHost: String?): SummonerService {
        val summonerservice by lazy {
            Retrofit.Builder()
                .baseUrl("https://$serverHost")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(SummonerService::class.java)
            /*create the webservice*/
        }
        return summonerservice
    }

    fun findSummonerRank(){
        val givenSummonerId: String = summoner.id
        val summonerService = createSummonerService(serverSelected.value)
        var searchSummonerRank: List<RankStatus>?
        var contained: Boolean = false
        val api = Keys.apiKey()
        summonerService.getSummonerRankAPI(givenSummonerId, api).enqueue(object :
            Callback<List<RankStatus>> {
            override fun onResponse(
                call: Call<List<RankStatus>>,
                response: Response<List<RankStatus>>
            ) {
                searchSummonerRank = response.body()
                if (response.code() == 200) {
                    if (searchSummonerRank?.isEmpty() == true) {
                        val newRank = RankStatus(id = givenSummonerId)
                        viewModelScope.launch(Dispatchers.IO) {
                            updateRank(newRank)
                            //launch(Dispatchers.Main) {

                            //}
                        }
                    } else {
                        if (searchSummonerRank?.size == 1) {
                            if (searchSummonerRank?.get(0)?.queueType.equals("RANKED_FLEX_SR")) {
                                val newRank = RankStatus(
                                    id = givenSummonerId,
                                    tierFlex = searchSummonerRank?.get(0)?.tier,
                                    rankFlex = searchSummonerRank?.get(0)?.rank
                                )
                                viewModelScope.launch(Dispatchers.IO) {
                                    updateRank(newRank)
                                    //launch(Dispatchers.Main) {

                                    //}
                                }
                            } else {
                                if (searchSummonerRank?.get(0)?.tier != null) {
                                    var pair = encryptData(searchSummonerRank?.get(0)?.tier!!)
                                    val encodedTierIV: String = Base64.encodeToString(pair.first, Base64.DEFAULT)
                                    val encodedTier: String = Base64.encodeToString(pair.second, Base64.DEFAULT)
                                    pair = encryptData(searchSummonerRank?.get(0)?.rank!!)
                                    val encodedRankIV: String = Base64.encodeToString(pair.first, Base64.DEFAULT)
                                    val encodedRank: String = Base64.encodeToString(pair.second, Base64.DEFAULT)
                                    val newRank = RankStatus(
                                        id = givenSummonerId,
                                        //tierSolo = searchSummonerRank?.get(0)?.tier,
                                        //rankSolo = searchSummonerRank?.get(0)?.rank,
                                        tierSoloIv = encodedTierIV,
                                        encryptedTierSolo = encodedTier,
                                        rankSoloIv = encodedRankIV,
                                        encryptedRankSolo = encodedRank
                                    )
                                    viewModelScope.launch(Dispatchers.IO) {
                                        updateRank(newRank)
                                        //launch(Dispatchers.Main) {

                                        //}
                                    }
                                }
                            }
                        } else {
                            var soloQ = -1
                            var flexQ = -1
                            if(searchSummonerRank?.get(0)?.queueType.equals("RANKED_FLEX_SR")){
                                soloQ = 1
                                flexQ = 0
                            }else{
                                soloQ = 0
                                flexQ = 1
                            }
                            var pair = encryptData(searchSummonerRank?.get(soloQ)?.tier!!)
                            val encodedTierIV: String = Base64.encodeToString(pair.first, Base64.DEFAULT)
                            val encodedTier: String = Base64.encodeToString(pair.second, Base64.DEFAULT)
                            pair = encryptData(searchSummonerRank?.get(soloQ)?.rank!!)
                            val encodedRankIV: String = Base64.encodeToString(pair.first, Base64.DEFAULT)
                            val encodedRank: String = Base64.encodeToString(pair.second, Base64.DEFAULT)
                            val newRank = RankStatus(
                                id = givenSummonerId,
                                //tierSolo = searchSummonerRank?.get(soloQ)?.tier,
                                tierSoloIv = encodedTierIV,
                                encryptedTierSolo = encodedTier,
                                tierFlex = searchSummonerRank?.get(flexQ)?.tier,
                                //rankSolo = searchSummonerRank?.get(soloQ)?.rank,
                                rankSoloIv = encodedRankIV,
                                encryptedRankSolo = encodedRank,
                                rankFlex = searchSummonerRank?.get(flexQ)?.rank
                            )
                            viewModelScope.launch(Dispatchers.IO) {
                                updateRank(newRank)
                                //launch(Dispatchers.Main) {

                                //}
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<List<RankStatus>>, t: Throwable) {
                t?.printStackTrace()
            }
        })
    }

    fun loadData(savedServer: String?, savedShort: String?, savedName: String?){
        _serverSelected.value = savedServer
        _serverSelectedHostShort.value = savedShort
        _summonerName.value = containsSummoner(savedName)
        /*tells the view summoner has been found*/
        dataIsReady()
    }

    fun containsSummoner(givenSummoner: String?): String?{
        var contained: String = ""
        val givenSummonerAux = givenSummoner?.replace("\\s".toRegex(), "")
        for (summo in summonerList){
            var auxName = summo.name.replace("\\s".toRegex(), "")
            if(auxName.equals(givenSummonerAux, ignoreCase = true) && summo.region == serverSelectedHostShort.value) {
                summoner = summo
                contained = summo.name
                break
            }
        }
        return contained
    }

    fun encryptData(data: String): Pair<ByteArray, ByteArray> {
        val cipher: Cipher = Cipher.getInstance("AES/CBC/NoPadding")

        var temp: String = data
        while (temp.toByteArray().size % 16 != 0)
            temp += "\u0020"

        cipher.init(Cipher.ENCRYPT_MODE, getKey())

        val ivBytes = cipher.iv

        val encryptedBytes = cipher.doFinal(temp.toByteArray())

        return Pair(ivBytes, encryptedBytes)
    }

    fun decryptData(ivBytes: ByteArray, data: ByteArray): String{
        val cipher = Cipher.getInstance("AES/CBC/NoPadding")
        val spec = IvParameterSpec(ivBytes)
        cipher.init(Cipher.DECRYPT_MODE, getKey(), spec)
        return cipher.doFinal(data).toString(Charsets.UTF_8).trim()
    }

    fun checkKey(): Boolean {
        var check = false
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        if(keystore.getEntry("MyKeyStore", null) == null){
        }else{
            //val secretKeyEntry = keystore.getEntry("MyKeyStore", null) as KeyStore.SecretKeyEntry
            check = true
        }
        //return secretKeyEntry.secretKey != null
        return check
    }
    private fun getKey(): SecretKey {
        val keystore: KeyStore = KeyStore.getInstance("AndroidKeyStore")
        keystore.load(null)
        val secretKeyEntry = keystore.getEntry("MyKeyStore", null) as KeyStore.SecretKeyEntry
        return secretKeyEntry.secretKey
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