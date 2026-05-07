package com.example.reviewbuddy.screens.profile

class ProfilePresenter(private val view: ProfileContract.View) : ProfileContract.Presenter {

    private val model = ProfileModel()

    override fun loadProfile() {
        view.showProfileName(model.getUserProfile())
    }

    override fun onBackToDashboardClicked() {
        view.navigateToDashboard()
    }

    override fun onBackClicked() {
        view.navigateToDashboard()
    }
}
