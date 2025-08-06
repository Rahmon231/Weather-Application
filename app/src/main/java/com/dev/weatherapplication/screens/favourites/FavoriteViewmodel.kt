package com.dev.weatherapplication.screens.favourites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.weatherapplication.model.Favorite
import com.dev.weatherapplication.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewmodel @Inject constructor(private val repository: WeatherDbRepository)
    :ViewModel(){
        private val _favList = MutableStateFlow<List<Favorite>>(emptyList())
    val favList = _favList.asStateFlow()
    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFavourites().distinctUntilChanged()
                .collect { listOfFavs ->
                    if (listOfFavs.isEmpty()){
                        _favList.value = emptyList()
                        Log.d("FavList", ": emptyList")
                    }else{
                        _favList.value = listOfFavs
                        Log.d("FavList", ": ${favList.value}")
                    }
                }

        }
    }
    fun insertFavorite(favorite: Favorite) = viewModelScope.launch {
        repository.insertFavorite(favorite)
    }
    fun updateFavorite(favorite: Favorite) = viewModelScope.launch {
        repository.updateFavorite(favorite)
    }
    fun deleteFavorite(favorite: Favorite) = viewModelScope.launch {
        repository.deleteFavorite(favorite)
    }
    fun deleteAllFavorites() = viewModelScope.launch {
        repository.deleteAllFavorites()
    }
    fun getFavById(city: String) = viewModelScope.launch {
        repository.getFavById(city)
    }



}