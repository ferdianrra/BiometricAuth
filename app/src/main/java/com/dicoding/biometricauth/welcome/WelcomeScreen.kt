package com.dicoding.biometricauth.welcome

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dicoding.biometricauth.R
import com.dicoding.biometricauth.fingerprintauth.login.PromptManager
import com.dicoding.biometricauth.fingerprintauth.login.PromptManager.*
import com.dicoding.biometricauth.navigation.Screen
import com.dicoding.biometricauth.ui.theme.BiometricAuthTheme

@Composable
fun WelcomeScreen (
    modifier: Modifier = Modifier.background(color = colorResource(id = R.color.blue)),
    navigateToHome: (String) -> Unit
) {
    val context = LocalContext.current
    val promptManager by remember { mutableStateOf(
        PromptManager(
            context as AppCompatActivity
        )
    ) }

    val biometricResult by promptManager.promptResult.collectAsState(initial = null)
    val enrollLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { println("Result: $it") }
        )

    LaunchedEffect(biometricResult) {
        if (biometricResult is BiometricResult.AuthenticationNotSet) {
            if (Build.VERSION.SDK_INT >= 30) {
                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                    putExtra(
                        Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                    )
                }
                enrollLauncher.launch(enrollIntent)
            }
        } else if (biometricResult is BiometricResult.AuthenticationSuccess) {
            navigateToHome(Screen.Home.route)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.login_desc),
            color = colorResource(id = R.color.white),
            fontWeight = FontWeight.Bold
        )
        Image(
            painter = painterResource(id = R.drawable.login_img),
            contentDescription = "login image",
            modifier = modifier.clip(CircleShape)
        )
        Button(
            colors =  ButtonDefaults.buttonColors(
                containerColor = colorResource(id = R.color.white),
                contentColor = colorResource(id = R.color.blue)
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            onClick = {
                promptManager.showPrompt(
                    title = "Fingerprint Required",
                    description = "Please scan your fingerprint to proceed."
                )

            },
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = 16.sp
            )
        }
        biometricResult?.let { result ->
            Text(
                text = when(result) {
                    is BiometricResult.AuthenticationError -> {
                        stringResource(id = R.string.biometric_error, result.error)
                    }
                    BiometricResult.AuthenticationFailed -> {
                        stringResource(id = R.string.authentication_failed)
                    }
                    BiometricResult.AuthenticationNotSet -> {
                        stringResource(id = R.string.authentication_not_set)
                    }
                    BiometricResult.AuthenticationSuccess -> {
                        stringResource(id = R.string.authentication_success)
                    }
                    BiometricResult.FeatureUnavailable -> {
                        stringResource(id = R.string.feature_unavailable)
                    }
                    BiometricResult.HardwareUnavailable -> {
                       stringResource(id = R.string.hardware_unavailable)
                    }
                },
                color = colorResource(id = R.color.white)
            )

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewWelcomeScreen() {
    BiometricAuthTheme {
        WelcomeScreen(navigateToHome = {})
    }
}