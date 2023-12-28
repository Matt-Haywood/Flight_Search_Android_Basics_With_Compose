package com.example.flightsearch.data

import com.example.flightsearch.model.Airport
import com.example.flightsearch.model.Favorite
import kotlinx.coroutines.flow.Flow

interface FlightsRepository {

    suspend fun insert(favorite: Favorite)

    suspend fun delete(favorite: Favorite)

    fun getDepartureAirportAutocorrect(userInput: String): Flow<List<Airport>>

    fun getDepartureAirportFlow(userInput: String): Flow<Airport>

    suspend fun getAirport(airportIata: String): Airport

    fun getDestinationAirportsFor(userInput: String): Flow<List<Airport>>

    suspend fun getAllAirports(): List<Airport>

    fun getAllAirportsFlow(): Flow<List<Airport>>

    suspend fun getFavorites(): List<Favorite>
}