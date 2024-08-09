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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherforecastcompose.R
import com.example.weatherforecastcompose.designsystem.components.AppBackground
import com.example.weatherforecastcompose.designsystem.components.ThemePreviews
import com.example.weatherforecastcompose.designsystem.theme.AppTheme

@Composable
fun SettingOptionRow(
    optionLabel: String,
    optionValue: String,
    @DrawableRes optionIcon: Int,
    @DrawableRes optionIconValue: Int,
    onOptionClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOptionClicked() }
            .padding(AppTheme.dimens.extraSmall)
    ) {
        Icon(
            painter = painterResource(id = optionIcon),
            contentDescription = optionLabel,
            modifier = Modifier
                .padding(AppTheme.dimens.extraSmall)
                .size(44.dp)
                .align(Alignment.CenterVertically)
        )
        Text(
            text = optionLabel,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(AppTheme.dimens.extraSmall),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.weight(AppTheme.weight.FULL))
        Text(
            text = optionValue,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(AppTheme.dimens.extraSmall),
            style = MaterialTheme.typography.bodyLarge
        )
        Image(
            painter = painterResource(id = optionIconValue),
            contentDescription = optionLabel,
            modifier = Modifier.padding(AppTheme.dimens.extraSmall)
        )
    }
}

@ThemePreviews
@Composable
fun SettingOptionRowPreview() {
    AppTheme {
        AppBackground {
            SettingOptionRow(
                optionLabel = "Language",
                optionValue = "Russian",
                optionIcon = R.drawable.ic_language,
                optionIconValue = R.drawable.flag_of_russia,
                onOptionClicked = {}
            )
        }
    }
}