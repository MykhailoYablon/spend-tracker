# Light/Dark Theme Toggle Feature

This document describes the implementation of the Light/Dark theme toggle feature for the Spend Tracker app.

## Features

### 1. **Theme Modes**
- **Light Theme**: Always uses light colors
- **Dark Theme**: Always uses dark colors  
- **System Theme**: Follows the device's system theme setting

### 2. **Theme Toggle Components**
- **Simple Toggle**: Quick circular button in the app bar
- **Dropdown Toggle**: More detailed theme selection with descriptions
- **Settings Screen**: Full theme management interface

### 3. **Persistence**
- Theme preference is saved using SharedPreferences
- Theme persists across app restarts
- Respects system theme changes when in "System" mode

## Implementation Details

### Files Created/Modified

#### New Files:
- `app/src/main/java/com/example/spendtracker/theme/ThemeManager.kt`
- `app/src/main/java/com/example/spendtracker/theme/AppTheme.kt`
- `app/src/main/java/com/example/spendtracker/composable/ThemeToggle.kt`
- `app/src/main/java/com/example/spendtracker/composable/SettingsScreen.kt`

#### Modified Files:
- `app/src/main/java/com/example/spendtracker/MainActivity.kt`
- `app/src/main/java/com/example/spendtracker/composable/InvestmentTrackerApp.kt`
- `app/src/main/java/com/example/spendtracker/composable/InvestmentGraphsScreen.kt`

### Key Components

#### ThemeManager
- Manages theme state and persistence
- Handles system theme detection
- Provides theme switching functionality

#### AppTheme
- Custom Material3 theme wrapper
- Applies theme colors to the entire app
- Handles status bar color changes

#### ThemeToggle
- Reusable theme toggle component
- Supports both simple and dropdown modes
- Animated icon transitions

#### SettingsScreen
- Dedicated settings interface
- Theme selection with descriptions
- Quick toggle access

## Usage

### Quick Toggle
1. Tap the theme icon in the top app bar
2. Theme cycles through: Light → Dark → System → Light

### Detailed Theme Selection
1. Tap the settings icon in the top app bar
2. Navigate to the Settings screen
3. Choose from Light, Dark, or System themes
4. See immediate theme changes

### System Theme
- Automatically follows device theme settings
- Updates when device theme changes
- Provides seamless integration

## Color Schemes

### Light Theme
- Primary: Purple (#3700B3)
- Secondary: Teal (#018786)
- Background: Light Gray (#FAFAFA)
- Surface: White (#FFFFFF)

### Dark Theme
- Primary: Light Purple (#BB86FC)
- Secondary: Light Teal (#03DAC5)
- Background: Dark Gray (#121212)
- Surface: Darker Gray (#1E1E1E)

## Benefits

1. **User Preference**: Users can choose their preferred theme
2. **Accessibility**: Dark theme reduces eye strain in low light
3. **Battery Saving**: Dark theme saves battery on OLED screens
4. **Modern UX**: Follows Material Design 3 guidelines
5. **System Integration**: Respects device theme preferences

## Future Enhancements

1. **Custom Color Schemes**: Allow users to customize theme colors
2. **Auto Theme**: Switch themes based on time of day
3. **Theme Animations**: Smooth transitions between themes
4. **Per-Screen Themes**: Different themes for different screens
5. **Theme Presets**: Pre-defined theme collections

## Technical Notes

- Uses Compose Material3 for modern theming
- Implements CompositionLocal for theme state management
- Follows Android best practices for theme implementation
- Supports both light and dark status bar colors
- Maintains backward compatibility with existing UI components 