package at.fhooe.mc.ada.core.util

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.material.BottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ScaffoldState
import at.fhooe.mc.ada.R
import at.fhooe.mc.ada.features.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object CustomBiometricPrompt {
    @OptIn(ExperimentalMaterialApi::class)
    fun showBiometricPrompt(
        activity: MainActivity,
        bottomSheetScaffoldState: BottomSheetScaffoldState,
        scaffoldState: ScaffoldState,
        scope: CoroutineScope
    ) {
        val biometricsIgnoredErrors = listOf(
            BiometricPrompt.ERROR_NEGATIVE_BUTTON,
            BiometricPrompt.ERROR_CANCELED,
            BiometricPrompt.ERROR_USER_CANCELED,
            BiometricPrompt.ERROR_NO_BIOMETRICS
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(activity.getString(R.string.biometric_prompt_title))
            .setSubtitle(activity.getString(R.string.biometric_prompt_description))
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
            .build()

        val biometricPrompt = BiometricPrompt(
            activity,
            object : BiometricPrompt.AuthenticationCallback() {
                @SuppressLint("StringFormatInvalid")
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    if (errorCode !in biometricsIgnoredErrors) {
                        scope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                activity.getString(
                                    R.string.biometric_prompt_error,
                                    errString
                                )
                            )
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        }
                    }
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    scope.launch {
                        bottomSheetScaffoldState.bottomSheetState.expand()
                    }
                }

                override fun onAuthenticationFailed() {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            activity.getString(
                                R.string.biometric_prompt_failed
                            )
                        )
                    }
                }
            }
        )
        biometricPrompt.authenticate(promptInfo)
    }
}