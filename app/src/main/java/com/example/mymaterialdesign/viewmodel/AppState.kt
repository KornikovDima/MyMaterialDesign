package com.example.mymaterialdesign.viewmodel

import com.example.mymaterialdesign.model.PictureOfTheDayResponseData

sealed class AppState {
    data class Success(val pictureOfTheDayResponseData: PictureOfTheDayResponseData) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}