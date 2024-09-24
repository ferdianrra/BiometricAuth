package com.dicoding.biometricauth

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.dicoding.biometricauth.ui.theme.BiometricAuthTheme
import com.dicoding.biometricauth.welcome.WelcomeScreen

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BiometricAuthTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = colorResource(id = R.color.blue))
                ) {
                    BiometricApp()
                }
            }
        }
    }
}
