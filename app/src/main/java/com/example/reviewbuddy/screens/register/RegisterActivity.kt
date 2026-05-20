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

        val edittextUsername = findViewById<android.widget.EditText>(R.id.edittextUsername)
        val edittextFirstName = findViewById<android.widget.EditText>(R.id.edittextFirstName)
        val edittextMiddleName = findViewById<android.widget.EditText>(R.id.edittextMiddleName)
        val edittextLastName = findViewById<android.widget.EditText>(R.id.edittextLastName)
        val edittextCourse = findViewById<android.widget.EditText>(R.id.edittextCourse)
        val edittextEmail = findViewById<android.widget.EditText>(R.id.edittextEmail)
        val edittextPassword = findViewById<android.widget.EditText>(R.id.edittextPassword)
        val edittextRePassword = findViewById<android.widget.EditText>(R.id.edittextRePassword)
        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val user = edittextUsername.text.toString()
            val firstName = edittextFirstName.text.toString()
            val middleName = edittextMiddleName.text.toString()
            val lastName = edittextLastName.text.toString()
            val course = edittextCourse.text.toString()
            val email = edittextEmail.text.toString()
            val pass = edittextPassword.text.toString()
            val pass2 = edittextRePassword.text.toString()
            presenter.attemptRegister(user, firstName, middleName, lastName, email, course, pass, pass2)
        }
    }

    override fun showRegisterSuccess() {
        android.widget.Toast.makeText(this, "Registration Successful!", android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun showRegisterError(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}
