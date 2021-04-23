package com.canonicalexamples.leagueApp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RankStatus_table")
data class RankStatus(
        @PrimaryKey
        val id: String = "",
        val queueType: String = "",
        val tier: String = "",
        val rank: String = "",
        val tierSoloIv: String? = "",
        val encryptedTierSolo: String? = "",
        val tierFlex: String? = "",
        val rankSoloIv: String? = "",
        val encryptedRankSolo: String? = "",
        val rankFlex: String? = ""
)