package com.example.reviewbuddy.screens.profile

class ProfilePresenter(private val view: ProfileContract.View) : ProfileContract.Presenter {
    override fun onBackToDashboardClicked() {
        view.navigateToDashboard()
    }
}
