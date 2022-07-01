package br.com.jacocoandsonar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val _showToastMessageWithId = MutableLiveData<Int>()
    val showToastMessageWithId: LiveData<Int> = _showToastMessageWithId

    fun buttonClick() {
        _showToastMessageWithId.value = R.string.toast_message
    }

}