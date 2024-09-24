package com.dicoding.biometricauth.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.dicoding.biometricauth.ui.theme.BiometricAuthTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
     Text(text = "Welcome to the Home Screen.")
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewHomeScreen() {
    BiometricAuthTheme {
        HomeScreen()
    }
}