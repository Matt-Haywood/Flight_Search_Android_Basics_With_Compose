package com.example.flightsearch.data

import android.content.Context

interface AppContainer {
    val flightsRepository: FlightsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val flightsRepository: FlightsRepository by lazy {
        OfflineFlightsRepository(AppDatabase.getDatabase(context).flightDao())
    }
}