package com.example.reviewbuddy.screens.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.reviewbuddy.R
import com.example.reviewbuddy.screens.decks.DecksActivity
import com.example.reviewbuddy.screens.login.LoginActivity

class ProfileActivity : Activity(), ProfileContract.View {

    private lateinit var presenter: ProfileContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)

        presenter = ProfilePresenter(this)

        val menuAccountSettings = findViewById<android.view.View>(R.id.menuAccountSettings)
        val menuNotifications = findViewById<android.view.View>(R.id.menuNotifications)
        val menuLogout = findViewById<android.view.View>(R.id.menuLogout)

        val navHome = findViewById<android.view.View>(R.id.navHome)
        val navDecks = findViewById<android.view.View>(R.id.navDecks)
        val navProfile = findViewById<android.view.View>(R.id.navProfile)

        presenter.loadProfile()

        menuAccountSettings.setOnClickListener { presenter.onSettingsClicked() }
        menuNotifications.setOnClickListener { presenter.onNotificationsClicked() }
        menuLogout.setOnClickListener { presenter.onLogoutClicked() }

        // Pressing Home pops Profile off the stack — reveals Dashboard underneath
        navHome.setOnClickListener { finish() }

        // Pressing Decks: pop Profile, then open Decks
        navDecks.setOnClickListener { presenter.onDecksClicked() }

        // Already on Profile
        navProfile.setOnClickListener { /* Already here */ }
    }

    override fun showProfileDetails(
        firstName: String,
        lastName: String,
        course: String,
        deckCount: Int,
        cardCount: Int
    ) {
        findViewById<android.widget.TextView>(R.id.textviewProfileName).text = firstName
        findViewById<android.widget.TextView>(R.id.textviewProfileSubtitle).text = course
        findViewById<android.widget.TextView>(R.id.textviewTotalDecks).text = deckCount.toString()
        findViewById<android.widget.TextView>(R.id.textviewCardsMastered).text = cardCount.toString()
    }

    // finish() pops Profile — Dashboard is already in the back stack below
    override fun navigateToDashboard() {
        finish()
    }

    override fun navigateToDecks() {
        val intent = Intent(this, DecksActivity::class.java)
        startActivity(intent)
        overridePendingTransition(0, 0)
        finish()
    }

    override fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    override fun showComingSoonMessage(feature: String) {
        Toast.makeText(this, "$feature coming soon!", Toast.LENGTH_SHORT).show()
    }
}
