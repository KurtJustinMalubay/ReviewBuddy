package com.example.reviewbuddy.screens.register

interface RegisterContract {
    interface View {
        fun navigateToLogin()
    }

    interface Presenter {
        fun onSubmitClicked()
    }
}
