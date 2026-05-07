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

    override fun showProfileName(name: String) {
        findViewById<android.widget.TextView>(R.id.textviewProfileName).text = name
    }

    override fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}
