package com.canonicalexamples.leagueApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summoner_table")
data class Summoner(
    val name: String = "",
    @PrimaryKey
    val id: String = "",
    val accountId: String = "",
    val puuid: String = "",
    val summonerLevel: Long? = 0,
    val profileIconId: Int? = 0,
    var favourite: Boolean = false,
    var show: Boolean = true,
    val region: String = ""
)