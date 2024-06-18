package com.example.weatherforecastcompose.ui.screens.favorites.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.Const
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.model.Coordinates
import com.example.weatherforecastcompose.model.CurrentWeather
import com.example.weatherforecastcompose.model.FavoriteCoordinates
import com.example.weatherforecastcompose.model.WeatherType
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme

@Composable
fun FavoriteItemCard(
    currentWeather: CurrentWeather,
    isFavorite: Boolean,
    onFavoriteIconClick: (favoritesCoordinates: FavoriteCoordinates, isFavorite: Boolean) -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                top = 10.dp,
                end = 20.dp,
            ),
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.surface,
//        ),
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        painter = painterResource(id = R.drawable.ic_location),
                        contentDescription = stringResource(id = R.string.image_content_description_city)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = currentWeather.city)
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
                        fontSize = 22.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(text = currentWeather.temperature, fontSize = 54.sp)
            }
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.End,
            ) {
                Icon(
                    modifier = Modifier.size(140.dp),
//                    tint = MaterialTheme.colorScheme.onSurface,
                    painter = painterResource(
                        currentWeather.icon.iconResId
                    ),
                    contentDescription = currentWeather.description,
                )
                IconButton(onClick = {
                    onFavoriteIconClick(
                        FavoriteCoordinates(
                            id = currentWeather.id,
                            coordinates = currentWeather.coordinates
                        ),
                        isFavorite
                    )
                }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
//                        tint = MaterialTheme.colorScheme.onSurface,
                        painter = painterResource(
                            if (isFavorite) R.drawable.ic_heart_selected
                            else R.drawable.ic_heart_unselected
                        ),
                        contentDescription = stringResource(id = R.string.image_content_description_is_favorite),
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun FavoritesItemPreview() {
    WeatherForecastComposeTheme {
        Box(
//            modifier = Modifier.paint(
//                painterResource(id = R.drawable.sky_wallpaper),
//                contentScale = ContentScale.FillBounds
//            )
        ) {
            FavoriteItemCard(
                currentWeather = CurrentWeather(
                    id = 0,
                    coordinates = Coordinates(
                        lon = Const.DEFAULT_LON,
                        lat = Const.DEFAULT_LAT
                    ),
                    city = "Moscow",
                    country = "RU",
                    temperature = "22°C",
                    icon = WeatherType.IC_UNKNOWN,
                    description = "cloudy",
                    feelsLike = "24°C",
                    humidity = "44%",
                    pressure = "1002hPa",
                    windSpeed = "7m/c",
                    data = "15.06.2024",
                    timezone = 0,
                ),
                isFavorite = true,
                onFavoriteIconClick = { _, _ -> }
            )
        }
    }
}