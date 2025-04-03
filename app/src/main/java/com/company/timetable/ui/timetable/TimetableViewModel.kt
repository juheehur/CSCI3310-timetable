package com.company.timetable.ui.timetable

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.company.timetable.data.model.TimetableData
import com.company.timetable.data.repository.TimetableRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class TimetableState {
    object Initial : TimetableState()
    object NotRegistered : TimetableState()
    object Registered : TimetableState()
    object Processing : TimetableState()
    data class Success(val data: TimetableData) : TimetableState()
    data class Error(val message: String) : TimetableState()
}

@HiltViewModel
class TimetableViewModel @Inject constructor(
    private val repository: TimetableRepository
) : ViewModel() {
    
    private val _timetableState = MutableStateFlow<TimetableState>(TimetableState.Initial)
    val timetableState: StateFlow<TimetableState> = _timetableState
    
    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri
    
    // For now, we'll just simulate checking if a timetable is registered
    // This would typically involve checking a database or local storage
    fun checkTimetableRegistration(userId: String) {
        // Dummy implementation for demonstration
        // In a real app, you would check your database or storage
        _timetableState.value = TimetableState.NotRegistered
    }
    
    fun setSelectedImage(uri: Uri) {
        _selectedImageUri.value = uri
    }
    
    fun uploadTimetable() {
        val uri = _selectedImageUri.value ?: return
        
        _timetableState.value = TimetableState.Processing
        
        viewModelScope.launch {
            repository.processTimeTableImage(uri)
                .onSuccess { timetableData ->
                    _timetableState.value = TimetableState.Success(timetableData)
                }
                .onFailure { exception ->
                    _timetableState.value = TimetableState.Error(exception.message ?: "Unknown error")
                }
        }
    }
    
    // For testing purposes
    fun setRegistered() {
        _timetableState.value = TimetableState.Registered
    }
    
    fun setNotRegistered() {
        _timetableState.value = TimetableState.NotRegistered
    }
} 