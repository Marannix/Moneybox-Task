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
import com.example.minimoneybox.design.FullscreenLoadingDialog
import com.example.minimoneybox.state.UserViewState
import com.example.minimoneybox.utils.FormValidator
import com.example.minimoneybox.viewmodel.UsersViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject

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

    @Inject
    lateinit var formValidator: FormValidator

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    private val userViewModel: UsersViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(UsersViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init() {
        loadingDialog = FullscreenLoadingDialog(requireContext()).apply {
            setCanceledOnTouchOutside(false)
        }
        setupViews()
        loginTimeSaver()
    }

    private fun setupViews() {
        btn_sign_in.setOnClickListener {
            animation.playAnimation()
            loginValidationCheck(et_email.text.toString(), et_password.text.toString())
        }
    }

    private fun login() {
        userViewModel.getUserInformation(et_email.text.toString(), et_password.text.toString())
        userViewModel.viewState.observe(this, Observer {
            when (it) {
                UserViewState.Loading -> {
                    loadingDialog.show()
                }
                is UserViewState.ShowUser -> {
                    loadingDialog.dismiss()
                    storeUserInformation(it.user.session.bearerToken)
                    listener?.onLoginSuccess()
                }
                is UserViewState.ShowError -> {
                    loadingDialog.dismiss()
                    Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun loginValidationCheck(email: String, password: String) {
        //Would be nice if both checks are done together
        if (validUserInformation(email) && validPasswordInformation(password)) {
            login()
        }
    }

    private fun validUserInformation(email: String): Boolean {
        return when {
            formValidator.validateEmail(email) == FormValidator.ValidationResult.VALID -> {
                //Removes empty space created by error
                til_email.isErrorEnabled = false
                til_email.error = null
                true
            }
            formValidator.validateEmail(email) == FormValidator.ValidationResult.EMPTY -> {
                til_email.error = getString(R.string.empty_email)
                false
            }
            else -> {
                // Invalid
                til_email.error = getString(R.string.email_invalid)
                false
            }
        }
    }

    private fun validPasswordInformation(password: String): Boolean {
        return when {
            formValidator.validatePassword(password) == FormValidator.ValidationResult.VALID -> {
                //Removes empty space created by error
                til_password.isErrorEnabled = false
                til_password.error = null
                true
            }
            formValidator.validatePassword(password) == FormValidator.ValidationResult.EMPTY -> {
                til_password.error = getString(R.string.password_invalid)
                false
            }
            else -> {
                til_password.error = getString(R.string.password_invalid)
                false
            }
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
            et_email.setText(getString(R.string.test_email_address))
            et_password.setText(getString(R.string.test_password))
            et_name.setText(getString(R.string.test_full_name))
            true
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
