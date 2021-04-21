package com.canonicalexamples.leagueApp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SummonerDao {
    @Insert
    suspend fun create(summoner: Summoner)
    @Query("SELECT * FROM summoner_table WHERE name = :summonerName")
    suspend fun get(summonerName: String): Summoner?
    @Query("SELECT * FROM summoner_table")
    suspend fun fetchSummoners(): List<Summoner>
    @Query("SELECT * FROM summoner_table WHERE favourite = '1'")
    suspend fun fetchFavourites(): List<Summoner>
    @Update
    suspend fun updateSummoner(summoner: Summoner)
    @Query("DELETE FROM summoner_table WHERE name = :summonerName")
    suspend fun deleteSummoner(summonerName: String)
}