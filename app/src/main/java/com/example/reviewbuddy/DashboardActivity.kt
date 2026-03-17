package com.example.reviewbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)
        // val username = getIntent().getStringExtra("Username")
        // val password = getIntent().getStringExtra("Password")
        
        val buttonProfile = findViewById<Button>(R.id.buttonProfile)
        val buttonLogout = findViewById<Button>(R.id.buttonLogout)

        buttonProfile.setOnClickListener {
            openProfileScreen()
        }

        buttonLogout.setOnClickListener {
            executeLogout()
        }
    }

    private fun openProfileScreen() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun executeLogout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
