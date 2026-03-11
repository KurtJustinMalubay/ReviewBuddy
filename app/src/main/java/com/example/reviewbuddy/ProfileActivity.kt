package com.example.reviewbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)

        val buttonBackDashboard = findViewById<Button>(R.id.buttonBackDashboard)

        buttonBackDashboard.setOnClickListener {
            returnToDashboard()
        }
    }

    private fun returnToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}