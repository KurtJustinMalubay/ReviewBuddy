package com.example.reviewbuddy.screens.register

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.reviewbuddy.R
import com.example.reviewbuddy.screens.login.LoginActivity

class RegisterActivity : Activity(), RegisterContract.View {

    private lateinit var presenter: RegisterContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_layout)

        presenter = RegisterPresenter(this)

        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            presenter.onSubmitClicked()
        }
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
