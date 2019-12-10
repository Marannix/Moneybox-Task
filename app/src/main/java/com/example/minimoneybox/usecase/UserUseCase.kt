package com.example.minimoneybox.usecase

import com.example.minimoneybox.repository.UsersRepository
import com.example.minimoneybox.state.UserDataState
import io.reactivex.Observable
import javax.inject.Inject

class UserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {

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