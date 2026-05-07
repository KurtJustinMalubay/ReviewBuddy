package com.example.reviewbuddy.register

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {
    override fun onSubmitClicked() {
        view.navigateToLogin()
    }
}
