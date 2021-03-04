package com.canonicalexamples.leagueApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "server_table")
data class Server (
    @PrimaryKey(autoGenerate = true)
    val serverId: Int = 0,
    val serverName: String = "",
    val serverHost: String = ""
){
    val isValid: Boolean
        get() = serverName.isNotEmpty() && serverHost.isNotEmpty() && serverId >= 0
}