package com.hdmsh.core_ui_playback.searching

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SpotifySearchBar() {
    Card(
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clip(RoundedCornerShape(4.dp)),
        backgroundColor = MaterialTheme.colors.background
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.LightGray,
                contentDescription = null
            )
            TextField(
                value = TextFieldValue(""),
                placeholder = { Text("Search in stations") },
                onValueChange = {},
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.background,
                    cursorColor = MaterialTheme.colors.onSurface,
                    focusedIndicatorColor = MaterialTheme.colors.onSurface,
                    disabledIndicatorColor = MaterialTheme.colors.background
                ),
                textStyle = typography.body2
            )

        }
    }
}
