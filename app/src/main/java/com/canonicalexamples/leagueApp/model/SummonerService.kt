package com.canonicalexamples.leagueApp.model

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SummonerService {
    @GET("/lol/summoner/v4/summoners/by-name/{summonerName}")
    fun getSummoner(@Path(value = "summonerName") summonerName: String): Call<Summoner>
    //TODO Puedo declarar mas servicios dentro de esta interfaz?
    //TODO De esta forma puedo declarar un servicio getSummoner para cada servidor si no puedo importar el HOST
    //TODO Ademas necesito adjuntar la api_key

    @GET("/lol/summoner/v4/summoners/by-name/{summonerName}?api_key=")
    fun getSummonerAPI(@Path(value = "summonerName") summonerName: String): Call<Summoner>
}