package com.example.spendtracker.theme

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.spendtracker.R

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

data class AppTheme(
    val isDark: Boolean,
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val background: Color,
    val surface: Color,
    val onSurface: Color,
    val error: Color,
    val onError: Color
)

class ThemeManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    var currentThemeMode by mutableStateOf(loadThemeMode())
        private set
    
    var isDarkTheme by mutableStateOf(shouldUseDarkTheme())
        private set
    
    private fun loadThemeMode(): ThemeMode {
        val themeName = sharedPreferences.getString("theme_mode", ThemeMode.SYSTEM.name)
        return try {
            ThemeMode.valueOf(themeName ?: ThemeMode.SYSTEM.name)
        } catch (e: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }
    
    private fun shouldUseDarkTheme(): Boolean {
        return when (currentThemeMode) {
            ThemeMode.LIGHT -> false
            ThemeMode.DARK -> true
            ThemeMode.SYSTEM -> isSystemInDarkTheme()
        }
    }
    
    private fun isSystemInDarkTheme(): Boolean {
        return context.resources.configuration.uiMode and 
               android.content.res.Configuration.UI_MODE_NIGHT_MASK == 
               android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
    
    fun setThemeMode(themeMode: ThemeMode) {
        currentThemeMode = themeMode
        isDarkTheme = shouldUseDarkTheme()
        sharedPreferences.edit().putString("theme_mode", themeMode.name).apply()
    }
    
    fun toggleTheme() {
        val newMode = when (currentThemeMode) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.SYSTEM
            ThemeMode.SYSTEM -> ThemeMode.LIGHT
        }
        setThemeMode(newMode)
    }
    
    fun getCurrentTheme(): AppTheme {
        return if (isDarkTheme) {
            AppTheme(
                isDark = true,
                primary = Color(0xFFBB86FC), // purple_200
                onPrimary = Color(0xFF000000), // black
                secondary = Color(0xFF03DAC5), // teal_200
                onSecondary = Color(0xFF000000), // black
                background = Color(0xFF121212), // dark background
                surface = Color(0xFF1E1E1E), // dark surface
                onSurface = Color(0xFFFFFFFF), // white
                error = Color(0xFFCF6679), // error color for dark theme
                onError = Color(0xFF000000) // black
            )
        } else {
            AppTheme(
                isDark = false,
                primary = Color(0xFF3700B3), // purple_700
                onPrimary = Color(0xFFFFFFFF), // white
                secondary = Color(0xFF018786), // teal_700
                onSecondary = Color(0xFFFFFFFF), // white
                background = Color(0xFFFAFAFA), // light background
                surface = Color(0xFFFFFFFF), // white
                onSurface = Color(0xFF000000), // black
                error = Color(0xFFB00020), // error color for light theme
                onError = Color(0xFFFFFFFF) // white
            )
        }
    }
}

val LocalThemeManager = staticCompositionLocalOf<ThemeManager> { 
    error("ThemeManager not provided") 
}

@Composable
fun rememberThemeManager(): ThemeManager {
    return LocalThemeManager.current
} 