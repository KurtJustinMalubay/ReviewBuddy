package com.example.reviewbuddy.screens.register

interface RegisterContract {
    interface View {
        fun navigateToLogin()
        fun showRegisterSuccess()
        fun showRegisterError(message: String)
    }

    interface Presenter {
        fun attemptRegister(
            username: String,
            firstName: String,
            middleName: String,
            lastName: String,
            email: String,
            pass: String,
            passConfirm: String
        )
    }
}
