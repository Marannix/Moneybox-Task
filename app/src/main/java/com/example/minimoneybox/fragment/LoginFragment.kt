package com.example.minimoneybox.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.data.error.SignInErrorResponse
import com.example.minimoneybox.data.error.SignInValidationResponse
import com.example.minimoneybox.design.FullscreenLoadingDialog
import com.example.minimoneybox.model.AuthModel
import com.example.minimoneybox.state.UserViewState
import com.example.minimoneybox.viewmodel.UsersViewModel
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_login.*
import retrofit2.HttpException

/**
 * A login screen that offers login via email/password.
 */
class LoginFragment : BaseFragment() {

    interface OnLoginSuccessListener {
        fun onLoginSuccess()
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

    fun attach(listener: OnLoginSuccessListener) {
        this.listener = listener
    }

    private val disposables = CompositeDisposable()
    private lateinit var loadingDialog: Dialog
    private var listener: OnLoginSuccessListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private lateinit var userViewModel: UsersViewModel

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init() {
        loadingDialog = FullscreenLoadingDialog(requireContext()).apply {
            setCanceledOnTouchOutside(false)
        }
        userViewModel =        ViewModelProviders.of(this, viewModelFactory)
            .get(UsersViewModel::class.java)
        setupViews()
        loginTimeSaver()
    }

    private fun setupViews() {
        btn_sign_in.setOnClickListener {
            animation.playAnimation()
            userViewModel.getUserInformation(et_email.text.toString(), et_password.text.toString())

            userViewModel.viewState.observe(this, Observer {
                when (it) {
                    UserViewState.Loading -> {
                        loadingDialog.show()
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is UserViewState.ShowUser -> {
                        loadingDialog.dismiss()
                        Toast.makeText(requireContext(), "Launching Dashboard", Toast.LENGTH_SHORT).show()
                        storeUserInformation(it.user.session.bearerToken)
                        listener?.onLoginSuccess()
                    }
                    is UserViewState.ShowError -> {
                        loadingDialog.dismiss()
                        // Need to collect errors
                        //{"Name":"Login failed","Message":"Incorrect email address or password. Please check and try again.","ValidationErrors":[]}
                        if (it.errorMessage is HttpException) {
                            if (it.errorMessage.code() == 401) {
                                val signInError = Gson().fromJson<SignInErrorResponse>(
                                    it.errorMessage.response().errorBody()?.charStream(),
                                    SignInErrorResponse::class.java
                                )
                                Toast.makeText(requireContext(), signInError.message, Toast.LENGTH_SHORT).show()
                                AuthModel.SignInException(signInError)

                            } else if (it.errorMessage.code() == 400) {
                                val signInValidationError = Gson().fromJson<SignInValidationResponse>(
                                    it.errorMessage.response().errorBody()?.charStream(),
                                    SignInValidationResponse::class.java
                                )

                                Toast.makeText(requireContext(), signInValidationError.name, Toast.LENGTH_SHORT).show()

                                AuthModel.SignInValidationException(signInValidationError)
                            }

                        }

                    }
                }
            })
        }

    }

    private fun storeUserInformation(bearerToken: String) {
        userPreference.setUserFullName(et_name.text.toString())
        userPreference.setToken("Bearer $bearerToken")
        userPreference.setUserHasLoggedIn(true)
    }


    private fun loginTimeSaver() {
        // Long press login button to populate the fields
        btn_sign_in.setOnLongClickListener {
            et_email.setText("androidtest@moneyboxapp.com")
            et_password.setText("P455word12")
            et_name.setText("Money")
            true
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
