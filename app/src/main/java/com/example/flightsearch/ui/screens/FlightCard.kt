package com.example.flightsearch.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.flightsearch.R
import com.example.flightsearch.model.Airport
import com.example.flightsearch.ui.theme.FlightSearchTheme


@Composable
fun FlightCard(
    isFlightFavorite: Boolean,
    onFavoriteClick: (String, String) -> Unit,
    departureAirportName: String,
    destinationAirportName: String,
    departureAirportIata: String,
    destinationAirportIata: String,
    interactionSource: MutableInteractionSource,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.large,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            disabledContainerColor = MaterialTheme.colorScheme.background,
            disabledContentColor = MaterialTheme.colorScheme.error
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.depart),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    Text(
                        text = "$departureAirportIata - ",
                        style = MaterialTheme.typography.titleSmall,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "$departureAirportName",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Text(
                    text = stringResource(R.string.arrive),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 10.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp)
                ) {
                    Text(
                        text = "$destinationAirportIata - ",
                        style = MaterialTheme.typography.titleSmall,
                        fontStyle = FontStyle.Italic
                    )
                    Text(
                        text = "$destinationAirportName",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                IconButton(onClick = {onFavoriteClick(departureAirportIata, destinationAirportIata)}) {
                    Icon(
                        painterResource(R.drawable.baseline_star_24),
                        contentDescription = null,
                        modifier = Modifier.padding(4.dp),
                        tint = if (isFlightFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightCardFavoritePreview() {
    FlightSearchTheme {
        val interactionSource = MutableInteractionSource()
        val fakeAirport = Airport(
            id = 1,
            iataCode = "BRS",
            name = "Bristol Airport haidflakjdf;lkej;lakjdf;lkhahadhfhehahfdhahdfhe",
            passengers = 1000000
        )
        val fakeAirport1 = Airport(
            id = 1,
            iataCode = "CNT",
            name = "Big Flappy Airport",
            passengers = 1000000
        )
        FlightCard(
            isFlightFavorite = true,
            onFavoriteClick = { _: String, _: String -> },
            departureAirportName = fakeAirport.name,
            destinationAirportName = fakeAirport1.name,
            departureAirportIata = fakeAirport.iataCode,
            destinationAirportIata = fakeAirport1.iataCode,
            interactionSource = interactionSource
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FlightCardPreview() {
    FlightSearchTheme {
        val interactionSource = MutableInteractionSource()
        val fakeAirport = Airport(
            id = 1,
            iataCode = "BRS",
            name = "Bristol Airport haidflakjdf;lkej;lakjdf;lkhahadhfhehahfdhahdfhe",
            passengers = 1000000
        )
        val fakeAirport1 = Airport(
            id = 1,
            iataCode = "CNT",
            name = "Big Flappy Airport",
            passengers = 1000000
        )
        FlightCard(
            isFlightFavorite = false,
            onFavoriteClick = { _: String, _: String -> },
            departureAirportName = fakeAirport.name,
            destinationAirportName = fakeAirport1.name,
            departureAirportIata = fakeAirport.iataCode,
            destinationAirportIata = fakeAirport1.iataCode,
            interactionSource = interactionSource
        )
    }
}