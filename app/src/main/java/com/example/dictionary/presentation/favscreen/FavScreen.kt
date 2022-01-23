package com.example.dictionary.presentation.favscreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FavScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment =  Alignment.Center
    ) {
        Text(text = "Fav Screen")
    }
}