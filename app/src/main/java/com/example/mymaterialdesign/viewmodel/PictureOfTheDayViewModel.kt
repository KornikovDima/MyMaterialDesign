package com.example.mymaterialdesign.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mymaterialdesign.BuildConfig
import com.example.mymaterialdesign.model.PictureOfTheDayResponseData
import com.example.mymaterialdesign.model.RepositoryImpl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val REQUEST_ERROR = "Ошибка запроса на сервер"

class PictureOfTheDayViewModel(
    private val liveData: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) :
    ViewModel() {

    fun getLiveData(): MutableLiveData<AppState> {
        return liveData
    }

    fun sendRequest(date: String) {
        liveData.postValue(AppState.Loading)
        repositoryImpl.getPictureOfTheDayApi().getPictureOfTheDay(BuildConfig.NASA_API_KEY, date)
            .enqueue(callback)
    }

    private val callback = object : Callback<PictureOfTheDayResponseData> {
        override fun onResponse(
            call: Call<PictureOfTheDayResponseData>,
            response: Response<PictureOfTheDayResponseData>
        ) {
            if (response.isSuccessful) {
                liveData.postValue(AppState.Success(response.body()!!))
            } else {
                liveData.postValue(AppState.Error(throw IllegalStateException("Error")))
            }
        }

        override fun onFailure(call: Call<PictureOfTheDayResponseData>, t: Throwable) {
            liveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }
}