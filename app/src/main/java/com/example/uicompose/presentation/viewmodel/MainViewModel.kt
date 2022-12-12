package com.example.uicompose.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uicompose.data.repository.UserRepository
import com.example.uicompose.presentation.ui.state.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    init {
        //Get All Users
        repository.getUsers()
            .onEach { _state.value = HomeState(isLoading = true) }
            .onEach { users -> _state.value = HomeState(users = users) }
            .catch { exception -> _state.value = HomeState(error = exception.message) }
            .launchIn(viewModelScope)
    }
    /** TODO: borrar esto si no es necesario

    sealed class HomeEvent{
        data class DeleteUser(val user:User): HomeEvent()
    }

    fun onEvent(event:HomeEvent){
        when (event) {
            is HomeEvent.DeleteUser ->{
                viewModelScope.launch{
                     deleteUser(event.user)
                }
            }
        }
    }

     * */
}