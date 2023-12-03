package com.example.holymoly

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DBViewModel : ViewModel() {
    private val _flagDB = MutableLiveData<Int>()
    val flagDB: LiveData<Int> get() = _flagDB

    init {
        // 초기값 설정
        _flagDB.value = 0
    }

    // flagDB 업데이트 함수
    fun updateFlagDB(newFlag: Int) {
        _flagDB.value = newFlag
        Log.d("ny", "in viewmodel FlagDB updated: $newFlag")
    }
}