package com.example.reviewbuddy.screens.login

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {
    override fun attemptLogin(username: String, password: String) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showLoginError("Please enter both username and password")
        } else {
            view.navigateToDashboard()
        }
    }

    override fun onRegisterClicked() {
        view.navigateToRegister()
    }
}
