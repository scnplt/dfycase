package dev.sertan.android.dfycase

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application class for the DfyCase app.
 * This class is annotated with @HiltAndroidApp to enable dependency injection using Hilt.
 */
@HiltAndroidApp
internal class DfyApp : Application()
