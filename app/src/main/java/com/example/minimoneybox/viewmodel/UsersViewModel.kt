package com.example.minimoneybox.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.minimoneybox.repository.UsersRepository
import com.example.minimoneybox.state.UserDataState
import com.example.minimoneybox.state.UserViewState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val disposables = CompositeDisposable()
    val viewState = MutableLiveData<UserViewState>()

    fun getUserInformation(email: String, password: String) {
        disposables.add(
            getUserDataState(email, password)
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


    // TODO: Move this to usecase :O
    private fun getUserDataState(email: String, password: String): Observable<UserDataState> {
        return usersRepository.getUsers(email, password)
            .map<UserDataState> { user ->
                //TODO: Should i just pass user.user?
                UserDataState.Success(user)
            }
            .onErrorReturn { error -> UserDataState.Error(error.message) }
    }
}