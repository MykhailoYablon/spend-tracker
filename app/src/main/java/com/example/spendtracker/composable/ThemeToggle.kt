package com.example.spendtracker.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.spendtracker.theme.ThemeManager
import com.example.spendtracker.theme.ThemeMode
import com.example.spendtracker.theme.rememberThemeManager

@Composable
fun ThemeToggle(
    modifier: Modifier = Modifier,
    showDropdown: Boolean = false
) {
    val themeManager = rememberThemeManager()
    
    if (showDropdown) {
        ThemeDropdown(
            themeManager = themeManager,
            modifier = modifier
        )
    } else {
        SimpleThemeToggle(
            themeManager = themeManager,
            modifier = modifier
        )
    }
}

@Composable
private fun SimpleThemeToggle(
    themeManager: ThemeManager,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (themeManager.currentThemeMode == ThemeMode.DARK) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "icon_rotation"
    )
    
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable { themeManager.toggleTheme() }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = when (themeManager.currentThemeMode) {
                ThemeMode.LIGHT -> Icons.Default.Brightness7
                ThemeMode.DARK -> Icons.Default.Brightness4
                ThemeMode.SYSTEM -> Icons.Default.Settings
            },
            contentDescription = "Toggle theme",
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.rotate(rotation)
        )
    }
}

@Composable
private fun ThemeDropdown(
    themeManager: ThemeManager,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    
    Box(modifier = modifier) {
        // Toggle button
        Card(
            modifier = Modifier
                .clickable { expanded = true }
                .padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (themeManager.currentThemeMode) {
                        ThemeMode.LIGHT -> Icons.Default.Brightness7
                        ThemeMode.DARK -> Icons.Default.Brightness4
                        ThemeMode.SYSTEM -> Icons.Default.Settings
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = when (themeManager.currentThemeMode) {
                        ThemeMode.LIGHT -> "Light"
                        ThemeMode.DARK -> "Dark"
                        ThemeMode.SYSTEM -> "System"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        
        // Dropdown menu
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(12.dp)
            )
        ) {
            ThemeMode.entries.forEach { themeMode ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = when (themeMode) {
                                    ThemeMode.LIGHT -> Icons.Default.Brightness7
                                    ThemeMode.DARK -> Icons.Default.Brightness4
                                    ThemeMode.SYSTEM -> Icons.Default.Settings
                                },
                                contentDescription = null,
                                tint = if (themeManager.currentThemeMode == themeMode) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                },
                                modifier = Modifier.size(20.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Text(
                                text = when (themeMode) {
                                    ThemeMode.LIGHT -> "Light Theme"
                                    ThemeMode.DARK -> "Dark Theme"
                                    ThemeMode.SYSTEM -> "System Default"
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (themeManager.currentThemeMode == themeMode) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                },
                                color = if (themeManager.currentThemeMode == themeMode) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurface
                                }
                            )
                        }
                    },
                    onClick = {
                        themeManager.setThemeMode(themeMode)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AnimatedThemeToggle(
    themeManager: ThemeManager,
    modifier: Modifier = Modifier
) {
    val isVisible by animateFloatAsState(
        targetValue = if (themeManager.isDarkTheme) 1f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "visibility"
    )
    
    Box(modifier = modifier) {
        // Light mode icon (always visible)
        AnimatedVisibility(
            visible = !themeManager.isDarkTheme,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Icon(
                imageVector = Icons.Default.Brightness7,
                contentDescription = "Light mode",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
        
        // Dark mode icon (always visible)
        AnimatedVisibility(
            visible = themeManager.isDarkTheme,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
        ) {
            Icon(
                imageVector = Icons.Default.Brightness4,
                contentDescription = "Dark mode",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
} 