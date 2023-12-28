package com.example.flightsearch.ui.screens

import android.app.Activity
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.NavigationDestination
import com.example.flightsearch.R
import com.example.flightsearch.data.MockFlightsUiState
import com.example.flightsearch.model.Airport
import com.example.flightsearch.model.Favorite
import com.example.flightsearch.ui.theme.FlightSearchTheme

object SearchDestination : NavigationDestination {
    override val route: String = "home"
    override val titleResource: Int = R.string.app_name
}

@Composable
fun FlightSearchHome(
    flightSearchViewModel: FlightSearchViewModel,
    uiState: FlightsUiState,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier
) {
    val activity = LocalContext.current as? Activity
    BackHandler {
        activity?.finish()
    }
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.favorite_routes),
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        flightSearchViewModel.getFavoriteFlights()
        val favoriteFlightsList = uiState.favoriteFlightsList

        if (favoriteFlightsList.isNotEmpty()) {
            Favorites(
                favoriteFlightsList = favoriteFlightsList,
                airportList = uiState.favoriteAirportList,
                onFavoriteClick = { departureAirportIata: String, destinationAirportIata: String ->
                    flightSearchViewModel.toggleFavorite(
                        departureAirportIata = departureAirportIata,
                        destinationAirportIata = destinationAirportIata
                    )
                },
                isFlightFavorite = { departureAirportIata: String, destinationAirportIata: String ->
                    flightSearchViewModel.isFlightFavorite(
                        departureAirportIata = departureAirportIata,
                        destinationAirportIata = destinationAirportIata
                    )
                },
                interactionSource = interactionSource
            )
        } else {
            Text(
                text = stringResource(R.string.no_favorites),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(6.dp)
            )
        }
    }
}


@Composable
fun Favorites(
    favoriteFlightsList: List<Favorite>,
    airportList: List<Airport>,
    onFavoriteClick: (String, String) -> Unit,
    isFlightFavorite: (String, String) -> Boolean,
    interactionSource: MutableInteractionSource,
) {
    LazyColumn(modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        content = {
        items(favoriteFlightsList, key = { it.id }) { item ->
            val departureAirport =
                airportList.first { airport -> airport.iataCode == item.departureCode }
            val destinationAirport =
                airportList.first { airport -> airport.iataCode == item.destinationCode }

            FlightCard(
                isFlightFavorite = isFlightFavorite(
                    departureAirport.iataCode,
                    destinationAirport.iataCode
                ),
                onFavoriteClick = onFavoriteClick,
                departureAirportName = departureAirport.name,
                destinationAirportName = destinationAirport.name,
                departureAirportIata = departureAirport.iataCode,
                destinationAirportIata = destinationAirport.iataCode,
                interactionSource = interactionSource
            )
        }
    })
}

@Preview(showBackground = true)
@Composable
fun FavoritesPreview() {
    FlightSearchTheme {
        val interactionSource = MutableInteractionSource()
        Favorites(
            favoriteFlightsList = MockFlightsUiState.favoriteList,
            airportList = MockFlightsUiState.airportList,
            onFavoriteClick = { _: String, _: String -> },
            isFlightFavorite = { _: String, _: String -> false },
            interactionSource = interactionSource
        )
    }
}





