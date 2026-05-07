package com.example.reviewbuddy.screens.profile

class ProfilePresenter(private val view: ProfileContract.View) : ProfileContract.Presenter {

    private val model = ProfileModel()

    override fun loadProfile() {
        val user = model.getUserProfile()
        if (user != null) {
            val initial = if (user.firstName.isNotBlank()) user.firstName.substring(0, 1).uppercase() else "U"
            view.showProfileDetails(
                firstName = user.firstName,
                middleName = user.middleName,
                lastName = user.lastName,
                username = user.username,
                email = user.email,
                initial = initial
            )
        } else {
            // Fallback or handle missing user (e.g. redirect to login)
            view.navigateToDashboard()
        }
    }

    override fun onBackToDashboardClicked() {
        view.navigateToDashboard()
    }

    override fun onBackClicked() {
        view.navigateToDashboard()
    }
}
