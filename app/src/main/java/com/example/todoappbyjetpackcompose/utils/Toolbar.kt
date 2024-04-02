package com.example.todoappbyjetpackcompose.utils

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoappbyjetpackcompose.ui.theme.LightPrimary


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(title: String, onNavigationIconClick: (() -> Unit)? = null) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                title,
                modifier = Modifier.padding(40.dp),
                style = TextStyle(
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 22.sp, fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            if (onNavigationIconClick != null) {
                IconButton(
                    onClick = {
                        onNavigationIconClick()
                    },
                    modifier = Modifier.padding(top = 30.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "backIcon"
                    )
                }
            }
        },
        modifier = Modifier.height(140.dp),
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = LightPrimary,
            navigationIconContentColor = Color.White,
        ),
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChatTopBarPreview() {
    TopAppBar(title = "To Do List") {
    }
}