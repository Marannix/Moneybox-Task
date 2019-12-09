package com.example.minimoneybox.utils

import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

class FormValidator @Inject constructor(){

    enum class ValidationResult {
        EMPTY,
        VALID,
        INVALID
    }

    private var EMAIL_PATTERN =
        "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

    private var PASSWORD_PATTERN =
        "^[a-zA-Z@#$%]\\w{5,19}$"

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

    private fun checkPasswordIsValid(password: String): Boolean {
        // Password needs to have uppercase etc...
//        return Pattern.matches(PASSWORD_PATTERN, password) && password.trim().length >= 5
        return password.trim().length >= 5
    }
}