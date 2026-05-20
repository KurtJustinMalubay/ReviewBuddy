package com.example.reviewbuddy.screens.profile

class ProfilePresenter(private val view: ProfileContract.View) : ProfileContract.Presenter {

    private val model = ProfileModel()

    override fun loadProfile() {
        val user = model.getUserProfile()
        if (user != null) {
            val fullName = listOf(user.firstName, user.middleName, user.lastName)
                .filter { it.isNotBlank() }
                .joinToString(" ")
            view.showProfileDetails(
                username = user.username,
                email = user.email,
                fullName = fullName,
                firstName = user.firstName,
                middleName = user.middleName,
                lastName = user.lastName,
                course = if (user.course.isNotBlank()) user.course else "Student"
            )
        } else {
            view.navigateToDashboard()
        }
    }

    override fun onBackToDashboardClicked() {
        view.navigateToDashboard()
    }

    override fun onLogoutClicked() {
        view.navigateToLogin()
    }

    override fun onSettingsClicked() {
        view.showComingSoonMessage("Account Settings")
    }

    override fun onNotificationsClicked() {
        view.showComingSoonMessage("Notifications")
    }

    override fun onHomeClicked() {
        view.navigateToDashboard()
    }

    override fun onDecksClicked() {
        view.navigateToDecks()
    }
}
