package com.pradytech.rafikiconnect.viewmodelz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pradytech.rafikiconnect.Repo.Repostuff
import com.pradytech.rafikiconnect.models.Resultstuff
import com.pradytech.rafikiconnect.networks.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel
import retrofit2.http.Field

@HiltViewModel
class Loginviewmodel @Inject constructor(private val repostuff: Repostuff) :
    ViewModel() {

    private val _notiResponse: MutableLiveData<Resource<Resultstuff>> = MutableLiveData()
    val notiResponse: LiveData<Resource<Resultstuff>>
        get() = _notiResponse
   //login
    fun validatenow(
        email: String, pass: String,rtype: String
    ) = viewModelScope.launch {
        _notiResponse.value = Resource.Loading
        _notiResponse.value= repostuff.validating(email,pass,rtype)
    }
    //borrowerreg
    fun breg(email: String, phone: Int, idno: Int, occupation: String,county: String,subcounty: String,rtype: String)=
        viewModelScope.launch {
            _notiResponse.value = Resource.Loading
            _notiResponse.value= repostuff.borrowereg(email, phone, idno, occupation, county, subcounty,rtype)
        }
    //lenderreg
    fun lreg(email: String, phone: Int, idno: Int, category: String,county: String,subcounty: String,rtype: String)=
        viewModelScope.launch {
            _notiResponse.value = Resource.Loading
            _notiResponse.value= repostuff.lenderreg(email, phone, idno, category, county, subcounty,rtype)
        }
    //forgotpass
    fun fgtpas(email:String,rtype: String){
        viewModelScope.launch {
            _notiResponse.value = Resource.Loading
            _notiResponse.value= repostuff.fgtpass(email,rtype)
        }
    }
}