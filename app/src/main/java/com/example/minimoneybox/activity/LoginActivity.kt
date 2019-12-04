package com.example.minimoneybox.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.minimoneybox.R
import com.example.minimoneybox.User
import com.example.minimoneybox.UserApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : BaseActivity() {

    private val disposables = CompositeDisposable()
    @Inject lateinit var userApi: UserApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupViews()
    }

    private fun setupViews() {
        btn_sign_in.setOnClickListener {
            animation.playAnimation()
            val disposable =
                userApi.logInUser(et_email.text.toString(), et_password.text.toString(), "ANYTHING")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Toast.makeText(this, it.user.firstName, Toast.LENGTH_SHORT).show()
                            launchDashboard(it.user)
                        },
                        {
                            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                            //{"Name":"Login failed","Message":"Incorrect email address or password. Please check and try again.","ValidationErrors":[]}
                        }
                    )

            disposables.add(disposable)
        }

        btn_sign_in.setOnLongClickListener {
            et_email.setText("androidtest@moneyboxapp.com")
            et_password.setText("P455word12")
            et_name.setText("Money")
            true
        }
    }

    private fun launchDashboard(user: User) {
        val intent = Intent(this, DashboardActivity::class.java)
        //TODO: Store user inside a database
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }
}
