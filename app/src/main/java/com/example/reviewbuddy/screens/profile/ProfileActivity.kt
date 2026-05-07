package com.example.reviewbuddy.screens.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.reviewbuddy.R
import com.example.reviewbuddy.screens.dashboard.DashboardActivity

class ProfileActivity : Activity(), ProfileContract.View {

    private lateinit var presenter: ProfileContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)

        presenter = ProfilePresenter(this)

        val buttonBackDashboard = findViewById<Button>(R.id.buttonBackDashboard)
        //val buttonBack = findViewById<Button>(R.id.buttonBack)

        presenter.loadProfile()

        buttonBackDashboard.setOnClickListener {
            presenter.onBackToDashboardClicked()
        }

//        buttonBack.setOnClickListener {
//            presenter.onBackClicked()
//        }
    }

    override fun showProfileDetails(
        firstName: String,
        middleName: String,
        lastName: String,
        username: String,
        email: String,
        initial: String
    ) {
        val middleInitial = if (middleName.isNotBlank()) "${middleName.substring(0, 1)}." else ""
        val fullName = "$firstName $middleInitial $lastName".trim().replace("  ", " ")

        findViewById<android.widget.TextView>(R.id.textviewProfileName).text = fullName
        findViewById<android.widget.TextView>(R.id.textviewUsername).text = "Username: $username"
        findViewById<android.widget.TextView>(R.id.textviewFirstName).text = "First Name: $firstName"
        findViewById<android.widget.TextView>(R.id.textviewMiddleName).text = "Middle Name: $middleName"
        findViewById<android.widget.TextView>(R.id.textviewLastName).text = "Last Name: $lastName"
        findViewById<android.widget.TextView>(R.id.textviewEmail).text = "Email: $email"
        
        findViewById<android.widget.TextView>(R.id.textviewProfileInitial).text = initial
    }

    override fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
