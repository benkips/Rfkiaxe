package com.pradytech.rafikiconnect.viewmodelz

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.pradytech.rafikiconnect.Repo.DataStoreRepository
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class Prefviewmodel @Inject constructor(private val prefrencemanger:DataStoreRepository) : ViewModel() {

    val readFromDataStore = prefrencemanger.readFromDataStore.asLiveData()

    fun saveToDataStore(email: String,type:String,tokn:String) = viewModelScope.launch  {
        prefrencemanger.saveToDataStore(email,type,tokn)
    }

    fun logout()=viewModelScope.launch  {
        prefrencemanger.clear()
    }
}