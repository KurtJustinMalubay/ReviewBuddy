package com.example.reviewbuddy.screens.register

import com.example.reviewbuddy.data.models.User

class RegisterPresenter(private val view: RegisterContract.View) : RegisterContract.Presenter {
    private val model = RegisterModel()

    override fun attemptRegister(
        username: String,
        firstName: String,
        middleName: String,
        lastName: String,
        email: String,
        course: String,
        pass: String,
        passConfirm: String
    ) {
        val validation = model.validateRegistration(username, firstName, lastName, email, pass, passConfirm)
        if (validation.first) {
            val user = User(username, pass, firstName, middleName, lastName, email, course)
            if (model.registerUser(user)) {
                view.showRegisterSuccess()
                view.navigateToLogin()
            } else {
                view.showRegisterError("Failed to register. Please try again.")
            }
        } else {
            view.showRegisterError(validation.second)
        }
    }
}
