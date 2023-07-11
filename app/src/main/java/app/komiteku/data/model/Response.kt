package app.komiteku.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/* Login form */
data class LoginResponse(
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String,
    @field:SerializedName("loginResult") val loginResult: LoginResult,
)

@Parcelize
data class LoginResult(
    @field:SerializedName("token") val token: String,
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("student_identification_number") val nis: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("school_major") val jurusan: String,
    @field:SerializedName("school_class") val kelas: String,
    @field:SerializedName("phone_number") val hp: String,
) : Parcelable

/* Home form */
data class ListTransactionResponse(
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String,
    @field:SerializedName("listTransactionResult") val listTransactionResult: List<ListTransactionResult> = arrayListOf(),
)

@Parcelize
data class ListTransactionResult(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("transaction_code") val transactionCode: String,
    @field:SerializedName("amount") val amount: Int,
    @field:SerializedName("paid_on") val tanggal: String,
    @field:SerializedName("is_paid") val status: String,
    @field:SerializedName("note") val note: String,
) : Parcelable

/* Bill form */
data class BillResponse(
    @field:SerializedName("billResult") val billResult: BillResult,
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String
)

@Parcelize
data class BillResult(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("billings") val billings: Int,
    @field:SerializedName("recent_bill") val recentBill: Int,
) : Parcelable


/* Payment form */
data class PaymentResponse(
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String,
    @field:SerializedName("paymentResult") val paymentResult: PaymentResult,
)

@Parcelize
data class PaymentResult(
    @field:SerializedName("request_id") val transactionId: String,
    @field:SerializedName("amount") val amount: Int,
    @field:SerializedName("va_number") val va_number: String,
    @field:SerializedName("expiry_time") val expire: String,
) : Parcelable

/* Transaction form */
data class TransactionResponse(
    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String,
    @field:SerializedName("transaction_status") val status: String,
)
