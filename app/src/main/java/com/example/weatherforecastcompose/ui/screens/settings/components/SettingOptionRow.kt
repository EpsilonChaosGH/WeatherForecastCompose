package com.example.weatherforecastcompose.ui.screens.settings.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.ui.theme.WeatherForecastComposeTheme

@Composable
fun SettingOptionRow(
    modifier: Modifier = Modifier,
    optionLabel: String,
    optionValue: String,
    @DrawableRes optionIconValue: Int,
    @DrawableRes optionIcon: Int,
    onOptionClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOptionClicked() }
            .padding(6.dp)
    ) {
        Icon(
            painter = painterResource(id = optionIcon),
            contentDescription = optionLabel,
            modifier = Modifier.padding(6.dp)
                .size(44.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = optionLabel,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(6.dp),
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = optionValue,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(6.dp)
        )
        Image(
            painter = painterResource(id = optionIconValue),
            contentDescription = optionLabel,
            modifier = Modifier.padding(6.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingOptionRowPreview() {
    WeatherForecastComposeTheme {
        SettingOptionRow(
            optionLabel = "Language",
            optionValue = "Russian",
            optionIcon = R.drawable.ic_language,
            optionIconValue = R.drawable.flag_of_russia,
            onOptionClicked = {}
        )
    }
}