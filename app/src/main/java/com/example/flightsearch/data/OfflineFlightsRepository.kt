package com.example.flightsearch.data

import com.example.flightsearch.model.Airport
import com.example.flightsearch.model.Favorite
import kotlinx.coroutines.flow.Flow

class OfflineFlightsRepository(private val flightDao: FlightDao): FlightsRepository {

    override suspend fun insert(favorite: Favorite) = flightDao.insert(favorite)

    override suspend fun delete(favorite: Favorite) = flightDao.delete(favorite)

    override fun getDepartureAirportAutocorrect(userInput: String): Flow<List<Airport>> = flightDao.getDepartureAirportAutocorrect(userInput)

    override fun getDepartureAirportFlow(userInput: String): Flow<Airport> = flightDao.getDepartureAirport(userInput)

    override suspend fun getAirport(airportIata: String): Airport = flightDao.getAirport(airportIata)

    override fun getDestinationAirportsFor(userInput: String): Flow<List<Airport>> = flightDao.getDestinationAirportsFor(userInput)

    override suspend fun getAllAirports(): List<Airport> = flightDao.getAllAirports()

    override fun getAllAirportsFlow(): Flow<List<Airport>> = flightDao.getAllAirportsFlow()

    override suspend fun getFavorites(): List<Favorite> = flightDao.getFavorites()
}