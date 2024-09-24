package com.dicoding.biometricauth.fingerprintauth.login

import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.PromptInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class PromptManager(
    private val activity: AppCompatActivity
) {

    private val result = Channel<BiometricResult>()
    val promptResult = result.receiveAsFlow()
    fun showPrompt(
        title: String,
        description: String
    ) {
        val manager = BiometricManager.from(activity)
        val authenticator = if (Build.VERSION.SDK_INT >= 30) {
            BIOMETRIC_STRONG  or DEVICE_CREDENTIAL
        } else {
            BIOMETRIC_STRONG
        }

        val promptInfo = PromptInfo.Builder()
            .setTitle(title)
            .setDescription(description)
            .setAllowedAuthenticators(authenticator)

        if (Build.VERSION.SDK_INT < 30) {
            promptInfo.setNegativeButtonText("Cancel")
        }

        when(manager.canAuthenticate(authenticator)) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                result.trySend(BiometricResult.HardwareUnavailable)
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                result.trySend(BiometricResult.AuthenticationNotSet)
                return
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                result.trySend(BiometricResult.FeatureUnavailable)
            }

            else -> {
                Unit
            }

        }

        prompt.authenticate(promptInfo.build())
    }

    private val prompt = BiometricPrompt(
        activity,
        object : BiometricPrompt.AuthenticationCallback(){
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                result.trySend(BiometricResult.AuthenticationFailed)
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                result.trySend(BiometricResult.AuthenticationError(errString.toString()))
            }

            override fun onAuthenticationSucceeded(resultSuccess: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(resultSuccess)
                result.trySend(BiometricResult.AuthenticationSuccess)
            }
        }
    )


    sealed interface BiometricResult {
        data object HardwareUnavailable: BiometricResult
        data object FeatureUnavailable: BiometricResult
        data class AuthenticationError(val error: String): BiometricResult
        data object AuthenticationFailed: BiometricResult
        data object AuthenticationSuccess: BiometricResult
        data object AuthenticationNotSet: BiometricResult
    }
}