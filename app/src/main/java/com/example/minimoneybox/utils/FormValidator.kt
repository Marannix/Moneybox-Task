package com.example.minimoneybox.utils

import java.util.regex.Pattern
import javax.inject.Inject

class FormValidator @Inject constructor(){

    enum class ValidationResult {
        EMPTY,
        VALID,
        INVALID
    }

    private var EMAIL_PATTERN =
        "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isEmpty() -> ValidationResult.EMPTY
            checkEmailAddressIsValid(email) -> ValidationResult.VALID
            else -> ValidationResult.INVALID
        }
    }

    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isEmpty() -> ValidationResult.EMPTY
            checkPasswordIsValid(password) -> ValidationResult.VALID
            else -> ValidationResult.INVALID
        }
    }

    private fun checkEmailAddressIsValid(email: String): Boolean {
        return Pattern.matches(EMAIL_PATTERN, email)
    }

    //Noticed the api returns a password error when no password has been sent to the backend
    private fun checkPasswordIsValid(password: String): Boolean {
        return password.trim().isNotEmpty()
    }
}