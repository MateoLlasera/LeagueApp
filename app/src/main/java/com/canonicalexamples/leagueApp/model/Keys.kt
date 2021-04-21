package com.canonicalexamples.leagueApp.model

object Keys {
    init {
        System.loadLibrary("native-lib")
    }

    external fun apiKey(): String
}