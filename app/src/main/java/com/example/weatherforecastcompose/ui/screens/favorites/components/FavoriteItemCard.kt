package com.example.weatherforecastcompose.ui.screens.favorites.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.WeatherType
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme

@Composable
fun FavoriteItemCard(
    currentWeather: CurrentWeather,
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 5.dp,
                bottom = 5.dp,
                end = 20.dp,
            ),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp, top = 10.dp),
            ) {
                Row {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = stringResource(id = R.string.image_content_description_city)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = currentWeather.city, fontSize = 22.sp)
                }
                Row(modifier = Modifier.padding(top = 6.dp)) {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = stringResource(id = R.string.image_content_description_date)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = currentWeather.data)
                }
                Row(Modifier.padding(top = 10.dp)) {
                    Text(
                        text = currentWeather.description,
                        fontSize = 18.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = currentWeather.temperature, fontSize = 54.sp)
            }
            Icon(
                modifier = Modifier.size(180.dp),
                painter = painterResource(
                    currentWeather.icon.iconResId
                ),
                contentDescription = currentWeather.description,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun FavoritesItemPreview() {
    WeatherForecastComposeTheme {
        FavoriteItemCard(
            currentWeather = CurrentWeather(
                id = 0,
                coordinates = Coordinates(
                    lon = "139.6917",
                    lat = "35.6895"
                ),
                city = "Moscow",
                country = "RU",
                temperature = "22°C",
                icon = WeatherType.IC_02D,
                description = "cloudy ",
                feelsLike = "24°C",
                humidity = "44%",
                pressure = "1002hPa",
                windSpeed = "7m/c",
                data = "Wed,19 June 15:54",
                timezone = 0,
            ),
        )
    }
}