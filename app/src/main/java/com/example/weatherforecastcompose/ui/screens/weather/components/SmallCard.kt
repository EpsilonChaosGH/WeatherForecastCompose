package com.example.weatherforecastcompose.ui.screens.weather.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme

@Composable
internal fun SmallCard(
    modifier: Modifier,
    titleResId: Int,
    value: String,
    iconId: Int,
    iconColor: Color = LocalContentColor.current
) {
    Card(
        modifier = modifier,
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = stringResource(id = titleResId))
//                Spacer(modifier = Modifier.weight(1f))
                Text(text = value, fontSize = 20.sp)
            }
            Icon(
                modifier = Modifier.size(50.dp),
                tint = iconColor,
                painter = painterResource(iconId),
                contentDescription = stringResource(id = titleResId),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
internal fun SmallCardPreview() {
    WeatherForecastComposeTheme {
        Column(
//            modifier = Modifier.paint(
//                painterResource(id = R.drawable.sky_wallpaper),
//                contentScale = ContentScale.FillBounds
//            )
        ) {
            SmallCard(
                modifier = Modifier
                    .width(210.dp)
                    .padding(20.dp),
                titleResId = R.string.title_pm10,
                value = "12",
                iconId = R.drawable.ic_air_good,
                iconColor = colorResource(R.color.orange)
            )
        }
    }
}

