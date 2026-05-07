package com.example.reviewbuddy.screens.register

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {
    private val model = RegisterModel()

    override fun attemptRegister(username: String, pass: String, passConfirm: String) {
        if (model.validateRegistration(username, pass, passConfirm)) {
            view.showRegisterSuccess()
            view.navigateToLogin()
        } else {
            view.showRegisterError("Invalid input or passwords do not match")
        }
    }
}
