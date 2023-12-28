package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.flightsearch.model.Airport
import com.example.flightsearch.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: Favorite)

    @Delete
    suspend fun delete(favorite: Favorite)

    @Query("SELECT * FROM airport WHERE name LIKE '%' || :userInput || '%' OR iata_code LIKE '%' || :userInput || '%' ORDER BY iata_code ASC")
    fun getDepartureAirportAutocorrect(userInput: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport WHERE iata_code LIKE :userInput")
    fun getDepartureAirport(userInput: String): Flow<Airport>

    @Query("SELECT * FROM airport WHERE iata_code IS :airportIata")
    suspend fun getAirport(airportIata: String): Airport

    @Query("SELECT * FROM airport WHERE NOT iata_code = :userInput ORDER BY iata_code ASC" )
    fun getDestinationAirportsFor(userInput: String): Flow<List<Airport>>

    @Query("SELECT * FROM airport")
    suspend fun getAllAirports(): List<Airport>

    @Query("SELECT * FROM airport")
    fun getAllAirportsFlow(): Flow<List<Airport>>

    @Query("SELECT * FROM favorite")
    suspend fun getFavorites(): List<Favorite>

//    @Query("SELECT EXISTS (SELECT 1 FROM favorite WHERE departure_code = :iataDeparture AND destination_code = :iataArrival)")
//    fun isFlightInFavorites(iataDeparture: String, iataArrival: String): Flow<Boolean>
}