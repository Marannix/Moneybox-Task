package com.example.minimoneybox.repository

import com.example.minimoneybox.api.UserApi
import com.example.minimoneybox.data.UsersDao
import com.example.minimoneybox.data.user.UserResponse
import com.example.minimoneybox.utils.UnitTestUtils.Companion.readJsonFile
import com.google.gson.GsonBuilder
import io.reactivex.Single
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

const val a_user_email = "mockito@moneybox.com"
const val a_user_password = "mockito@moneybox.com"
const val a_user_idfa = "ANYTHING"

class UsersRepositoryTest {

    private val api = Mockito.mock(UserApi::class.java)
    private val dao = Mockito.mock(UsersDao::class.java)

    private lateinit var usersRepository: UsersRepository
    private lateinit var userResponse: UserResponse

    private lateinit var result: UserResponse

    @Before
    fun setUp() {
        val response = readJsonFile("user.json")
        userResponse = GsonBuilder().create().fromJson(response, UserResponse::class.java)
        usersRepository = UsersRepository(dao, api)
    }

    @Test
    fun `when calling the network, parse correctly the response`() {
        `when`(api.logInUser(a_user_email, a_user_password, a_user_idfa)).thenReturn(Single.just(userResponse))

        usersRepository.getUsers(a_user_email, a_user_password)
            .blockingSubscribe {
                result = it!!
            }

        assertThat(result).isEqualTo(userResponse)
    }
}