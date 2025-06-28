package com.example.spendtracker.util

object AppConstants {
    const val DATABASE_NAME = "investment_tracker_db"
    const val DEFAULT_CURRENCY = "USD"
    const val MAX_AMOUNT = 999999.99
    const val MIN_AMOUNT = 0.01
    
    // UI Constants
    const val CARD_ELEVATION = 4
    const val CARD_SELECTED_ELEVATION = 8
    const val FAB_PADDING = 36
    const val DEFAULT_PADDING = 16
    const val SMALL_PADDING = 8
    const val ICON_SIZE = 64
    const val CAROUSEL_HEIGHT = 200
    const val CAROUSEL_IMAGE_SIZE = 80
    const val PAGE_INDICATOR_SIZE = 8
    
    // Animation durations
    const val ANIMATION_DURATION = 300L
    
    // Error messages
    const val ERROR_EMPTY_NAME = "Name cannot be empty"
    const val ERROR_INVALID_AMOUNT = "Invalid amount"
    const val ERROR_NEGATIVE_AMOUNT = "Amount must be positive"
    const val ERROR_ADD_INVESTMENT = "Failed to add investment"
    const val ERROR_UPDATE_INVESTMENT = "Failed to update investment"
    const val ERROR_DELETE_INVESTMENT = "Failed to delete investment"
    const val ERROR_ADD_SPENDING = "Failed to add spending"
    const val ERROR_DELETE_SPENDING = "Failed to delete spending"
    
    // Success messages
    const val SUCCESS_ADD_INVESTMENT = "Investment added successfully"
    const val SUCCESS_UPDATE_INVESTMENT = "Investment updated successfully"
    const val SUCCESS_DELETE_INVESTMENT = "Investment deleted successfully"
    const val SUCCESS_ADD_SPENDING = "Spending added successfully"
    const val SUCCESS_DELETE_SPENDING = "Spending deleted successfully"
} 