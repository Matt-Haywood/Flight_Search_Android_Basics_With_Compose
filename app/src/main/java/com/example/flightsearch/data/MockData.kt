package com.example.flightsearch.data

import com.example.flightsearch.data.MockData.airport1
import com.example.flightsearch.data.MockData.mockDataAirportList
import com.example.flightsearch.data.MockData.mockDataFavoriteList
import com.example.flightsearch.model.Airport
import com.example.flightsearch.model.Favorite

object MockFlightsUiState {
    val searchQuery: String = "LHR"
    val favoriteList: List<Favorite> = mockDataFavoriteList
    val airportList: List<Airport> = mockDataAirportList
    val selectedAirportIata: String = "LHR"
    val destinationList: List<Airport> = listOf(MockData.airport2, MockData.airport3)
    val departureAirport: Airport = airport1
}

object MockData {
    val airport1 = Airport(
        id = 1,
        iataCode = "LHR",
        name = "London Heathrow Airport",
        passengers = 80000
    )

    val airport2 = Airport(
        id = 2,
        iataCode = "JFK",
        name = "John F. Kennedy International Airport",
        passengers = 90000
    )

    val airport3 = Airport(
        id = 3,
        iataCode = "HND",
        name = "Tokyo Haneda Airport",
        passengers = 95000
    )

    val mockDataAirportList: List<Airport> = listOf(airport1,airport2,airport3)

    val mockDataFavoriteList = listOf(
        Favorite(
            id = 1,
            departureCode = "LHR",
            destinationCode = "JFK"
        ),
        Favorite(
            id = 2,
            departureCode = "JFK",
            destinationCode = "HND"
        ),
        Favorite(
            id = 3,
            departureCode = "HND",
            destinationCode = "LHR"
        )
    )

}