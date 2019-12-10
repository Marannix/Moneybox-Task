package com.example.minimoneybox.repository

import com.example.minimoneybox.api.UserApi
import com.example.minimoneybox.data.UsersDao
import com.example.minimoneybox.data.user.User
import com.example.minimoneybox.data.user.UserResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val usersDao: UsersDao,
    private val userApi: UserApi
) {

    fun getUsers(email: String, password: String): Observable<UserResponse> {
       return getUserFromApi(email, password).toObservable()
    }

    // Can only get user from api so no need to check against db.... unless the user is already signed in!!! :OOO
    private fun getUserFromApi(email: String, password: String): Single<UserResponse> {
        return userApi.logInUser(email, password, "ANYTHING")
            .doOnSuccess { user ->
                storeUserInDb(user.user)
            }
            .subscribeOn(Schedulers.io())
    }

    // Store user so I can fetch later in dashboard activity
    // Need to also delete user for db
    private fun storeUserInDb(user: User) {
        usersDao.insertUser(user)
    }

    fun getUserFromDB() : User {
        return usersDao.getUser()
    }

}
