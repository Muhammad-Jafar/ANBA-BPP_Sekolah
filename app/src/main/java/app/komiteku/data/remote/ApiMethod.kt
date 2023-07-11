package app.komiteku.data.remote

import app.komiteku.data.model.BillResponse
import app.komiteku.data.model.ListTransactionResponse
import app.komiteku.data.model.LoginResponse
import app.komiteku.data.model.PaymentResponse
import app.komiteku.data.model.TransactionResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiMethod {

    @FormUrlEncoded
    @POST("login/student")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): LoginResponse

    @GET("billings/{id}")
    suspend fun getBillings(
        @Header("Authorization") Bearer: String,
        @Path("id") id: Int,
    ): BillResponse

    @FormUrlEncoded
    @POST("transaction/{id}")
    suspend fun getTransaction(
        @Header("Authorization") Bearer: String,
        @Path("id") id: Int,
        @Field("limit") limit: Int? = null,
    ): ListTransactionResponse

    @FormUrlEncoded
    @POST("transaction/pay")
    suspend fun doPayment(
        @Header("Authorization") Bearer: String,
        @Field("bill_id") billId: Int,
        @Field("student_id") studentId: Int,
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("amount") amount: Int,
        @Field("note") note: String,
    ): PaymentResponse

    @FormUrlEncoded
    @POST("transaction/status")
    suspend fun cekStatus(
        @Header("Authorization") Bearer: String,
        @Field("transaction_id") transactionId: String,
    ): TransactionResponse
}
