package com.canonicalexamples.leagueApp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ServerListViewModel: ViewModel() {
    var navigate: MutableLiveData<Boolean> = MutableLiveData(false)

    fun serverButtonClicked(){
        navigate.value = true
    }
}