package com.canonicalexamples.leagueApp.app

import android.app.Application
import com.canonicalexamples.leagueApp.model.Server
import com.canonicalexamples.leagueApp.model.ServerDatabase
import com.canonicalexamples.leagueApp.model.Summoner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LeagueAppApp: Application() {
    val database by lazy { ServerDatabase.getInstance(this) }
    /**fun createSummonerService(serverHost: String?): SummonerService {
        val summonerservice by lazy {
            Retrofit.Builder()
                //TODO Necesito anadir el host a la url del servicio
                .baseUrl("https://$serverHost")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build().create(SummonerService::class.java)
            /*Esto significa que la url ya no puede ser cambiada?*/
            /*create the webservice*/
        }
        return summonerservice
    }*/

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            database.clearAllTables()
            database.serverDao.create(Server(serverId = 1, serverName = "Europe West", serverHost = "euw1.api.riotgames.com", serverShort = "EUW"))
            database.serverDao.create(Server(serverId = 2, serverName = "Europe Nordic", serverHost = "eun1.api.riotgames.com", serverShort = "EUNE"))
            database.serverDao.create(Server(serverId = 3, serverName = "Turkey", serverHost = "tr1.api.riotgames.com", serverShort = "TR"))
            database.serverDao.create(Server(serverId = 4, serverName = "Russia", serverHost = "ru.api.riotgames.com", serverShort = "RU"))
            database.serverDao.create(Server(serverId = 5, serverName = "Korea", serverHost = "kr.api.riotgames.com", serverShort = "KR"))
            database.serverDao.create(Server(serverId = 6, serverName = "Japan", serverHost = "jp1.api.riotgames.com", serverShort = "JP"))
            database.serverDao.create(Server(serverId = 7, serverName = "Oceania", serverHost = "oc1.api.riotgames.com", serverShort = "OC"))
            database.serverDao.create(Server(serverId = 8, serverName = "North America", serverHost = "na1.api.riotgames.com", serverShort = "NA"))
            database.serverDao.create(Server(serverId = 9, serverName = "LA North", serverHost = "la1.api.riotgames.com", serverShort = "LAN"))
            database.serverDao.create(Server(serverId = 10, serverName = "LA South", serverHost = "la2.api.riotgames.com", serverShort = "LAS"))
            database.serverDao.create(Server(serverId = 11, serverName = "Brazil", serverHost = "br1.api.riotgames.com", serverShort = "BR"))
            database.summonerDao.create(Summoner(
                id = "cRyauCyYoalCmLwnhcW25i1VLLJZKkKv380p3kpkf7HLGCU",
                name = "deaki",
                accountId = "uhWl0anuZiv7ynlvufCj5PG0UvaldLh59u7lvA7qHOFoqQ",
                puuid = "Sb1OvbmZ4194zANj4pc7UkdtCvuXmNhOeFPU5rBAkltsjEp1i0IHqGztOYxSLH_3klc_AUB-dhr6hQ",
                profileIconId = 1211,
                summonerLevel = 313,
                region = "EUW"
            ))
        }
    }
}