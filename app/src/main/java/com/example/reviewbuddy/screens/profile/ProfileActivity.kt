package com.example.reviewbuddy.screens.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.reviewbuddy.R
import com.example.reviewbuddy.screens.decks.DecksActivity
import com.example.reviewbuddy.screens.login.LoginActivity

/**
 * Controller Activity representing the user profile section.
 * Manages logging out, updating display details, and dynamic avatar picture picker actions.
 */
class ProfileActivity : Activity(), ProfileContract.View {

    private lateinit var presenter: ProfileContract.Presenter
    private val IMAGE_PICK_REQUEST = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile_layout)

        presenter = ProfilePresenter(this)

        val menuLogout = findViewById<android.view.View>(R.id.menuLogout)
        val imageProfileAvatar = findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.imageProfileAvatar)

        val navHome = findViewById<android.view.View>(R.id.navHome)
        val navDecks = findViewById<android.view.View>(R.id.navDecks)
        val navProfile = findViewById<android.view.View>(R.id.navProfile)

        presenter.loadProfile()

        menuLogout.setOnClickListener { presenter.onLogoutClicked() }

        // Pressing Home pops Profile off the stack — reveals Dashboard underneath
        navHome.setOnClickListener { finish() }

        // Pressing Decks: pop Profile, then open Decks
        navDecks.setOnClickListener { presenter.onDecksClicked() }

        // Already on Profile
        navProfile.setOnClickListener { /* Already here */ }

        // Click Avatar to pick custom image
        imageProfileAvatar.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "image/*"
            }
            startActivityForResult(intent, IMAGE_PICK_REQUEST)
        }
    }

    /**
     * Intercepts picked profile photo URI, persists document permissions, and saves avatar URI.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST && resultCode == RESULT_OK && data != null) {
            val uri = data.data
            if (uri != null) {
                try {
                    // Take persistable permission to keep access across reboots/app restarts
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: Exception) {
                    // Fail-safe for non-persistable providers
                }

                val loggedUser = com.example.reviewbuddy.app.ReviewBuddyApp.userRepository.getLoggedInUser()
                if (loggedUser != null) {
                    com.example.reviewbuddy.app.ReviewBuddyApp.userRepository.updateUserAvatar(loggedUser.username, uri.toString())
                    
                    // Instantly refresh avatar layout
                    presenter.loadProfile()
                }
            }
        }
    }

    /**
     * Displays complete profile details and updates avatar photo orfallback circular shapes.
     */
    override fun showProfileDetails(
        username: String,
        email: String,
        fullName: String,
        firstName: String,
        middleName: String,
        lastName: String,
        course: String
    ) {
        findViewById<android.widget.TextView>(R.id.textviewProfileName).text = firstName
        findViewById<android.widget.TextView>(R.id.textviewProfileSubtitle).text = course

        // Bind the detailed account fields
        findViewById<android.widget.TextView>(R.id.textviewProfileUsernameValue).text = username
        findViewById<android.widget.TextView>(R.id.textviewProfileEmailValue).text = email
        findViewById<android.widget.TextView>(R.id.textviewProfileFullNameValue).text = fullName
        findViewById<android.widget.TextView>(R.id.textviewProfileCourseValue).text = course

        // Dynamically load picked profile photo or fallback placeholder
        val imageProfileAvatar = findViewById<androidx.appcompat.widget.AppCompatImageView>(R.id.imageProfileAvatar)
        val loggedUser = com.example.reviewbuddy.app.ReviewBuddyApp.userRepository.getLoggedInUser()
        if (loggedUser != null && loggedUser.avatarUri != null) {
            try {
                imageProfileAvatar.setImageURI(android.net.Uri.parse(loggedUser.avatarUri))
                imageProfileAvatar.imageTintList = null // Clear blue tint
                imageProfileAvatar.setPadding(0, 0, 0, 0) // No padding for full crop image
            } catch (e: Exception) {
                // Inaccessible URI fallback
                imageProfileAvatar.setImageResource(R.drawable.ic_profile)
                imageProfileAvatar.imageTintList = android.content.res.ColorStateList.valueOf(resources.getColor(R.color.primary))
                val p = (12 * resources.displayMetrics.density).toInt()
                imageProfileAvatar.setPadding(p, p, p, p)
            }
        } else {
            imageProfileAvatar.setImageResource(R.drawable.ic_profile)
            imageProfileAvatar.imageTintList = android.content.res.ColorStateList.valueOf(resources.getColor(R.color.primary))
            val p = (12 * resources.displayMetrics.density).toInt()
            imageProfileAvatar.setPadding(p, p, p, p)
        }
    }

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
        overridePendingTransition(0, 0)
        finishAffinity()
    }

    override fun showComingSoonMessage(feature: String) {
        Toast.makeText(this, "$feature coming soon!", Toast.LENGTH_SHORT).show()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}
