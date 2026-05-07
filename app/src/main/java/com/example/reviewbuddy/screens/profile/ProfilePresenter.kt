package com.example.reviewbuddy.screens.profile

class ProfilePresenter(private val view: ProfileContract.View) : ProfileContract.Presenter {

    private val model = ProfileModel()

    override fun loadProfile() {
        val user = model.getUserProfile()
        if (user != null) {
            view.showProfileDetails(
                firstName = user.firstName,
                lastName = user.lastName,
                course = user.course ?: "Student",
                deckCount = model.getTotalDecks(),
                cardCount = model.getTotalCards()
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
