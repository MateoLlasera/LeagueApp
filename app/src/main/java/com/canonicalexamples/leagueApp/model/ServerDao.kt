package com.canonicalexamples.leagueApp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ServerDao {
    @Insert
    suspend fun create(server: Server)
    @Query("SELECT * FROM server_table WHERE serverId = :id")
    suspend fun get(id: Int): Server?
    @Query("SELECT * FROM server_table")
    suspend fun fecthServers(): List<Server>
    @Update
    suspend fun updateServer(server: Server)
    @Query("DELETE FROM server_table WHERE serverId = :id")
    suspend fun delete(id: Int)
}