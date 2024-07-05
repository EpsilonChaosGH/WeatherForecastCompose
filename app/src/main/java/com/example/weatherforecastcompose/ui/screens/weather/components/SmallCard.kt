package com.example.weatherforecastcompose.ui.screens.weather.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
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
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.WeatherAppTheme
import com.example.weatherforecastcompose.designsystem.WeatherForecastComposeTheme

@Composable
internal fun SmallCard(
    modifier: Modifier,
    titleResId: Int,
    value: String,
    iconId: Int,
    iconColor: Color = LocalContentColor.current
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(WeatherAppTheme.dimens.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(WeatherAppTheme.weight.FULL)) {
                Text(
                    text = stringResource(id = titleResId),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = value, style = MaterialTheme.typography.titleLarge)
            }
            Icon(
                modifier = Modifier.size(WeatherAppTheme.dimens.ultraLarge),
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
        Column {
            SmallCard(
                modifier = Modifier
                    .width(210.dp)
                    .padding(20.dp),
                titleResId = R.string.title_pm10,
                value = "122asd",
                iconId = R.drawable.ic_air_good,
                iconColor = colorResource(R.color.orange)
            )
        }
    }
}