package app.komiteku.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class LoginRequest(val email: String, val password: String)

data class PaymentRequest(
    val bill_id: Int,
    val student_id: Int,
    val name: String,
    val email: String,
    val amount: Int,
    val note: String
)

@Parcelize
data class PaymentRequire(val amount: Int, val expire: String) : Parcelable