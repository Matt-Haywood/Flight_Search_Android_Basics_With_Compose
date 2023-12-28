package com.example.flightsearch

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flightsearch.ui.screens.FlightSearchAutocorrect
import com.example.flightsearch.ui.screens.FlightSearchBar
import com.example.flightsearch.ui.screens.FlightSearchHome
import com.example.flightsearch.ui.screens.FlightSearchResults
import com.example.flightsearch.ui.screens.FlightSearchViewModel
import com.example.flightsearch.ui.screens.ResultsDestination
import com.example.flightsearch.ui.screens.SearchDestination

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FlightSearchApp() {
    val navController = rememberNavController()
    val flightSearchViewModel: FlightSearchViewModel =
        viewModel(factory = FlightSearchViewModel.factory)
    val uiState = flightSearchViewModel.uiState.collectAsState().value
    val interactionSource = MutableInteractionSource()
    val controller = LocalSoftwareKeyboardController.current

    Scaffold(
        modifier = Modifier,
        topBar = { FlightTopAppBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {

            FlightSearchBar(
                query = flightSearchViewModel.userInput,
                onSearchValueChange = {

                    flightSearchViewModel.updateUserInput(it)
                    if (it.isBlank()) {
                        navController.navigate(SearchDestination.route)
                    }
                },
                onTrailingIconClick = {
                    flightSearchViewModel.deleteUserInput()
                    navController.navigate(SearchDestination.route)
                },
                interactionSource = interactionSource
            )
            Spacer(modifier = Modifier.height(5.dp))
            val density = LocalDensity.current
            Box(modifier = Modifier.fillMaxSize()) {
                this@Column.AnimatedVisibility(
                    visible = uiState.showAutocorrect,
                    enter = slideInVertically {
                        with(density) { -20.dp.roundToPx() }
                    } + expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(),
                    exit = slideOutVertically {
                        with(density) { -40.dp.roundToPx() }
                    } + shrinkVertically(
                        shrinkTowards = Alignment.Bottom
                    ) + fadeOut()
                ) {
                    val airports = uiState.airportAutocorrectList
                    FlightSearchAutocorrect(
                        airportList = airports,
                        onSelectAirport = {
                            flightSearchViewModel.onSelectAirport(it)
                            Log.i("Airport Selected", "airport $it selected")
                            navController.navigate(ResultsDestination.route)
                            controller?.hide()
                        }
                    )
                }
                this@Column.AnimatedVisibility(
                    visible = !uiState.showAutocorrect,
                    enter = slideInVertically {
                        with(density) { -20.dp.roundToPx() }
                    } + expandVertically(
                        expandFrom = Alignment.Top
                    ) + fadeIn(),
                    exit = slideOutVertically {
                        with(density) { -40.dp.roundToPx() }
                    } + shrinkVertically(
                        shrinkTowards = Alignment.Top
                    ) + fadeOut()
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = SearchDestination.route,
                        modifier = Modifier.fillMaxSize()

                    ) {

                        composable(route = SearchDestination.route) {
                            FlightSearchHome(
                                flightSearchViewModel = flightSearchViewModel,
                                uiState = uiState,
                                interactionSource = interactionSource
                            )
                        }

                        composable(route = ResultsDestination.route) {
                            FlightSearchResults(
                                navController = navController,
                                departureAirport = uiState.selectedAirport,
                                destinationAirports = uiState.destinationList,
                                checkIsFlightInFavorites = { departureAirportIata: String, destinationAirportIata: String ->
                                    flightSearchViewModel.isFlightFavorite(
                                        destinationAirportIata = destinationAirportIata,
                                        departureAirportIata = departureAirportIata
                                    )
                                },
                                onFavoriteClick = { departureAirportIata: String, destinationAirportIata: String ->
                                    flightSearchViewModel.toggleFavorite(
                                        departureAirportIata = departureAirportIata,
                                        destinationAirportIata = destinationAirportIata
                                    )
                                },
                                interactionSource = interactionSource,
                                modifier = Modifier
                            )

                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightTopAppBar() {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.app_name)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.inversePrimary
        )
    )
}

interface NavigationDestination {

    val route: String

    val titleResource: Int
}