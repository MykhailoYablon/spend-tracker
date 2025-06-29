package com.example.spendtracker.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

@Composable
fun AppTheme(
    themeManager: ThemeManager = rememberThemeManager(),
    content: @Composable () -> Unit
) {
    val appTheme = themeManager.getCurrentTheme()
    
    val colorScheme = if (appTheme.isDark) {
        darkColorScheme(
            primary = appTheme.primary,
            onPrimary = appTheme.onPrimary,
            secondary = appTheme.secondary,
            onSecondary = appTheme.onSecondary,
            background = appTheme.background,
            surface = appTheme.surface,
            onSurface = appTheme.onSurface,
            error = appTheme.error,
            onError = appTheme.onError
        )
    } else {
        lightColorScheme(
            primary = appTheme.primary,
            onPrimary = appTheme.onPrimary,
            secondary = appTheme.secondary,
            onSecondary = appTheme.onSecondary,
            background = appTheme.background,
            surface = appTheme.surface,
            onSurface = appTheme.onSurface,
            error = appTheme.error,
            onError = appTheme.onError
        )
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as android.app.Activity).window
            window.statusBarColor = appTheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !appTheme.isDark
        }
    }
    
    CompositionLocalProvider(LocalThemeManager provides themeManager) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
} 