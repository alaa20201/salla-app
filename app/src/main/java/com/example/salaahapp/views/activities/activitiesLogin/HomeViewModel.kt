package com.example.salaahapp.views.activities.activitiesLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    val city = "Bangalore"
    val country = "India"
    val method = 8

    fun getSallahData(){
        viewModelScope.launch {

        }
    }
}