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
                // TODO: Maybe at this point check if either error 400, 401 or 500
//                if (error is HttpException) {
//                    if (error.code() == 401) {
//                        val signInError = Gson().fromJson<SignInErrorResponse>(
//                            error.response().errorBody()!!.charStream(),
//                            SignInErrorResponse::class.java
//                        )
////                        AuthModel.SignInException(signInError)
//
//                        UserDataState.Error(AuthModel.SignInException(signInError))
//                    } else if (error.code() == 400) {
//                        val signUpValidationError = Gson().fromJson<SignInValidationResponse>(
//                            error.response().errorBody()!!.charStream(),
//                            SignInValidationResponse::class.java
//                        )
//                        UserDataState.Error( AuthModel.SignInValidationException(signUpValidationError)
//                    }
//                }
                UserDataState.Error(error)
            }
    }

}