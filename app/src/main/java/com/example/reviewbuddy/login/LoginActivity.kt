package com.example.reviewbuddy.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.reviewbuddy.R
import com.example.reviewbuddy.dashboard.DashboardActivity
import com.example.reviewbuddy.register.RegisterActivity

class LoginActivity : Activity(), LoginContract.View {

    private lateinit val presenter: LoginContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)

        presenter = LoginPresenter(this)

        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textviewRegister = findViewById<TextView>(R.id.textviewRegister)

        val edittextUsername = findViewById<EditText>(R.id.edittextUsername)
        val edittextPassword = findViewById<EditText>(R.id.edittextPassword)

        buttonLogin.setOnClickListener {
            val username = edittextUsername.text.toString().trim()
            val password = edittextPassword.text.toString().trim()
            presenter.attemptLogin(username, password)
        }

        textviewRegister.setOnClickListener {
            presenter.onRegisterClicked()
        }
    }

    override fun showLoginError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    override fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}
