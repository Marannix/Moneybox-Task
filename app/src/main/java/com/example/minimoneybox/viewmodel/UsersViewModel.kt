package com.example.minimoneybox.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.state.UserDataState
import com.example.minimoneybox.state.UserViewState
import com.example.minimoneybox.usecase.UserUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val viewState = MutableLiveData<UserViewState>()

    fun getUserInformation(email: String, password: String) {
        disposables.add(
            userUseCase.getUserDataState(email, password)
                .observeOn(AndroidSchedulers.mainThread())
                .map { userDataState ->
                    return@map when (userDataState) {
                        is UserDataState.Success -> {
                            UserViewState.ShowUser(userDataState.user)
                        }
                        is UserDataState.Error -> {
                            UserViewState.ShowError(userDataState.errorMessage)
                        }
                    }
                }.doOnSubscribe { viewState.value = UserViewState.Loading }
                .subscribe {
                    this.viewState.value = it
                }
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}