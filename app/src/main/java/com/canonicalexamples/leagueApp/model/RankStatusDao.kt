package com.canonicalexamples.leagueApp.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface RankStatusDao {
    @Insert
    suspend fun create(summonerRankStatus: RankStatus)
    @Query("SELECT * FROM RankStatus_table WHERE id = :id")
    suspend fun get(id: String): RankStatus?
    @Query("SELECT * FROM RankStatus_table")
    suspend fun fetchRanks(): List<RankStatus>
    @Update
    suspend fun updateRank(rankStatus: RankStatus)
    @Query("DELETE FROM RankStatus_table WHERE id = :id")
    suspend fun deleteRankStatus(id: String)
}