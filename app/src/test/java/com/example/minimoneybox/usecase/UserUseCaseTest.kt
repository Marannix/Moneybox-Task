package com.example.minimoneybox.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.minimoneybox.api.UserApi
import com.example.minimoneybox.data.UsersDao
import com.example.minimoneybox.data.user.UserResponse
import com.example.minimoneybox.repository.UsersRepository
import com.example.minimoneybox.state.UserDataState
import com.example.minimoneybox.utils.RxImmediateSchedulerRule
import com.example.minimoneybox.utils.UnitTestUtils
import com.google.gson.GsonBuilder
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

private const val a_user_email = "mockito@moneybox.com"
private const val a_user_password = "mockito@moneybox.com"
private const val a_user_idfa = "ANYTHING"
private const val a_wrong_email = "incorrectmockito@moneybox.com"
private const val a_wrong_password = "PW123ALSO"
private const val a_user_error_message = "No Network"

class UserUseCaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val api = Mockito.mock(UserApi::class.java)
    private val dao = Mockito.mock(UsersDao::class.java)

    private val usersRepository by lazy { UsersRepository(dao, api) }
    private val userUseCase by lazy { UserUseCase(usersRepository) }

    private lateinit var userResponse: UserResponse
    private lateinit var state: UserDataState
    private lateinit var expectedSuccessState: UserDataState.Success
    private val expectedErrorState = UserDataState.Error(a_user_error_message)

    @Before
    fun setUp() {
        val response = UnitTestUtils.readJsonFile("user.json")
        userResponse = GsonBuilder().create().fromJson(response, UserResponse::class.java)
        expectedSuccessState = UserDataState.Success(userResponse)
    }

    @Test
    fun `when network succeed, emit data success state`() {
        Mockito.`when`(api.logInUser(a_user_email, a_user_password, a_user_idfa)).thenReturn(Single.just(userResponse))

        userUseCase.getUserDataState(a_user_email, a_user_password).subscribe { dataState ->
            state = dataState
        }

        Assert.assertEquals(state, expectedSuccessState)
    }

    @Test
    fun `when network fails, emit error data state`() {
        Mockito.`when`(api.logInUser(a_wrong_email, a_wrong_password, a_user_idfa))
            .thenReturn(Single.error(Throwable(a_user_error_message)))

        userUseCase.getUserDataState(a_wrong_email, a_wrong_password).subscribe { dataState ->
            state = dataState
        }

        Assert.assertEquals(state, expectedErrorState)
    }

    // Need test for wrong pw, wrong email etc...
}