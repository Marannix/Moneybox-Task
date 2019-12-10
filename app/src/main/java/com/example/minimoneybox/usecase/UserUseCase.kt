package com.example.minimoneybox.usecase

import com.example.minimoneybox.repository.UsersRepository
import com.example.minimoneybox.state.UserDataState
import io.reactivex.Observable
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {

    /**
     * Return a data state to the viewmodel which will contain either a success or an error state (auth / generic error)
     */
    fun getUserDataState(email: String, password: String): Observable<UserDataState> {
        return usersRepository.getUsers(email, password)
            .map<UserDataState> { user ->
                UserDataState.Success(user)
            }
            .onErrorReturn { error ->
                UserDataState.Error(error.message)
            }
    }

}