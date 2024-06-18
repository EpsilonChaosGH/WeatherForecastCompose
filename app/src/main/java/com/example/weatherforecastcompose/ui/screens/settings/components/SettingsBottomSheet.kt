package com.example.weatherforecastcompose.ui.screens.settings.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherforecastcompose.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleSelectBottomSheet(
    title: String,
    items: List<BottomSheetItem>,
    selectedItem: BottomSheetItem,
    sheetState: SheetState,
    onSaveState: (BottomSheetItem) -> Unit,
    onDismiss: (() -> Unit)? = null,
) {
    val scope = rememberCoroutineScope()
    ModalBottomSheet(
        onDismissRequest = {
            scope.launch {
                sheetState.hide()
            }
            if (onDismiss != null) {
                onDismiss()
            }
        },
        sheetState = sheetState,
        windowInsets = WindowInsets.navigationBars,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 30.sp,
                modifier = Modifier.padding(
                    vertical = 16.dp,
                    horizontal = 16.dp
                )
            )

            val selectedItemsState = remember { mutableStateOf(selectedItem) }
            LazyColumn(Modifier.weight(1f, false)) {
                items(items) { item ->
                    Row(Modifier.clickable {
                        selectedItemsState.value = item.copy(isSelected = true)
                    }) {
                        RadioButton(
                            modifier = Modifier
                                .padding(16.dp)
                                .align(alignment = Alignment.CenterVertically),
                            selected = selectedItemsState.value.id == item.id,
                            onClick = null
                        )
                        Text(
                            text = item.name,
                            modifier = Modifier
                                .padding(6.dp)
                                .align(alignment = Alignment.CenterVertically)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Image(
                            modifier = Modifier
                                .padding(6.dp)
                                .align(alignment = Alignment.CenterVertically),
                            painter = painterResource(id = item.imageResId),
                            contentDescription = item.name
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = MaterialTheme.shapes.small,
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    scope.launch {
                        sheetState.hide()
                    }
                    onSaveState(selectedItemsState.value)
                }
            ) {
                Text(text = stringResource(R.string.bt_save), fontSize = 22.sp)
            }
        }
    }
}

data class BottomSheetItem(
    val name: String,
    val id: Int,
    @DrawableRes val imageResId: Int,
    val isSelected: Boolean = false,
)

const val NONE_ID = -1