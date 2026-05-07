package com.example.reviewbuddy.screens.login

class LoginPresenter(private val view: LoginContract.View) : LoginContract.Presenter {
    private val model = LoginModel()

    override fun attemptLogin(username: String, password: String) {
        if (model.authenticate(username, password)) {
            view.navigateToDashboard()
        } else {
            view.showLoginError("Please enter a valid username and password")
        }
    }

    override fun onRegisterClicked() {
        view.navigateToRegister()
    }
}
