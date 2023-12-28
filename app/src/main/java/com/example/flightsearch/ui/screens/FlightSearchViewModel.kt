package com.example.flightsearch.ui.screens


import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightApplication
import com.example.flightsearch.data.FlightsRepository
import com.example.flightsearch.data.UserPreferencesRepository
import com.example.flightsearch.model.Airport
import com.example.flightsearch.model.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException


data class FlightsUiState(
    val searchQuery: String = "",
    val showAutocorrect: Boolean = false,
    val favoriteFlightsList: List<Favorite> = emptyList(),
    val favoriteAirportList: List<Airport> = emptyList(),
    val airportAutocorrectList: List<Airport> = emptyList(),
    val selectedAirport: Airport = Airport(),
    val destinationList: List<Airport> = emptyList(),
    val departureAirport: Airport = Airport(),
)

class FlightSearchViewModel(
    private val flightsRepository: FlightsRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val backgroundCoroutineScope = CoroutineScope(Job() + Dispatchers.IO)

    private val _uiState = MutableStateFlow(FlightsUiState())
    val uiState: StateFlow<FlightsUiState> = _uiState

    // Airports from the Airport Table
    private var favoriteAirportList = mutableListOf<Airport>()

    private var searchAirportList = mutableListOf<Airport>()

    // Favorites from the Favorite Table
    private var favoritesFlightsList = mutableListOf<Favorite>()

    private var getAirportsJob: Job? = null

    private var isFlightFavorite: Boolean by mutableStateOf(false)

    private var hasCollected = false

    init {
        if (!hasCollected) { // check if the collection has not happened
            viewModelScope.launch {
                userPreferencesRepository.getUserSearchQuery.collect() {
                    updateUserInput(it)
                }
                hasCollected = true
            }
        }
    }

    var userInput: String by mutableStateOf(_uiState.value.searchQuery)
        private set


    fun deleteUserInput() {
        updateUserInput("")
    }

    /**
     * Updates the user input when called.
     * The function updates the userInput in the mutableState of private variable, the user preferences
     * repository (so that it can be saved when the app is closed). It also makes calls to the
     * getAirportAutocorrect function getFavoriteFlights function if the user input is empty
     */
    fun updateUserInput(input: String) {

        try {
            userInput = input.replace(
                "[^A-Za-z]".toRegex(),
                ""
            ) //this replacement stops users adding special symbols which cause bugs and also numbers because they don't need numbers.
            viewModelScope.launch {
                delay(50) //adds a delay to stop

                updateUserPreferencesSearchQuery(userInput)
                _uiState.update {
                    it.copy(
                        searchQuery = userInput, showAutocorrect = (userInput != "")
                    )
                }
                Log.i("user input", "updateUserInput: ${_uiState.value.searchQuery}")
                Log.i("user input", "updateUserInput: ${input}")

                if (input.isEmpty()) {
                    getFavoriteFlights()
                } else {
                    getAirportAutocorrect()
                }
            }
        } catch (e: IOException) {
            Log.e("IOException", "io exception")
            return
        } catch (e: Exception) {
            Log.e("Exception", "Exception of some kind")
            return
        }
    }

    /**
     * Gets the Autocorrect List from comparing the user input to the airport database and saves the
     * list of autocorrect airports to the uiState then logs the last airport added to validate.
     */
    private fun getAirportAutocorrect() {
        getAirportsJob?.cancel()
        getAirportsJob =
            flightsRepository.getDepartureAirportAutocorrect(_uiState.value.searchQuery)
                .onEach { result ->
                    _uiState.update {
                        searchAirportList = result.toMutableStateList()
                        _uiState.value.copy(airportAutocorrectList = result)
                    }
                }
                .launchIn(viewModelScope)
    }

    /**
     * Starts a coroutine to update the saved user input to the passed value
     */
    private suspend fun updateUserPreferencesSearchQuery(userInput: String) {
        backgroundCoroutineScope.launch {
            userPreferencesRepository.updateUserPreferencesSearchQuery(userInput)
        }
    }

    /**
     * This gets the favorite flights and cross references them against the airport database.
     * This would be less resource intensive if I had just retrieved an instance of the full airport database
     * to cross reference the iata code to the airport name however, the task was very specific in saying
     * not to do that.
     */
    fun getFavoriteFlights() {
        viewModelScope.launch {
            _uiState.update {
                favoritesFlightsList = flightsRepository.getFavorites().toMutableStateList()
                favoritesFlightsList.forEach { favorite ->
                    if (!favoriteAirportList.any { it.iataCode == favorite.departureCode }) {
                        favoriteAirportList.add(flightsRepository.getAirport(favorite.departureCode))
                    }
                    if (!favoriteAirportList.any { it.iataCode == favorite.destinationCode }) {
                        favoriteAirportList.add(flightsRepository.getAirport(favorite.destinationCode))
                    }
                }
                _uiState.value.copy(
                    favoriteAirportList = favoriteAirportList,
                    favoriteFlightsList = favoritesFlightsList
                )

            }
        }
    }


    init {
        getFavoriteFlights()
    }

    fun toggleFavorite(departureAirportIata: String, destinationAirportIata: String) {
        try {
            if (isFlightFavorite(departureAirportIata, destinationAirportIata)
            ) {
                removeFavorite(
                    Favorite(
                        id = favoritesFlightsList.first {
                            (it.departureCode == departureAirportIata && it.destinationCode == destinationAirportIata)
                        }.id,
                        departureCode = departureAirportIata,
                        destinationCode = destinationAirportIata
                    )
                )
                Log.i("Favorite", "toggleFavorite: removed")

            } else {
                addFavorite(
                    Favorite(
                        departureCode = departureAirportIata,
                        destinationCode = destinationAirportIata
                    )
                )
                Log.i("Favorite", "toggleFavorite: added")
            }
            getFavoriteFlights()
        } catch (e: IOException) {
            Log.i("IOException", "toggleFavorite: IO Exception")
            return
        } catch (e: NoSuchElementException) {
            Log.i("NoSuchElementException", "The favorite was toggled too quickly")
            return
        }
    }

    fun isFlightFavorite(
        departureAirportIata: String,
        destinationAirportIata: String
    ): Boolean {
        val favorite = _uiState.value.favoriteFlightsList.firstOrNull {
            it.departureCode == departureAirportIata && it.destinationCode == destinationAirportIata
        }
        isFlightFavorite = (favorite != null)
        return isFlightFavorite
    }


    private fun removeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            flightsRepository.delete(favorite)
        }
    }

    private fun addFavorite(favorite: Favorite) {
        viewModelScope.launch {
            flightsRepository.insert(favorite)
        }
    }

    fun onSelectAirport(departureAirportIata: String) {
        viewModelScope.launch {
            val departureAirport =
                flightsRepository.getDepartureAirportFlow(departureAirportIata).first()
            val destinationList =
                flightsRepository.getDestinationAirportsFor(departureAirportIata).first()
            _uiState.update {
                _uiState.value.copy(
                    selectedAirport = departureAirport,
                    destinationList = destinationList,
                    showAutocorrect = false
                )
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FlightApplication)

                FlightSearchViewModel(
                    flightsRepository = application.container.flightsRepository,
                    userPreferencesRepository = application.userPreferencesRepository
                )
            }
        }
    }
}