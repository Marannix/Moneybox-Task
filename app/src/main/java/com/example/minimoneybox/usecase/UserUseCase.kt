package com.example.minimoneybox.usecase

import com.example.minimoneybox.repository.UsersRepository
import com.example.minimoneybox.state.UserDataState
import io.reactivex.Observable
import retrofit2.HttpException
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
                // TODO: Maybe at this point check if either error 400, 401 or 500
                if (error is HttpException) {
                    when (error.code()) {

                    }
                }
                UserDataState.Error(error.message)
            }
    }

}