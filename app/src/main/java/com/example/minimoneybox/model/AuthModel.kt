package com.example.minimoneybox.model

import com.example.minimoneybox.data.error.SignInErrorResponse
import com.example.minimoneybox.data.error.SignInValidationResponse

class AuthModel {

//    class SignInException(error: SignInErrorResponse) : Throwable()

    class SignInException(cause: SignInErrorResponse)

    class SignInValidationException(error: SignInValidationResponse)

    class UnknownException(error: String?)

}