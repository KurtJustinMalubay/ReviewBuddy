package com.example.reviewbuddy.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.example.reviewbuddy.R
import com.example.reviewbuddy.dashboard.DashboardActivity

class ProfileActivity : Activity(), ProfileContract.View {

    private lateinit val presenter: ProfileContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)

        presenter = ProfilePresenter(this)

        val buttonBackDashboard = findViewById<Button>(R.id.buttonBackDashboard)

        buttonBackDashboard.setOnClickListener {
            presenter.onBackToDashboardClicked()
        }
    }

    override fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}