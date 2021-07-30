package com.pradytech.rafikiconnect.Repo

import com.pradytech.rafikiconnect.networks.ApiInterface
import javax.inject.Inject

class Repostuff @Inject constructor(
    private val apiInterface: ApiInterface,
) : Baserepository() {
    //Log in
    suspend  fun validating(email: String,pass: String,rtype: String
    ) = safeApiCall {
        apiInterface.Login(email,pass,rtype)
    }
    //regborrower
    suspend fun borrowereg(email: String,  phone: Int, idno: Int, occupation: String,county: String,subcounty: String,rtype: String)= safeApiCall{
        apiInterface.borrowerRegstration(email, phone, idno, occupation, county, subcounty,rtype)
    }
    //reglender
    suspend fun lenderreg(email: String,  phone: Int, idno: Int, category: String,county: String,subcounty: String,rtype: String)= safeApiCall{
        apiInterface.lenderRegstration(email, phone, idno, category, county, subcounty,rtype)
    }
    //fgtpass
    suspend fun fgtpass(email:String,rtype: String)=safeApiCall {
        apiInterface.fgtpass(email,rtype)
    }


}