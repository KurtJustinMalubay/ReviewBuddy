package com.example.reviewbuddy.register

interface RegisterContract {
    interface View {
        fun navigateToLogin()
    }

    interface Presenter {
        fun onSubmitClicked()
    }
}
