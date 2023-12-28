package com.example.flightsearch.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.flightsearch.NavigationDestination
import com.example.flightsearch.R
import com.example.flightsearch.model.Airport

object ResultsDestination : NavigationDestination {
    override val route: String = "searchResults"
    override val titleResource: Int = R.string.app_name
}

@Composable
fun FlightSearchResults(
    navController: NavController,
    departureAirport: Airport,
    destinationAirports: List<Airport>,
    checkIsFlightInFavorites: (String, String) -> Boolean,
    onFavoriteClick: (String, String) -> Unit,

    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier
) {
    BackHandler {
        navController.navigateUp()
    }
    Column {
        Text(
            text = stringResource(R.string.departures_from, departureAirport.name),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, start = 14.dp, bottom = 8.dp)
        )
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(6.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(destinationAirports) { destinationAirport ->
                    val isFlightFavorite = checkIsFlightInFavorites(
                        departureAirport.iataCode,
                        destinationAirport.iataCode
                    )
                    FlightCard(
                        isFlightFavorite = isFlightFavorite,
                        onFavoriteClick = onFavoriteClick,
                        departureAirportName = departureAirport.name,
                        destinationAirportName = destinationAirport.name,
                        departureAirportIata = departureAirport.iataCode,
                        destinationAirportIata = destinationAirport.iataCode,
                        interactionSource = interactionSource
                    )
                }
            }
        )
    }
}