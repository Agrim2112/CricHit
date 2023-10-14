package com.example.androidworrkshop.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidworrkshop.di.Resource
import com.example.androidworrkshop.model.MatchInfo
import com.example.androidworrkshop.repo.CricketRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val CricketRepo: CricketRepo) : ViewModel() {

    private val performFetchMatches = MutableLiveData<Resource<MatchInfo>>()
    val performFetchMatchesStatus: LiveData<Resource<MatchInfo>>
        get() = performFetchMatches


    fun getShops() {
        viewModelScope.launch {
            try {
                performFetchMatches.value = Resource.loading()
                val response = CricketRepo.getMatchList()
                if (response.isSuccessful) {
                    performFetchMatches.value = Resource.success(response.body()!!)
                } else {
                    performFetchMatches.value = Resource.empty()
                }
            } catch (e: Exception) {
                if (e is UnknownHostException) {
                    performFetchMatches.value = Resource.offlineError()
                } else {
                    performFetchMatches.value = Resource.error(e)
                }
            }
        }
    }

}