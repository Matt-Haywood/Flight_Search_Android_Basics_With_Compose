package com.example.flightsearch.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


class UserPreferencesRepository(
    private val dataStore: DataStore<Preferences>
) {
    private companion object {
        val userSearchQuery = stringPreferencesKey("user_search_query")
    }

    val getUserSearchQuery: Flow<String> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            Log.i("Exception", "IOException" )
        } else {
            throw exception
        }
    }.map { preferences ->
        preferences[userSearchQuery] ?: ""
    }

    suspend fun updateUserPreferencesSearchQuery(searchValue: String) {
        dataStore.edit { preferences ->
            preferences[userSearchQuery] = searchValue
        }


    }
}
