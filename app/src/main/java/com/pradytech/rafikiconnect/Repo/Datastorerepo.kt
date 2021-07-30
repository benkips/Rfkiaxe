package com.pradytech.rafikiconnect.Repo

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

const val PREFERENCE_NAME = "myprefrence"
data class  Filterpreferences(val  em: String,val typ:String,val tkn:String)
@Singleton
class DataStoreRepository @Inject constructor(@ApplicationContext context: Context)  {

    private object PreferenceKeys {
        val email = preferencesKey<String>("email")
        val type = preferencesKey<String>("type")
        val tkn = preferencesKey<String>("token")
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCE_NAME
    )

    suspend fun saveToDataStore(email: String,pass:String,tokn:String){
        dataStore.edit { preference ->
            preference[PreferenceKeys.email] = email
            preference[PreferenceKeys.type]=pass
            preference[PreferenceKeys.tkn]=tokn

        }
    }

    val readFromDataStore= dataStore.data
        .catch { exception ->
            if(exception is IOException){
                Log.d("DataStore", exception.message.toString())
                emit(emptyPreferences())
            }else {
                throw exception
            }
        }
        .map { preference ->
            val email = preference[PreferenceKeys.email] ?: "none"
            val type = preference[PreferenceKeys.type] ?: "none"
            val tokn = preference[PreferenceKeys.tkn] ?: "none"

            Filterpreferences(email,type,tokn)
        }


    suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}