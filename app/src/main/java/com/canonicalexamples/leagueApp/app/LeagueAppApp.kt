package com.canonicalexamples.leagueApp.app

import android.app.Application
import com.canonicalexamples.leagueApp.model.Server
import com.canonicalexamples.leagueApp.model.ServerDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LeagueAppApp: Application() {
    val database by lazy { ServerDatabase.getInstance(this) }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            database.clearAllTables()
            database.serverDao.create(Server(serverId = 1, serverName = "Europe West", serverHost = "euw1.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 2, serverName = "Europe Nordic", serverHost = "eun1.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 3, serverName = "Turkey", serverHost = "tr1.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 4, serverName = "Russia", serverHost = "ru.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 5, serverName = "Korea", serverHost = "kr.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 6, serverName = "Japan", serverHost = "jp1.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 7, serverName = "Oceania", serverHost = "oc1.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 8, serverName = "North America", serverHost = "na1.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 9, serverName = "LA North", serverHost = "la1.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 10, serverName = "LA South", serverHost = "la2.api.riotgames.com"))
            database.serverDao.create(Server(serverId = 11, serverName = "Brazil", serverHost = "br1.api.riotgames.com"))
        }
    }
}