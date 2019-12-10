package com.example.minimoneybox.fragment

import com.example.minimoneybox.utils.FormValidator
import org.junit.Test
import org.mockito.Mockito

private const val a_user_email = "mockito@moneybox.com"
private const val a_user_password = "mockito@moneybox.com"

class LoginFragmentTest {

    private val formValidator = Mockito.mock(FormValidator::class.java)

    @Test
    fun `given valid email then emits valid result`() {
        Mockito.`when`(formValidator.validateEmail(a_user_email))
            .thenReturn(FormValidator.ValidationResult.VALID)

        assert(validEmail(a_user_email))
    }

    @Test
    fun `when invalid email then emits invalid result`() {
        Mockito.`when`(formValidator.validateEmail(a_user_email))
            .thenReturn(FormValidator.ValidationResult.INVALID)

        assert(!validEmail(a_user_email))
    }

    @Test
    fun `when valid password then emits valid result`() {
        Mockito.`when`(formValidator.validatePassword(a_user_password))
            .thenReturn(FormValidator.ValidationResult.VALID)

        assert(validPassword(a_user_password))
    }

    @Test
    fun `when valid password then emits invalid result`() {
        Mockito.`when`(formValidator.validatePassword(a_user_password))
            .thenReturn(FormValidator.ValidationResult.INVALID)

        assert(!validPassword(a_user_password))
    }

    private fun validEmail(email: String): Boolean {
        return formValidator.validateEmail(email) == FormValidator.ValidationResult.VALID
    }

    private fun validPassword(password: String): Boolean {
        return formValidator.validatePassword(password) == FormValidator.ValidationResult.VALID
    }

}