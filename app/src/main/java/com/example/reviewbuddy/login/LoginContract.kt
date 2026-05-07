package com.example.reviewbuddy.login

interface LoginContract {
    interface View {
        fun showLoginError(message: String)
        fun navigateToDashboard()
        fun navigateToRegister()
    }

    interface Presenter {
        fun attemptLogin(username: String, password: String)
        fun onRegisterClicked()
    }
}
