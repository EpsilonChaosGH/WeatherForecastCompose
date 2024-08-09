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
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.components.AppBackground
import com.example.weatherforecastcompose.designsystem.components.ThemePreviews
import com.example.weatherforecastcompose.designsystem.theme.AppTheme

@Composable
internal fun SmallCard(
    value: String,
    titleResId: Int,
    iconResId: Int,
    modifier: Modifier,
    iconColor: Color = LocalContentColor.current
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(AppTheme.dimens.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(AppTheme.weight.FULL)) {
                Text(
                    text = stringResource(id = titleResId),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Icon(
                modifier = Modifier.size(AppTheme.dimens.ultraLarge),
                tint = iconColor,
                painter = painterResource(iconResId),
                contentDescription = stringResource(id = titleResId),
            )
        }
    }
}

@ThemePreviews
@Composable
internal fun SmallCardPreview() {
    AppTheme {
        AppBackground {
            SmallCard(
                value = "122 μg/m3",
                titleResId = R.string.title_pm10,
                iconResId = R.drawable.ic_air_good,
                modifier = Modifier
                    .width(210.dp)
                    .padding(20.dp),
                iconColor = colorResource(R.color.orange)
            )
        }
    }
}