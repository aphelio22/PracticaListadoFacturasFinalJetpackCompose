package com.example.practicalistadofacturasfinaljetpackcompose.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practicalistadofacturasfinaljetpackcompose.data.AppRepository
import com.example.practicalistadofacturasfinaljetpackcompose.data.room.EnergyDataModelRoom
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EnergyActivityViewModel @Inject constructor(private val appRepository: AppRepository) : ViewModel() {

private val _energyDataLiveData = MutableLiveData<EnergyDataModelRoom>()
    val energyDataLiveData: LiveData<EnergyDataModelRoom>
        get() = _energyDataLiveData

    init {
        fetchEnergyData()
    }

    fun fetchEnergyData() {
        viewModelScope.launch(Dispatchers.IO) {
            appRepository.fetchAndInsertEnergyDataFromMock()
            _energyDataLiveData.postValue(appRepository.getEnergyDataFromRoom())
        }
    }
}