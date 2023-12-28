package com.example.flightsearch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.data.MockFlightsUiState
import com.example.flightsearch.model.Airport
import com.example.flightsearch.ui.theme.FlightSearchTheme

@Composable
fun FlightSearchAutocorrect(airportList: List<Airport>, onSelectAirport: (String) -> Unit) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        content = {
            items(airportList) { departureAirport ->
                AutocorrectListItem(
                    departureAirport.iataCode,
                    departureAirport.name,
                    onSelectAirport
                )
            }
        })

}

@Composable
fun AutocorrectListItem(
    airportIata: String,
    airportName: String,
    onSelectAirport: (String) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .clickable { onSelectAirport(airportIata) }
    ) {
        Text(
            text = "$airportIata - ",
            style = MaterialTheme.typography.titleSmall,
            fontStyle = FontStyle.Italic
        )
        Text(
            text = airportName,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFlightSearchAutocorrect() {
    FlightSearchTheme {
        val airportList = MockFlightsUiState.airportList

        FlightSearchAutocorrect(airportList, onSelectAirport = { _: String -> })
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAutocorrectListItem() {
    FlightSearchTheme {
        AutocorrectListItem(
            airportIata = "BRS",
            airportName = "Bristol Airport",
            onSelectAirport = { _: String -> })
    }
}