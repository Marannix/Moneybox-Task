package com.example.minimoneybox.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.minimoneybox.R
import com.example.minimoneybox.UserApi
import com.example.minimoneybox.state.UserViewState
import com.example.minimoneybox.viewmodel.UsersViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity() {

    private val disposables = CompositeDisposable()
    @Inject
    lateinit var userApi: UserApi
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val userViewModel: UsersViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)
            .get(UsersViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupViews()
    }

    private fun setupViews() {
        btn_sign_in.setOnClickListener {
            animation.playAnimation()
            userViewModel.getUserInformation(et_email.text.toString(), et_password.text.toString())
            userViewModel.viewState.observe(this, Observer {
                when (it) {
                    UserViewState.Loading -> {
                        Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
                    }
                    is UserViewState.ShowUser -> {
                        Toast.makeText(this, "Launching Dashboard", Toast.LENGTH_SHORT).show()
                        launchDashboard()
                    }
                    is UserViewState.ShowError -> {
                        // Need to collect errors
                        //{"Name":"Login failed","Message":"Incorrect email address or password. Please check and try again.","ValidationErrors":[]}
                        Toast.makeText(this, it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        // Long press login button to populate the fields
        btn_sign_in.setOnLongClickListener {
            et_email.setText("androidtest@moneyboxapp.com")
            et_password.setText("P455word12")
            et_name.setText("Money")
            true
        }
    }

    private fun launchDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
