package com.example.reviewbuddy.screens.register

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {
    override fun onSubmitClicked() {
        view.navigateToLogin()
    }
}
