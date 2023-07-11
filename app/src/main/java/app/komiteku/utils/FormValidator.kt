package app.komiteku.utils

import android.util.Patterns

object FormValidator {
    fun isEmailValid(email: String): Boolean = !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    fun isPasswordValid(password: String): Boolean = password.length < 5
}
