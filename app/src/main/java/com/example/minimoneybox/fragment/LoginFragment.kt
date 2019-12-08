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
import com.example.minimoneybox.viewmodel.UsersViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_login.*

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
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
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
