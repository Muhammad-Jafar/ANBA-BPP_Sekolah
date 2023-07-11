package app.komiteku.data.repo

import android.content.Context
import app.komiteku.data.local.LoginSession
import app.komiteku.data.local.dataStore
import app.komiteku.data.model.BillResult
import app.komiteku.data.model.BillState
import app.komiteku.data.model.ListTransactionState
import app.komiteku.data.model.LoginRequest
import app.komiteku.data.model.PaymentRequest
import app.komiteku.data.model.PaymentResult
import app.komiteku.data.model.RequestState
import app.komiteku.data.model.TransactionState
import app.komiteku.data.remote.ApiMethod
import app.komiteku.utils.Constanta
import app.komiteku.utils.currentDate
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

open class Repository

class AuthRepo(private val context: Context, private val callApi: ApiMethod) : Repository() {

    fun doLogin(request: LoginRequest) = flow {
        val response = callApi.login(request.email, request.password)
        if (!response.error) emit(RequestState.Success(response.loginResult))
        else emit(RequestState.Error(Constanta.Unauthorized))
    }.onStart { emit(RequestState.Loading) }.catch { emit(RequestState.Error(Constanta.Uncaught)) }

    suspend fun saveSession(session: LoginSession) = context.dataStore.updateData {
        it.copy(
            loginAt = currentDate,
            token = session.token,
            id = session.id,
            nis = session.nis,
            name = session.name,
            kelas = session.kelas,
            jurusan = session.jurusan,
            email = session.email,
            hp = session.hp,
        )
    }
}

class MainRepo(private val pref: Context, private val callApi: ApiMethod) : Repository() {

    val getToken = pref.dataStore.data.map { it.token }
    val getLoginAt = pref.dataStore.data.map { it.loginAt }
    val getId = pref.dataStore.data.map { it.id }
    val getNis = pref.dataStore.data.map { it.nis }
    val getName = pref.dataStore.data.map { it.name }
    val getJurusan = pref.dataStore.data.map { it.jurusan }
    val getKelas = pref.dataStore.data.map { it.kelas }
    val getEmail = pref.dataStore.data.map { it.email }
    val getPhone = pref.dataStore.data.map { it.hp }

    val getBillId = pref.dataStore.data.map { it.billId }
    val getBillings = pref.dataStore.data.map { it.billings }
    val getRecentBill = pref.dataStore.data.map { it.recentBill }

    val getTransactionId = pref.dataStore.data.map { it.transactionId }
    val getAmount = pref.dataStore.data.map { it.amount }
    val getVaNumber = pref.dataStore.data.map { it.vaNumber }
    val getExpire = pref.dataStore.data.map { it.expireTime }

    suspend fun saveBillings(result: BillResult) = pref.dataStore.updateData {
        it.copy(billId = result.id, billings = result.billings, recentBill = result.recentBill)
    }

    suspend fun saveVaNumber(result: PaymentResult) = pref.dataStore.updateData {
        it.copy(
            transactionId = result.transactionId,
            amount = result.amount,
            vaNumber = result.va_number,
            expireTime = result.expire
        )
    }

    fun getBill() = flow {
        val token = pref.dataStore.data.map { it.token }.first()
        val id = pref.dataStore.data.map { it.id }.first()
        val response = callApi.getBillings("Bearer $token", id!!)

        if (!response.error) emit(BillState.Success(response.billResult))
        else emit(BillState.Error(response.message))
    }.onStart { emit(BillState.Loading) }.catch { emit(BillState.Error(Constanta.Uncaught)) }

    fun getListTransaction(limit: Int? = null) = flow {
        val token = pref.dataStore.data.map { it.token }.first()
        val id = pref.dataStore.data.map { it.id }.first()
        val response = callApi.getTransaction("Bearer $token", id!!, limit)

        if (!response.error) emit(ListTransactionState.Success(response.listTransactionResult))
        else emit(ListTransactionState.Error(response.message))
    }.onStart { emit(ListTransactionState.Loading) }
        .catch { emit(ListTransactionState.Error(Constanta.Uncaught)) }

    fun doPayment(req: PaymentRequest) = flow {
        val token = pref.dataStore.data.map { it.token }.first()
        val response = callApi.doPayment(
            "Bearer $token",
            req.bill_id,
            req.student_id,
            req.name,
            req.email,
            req.amount,
            req.note
        )
        if (!response.error) emit(RequestState.Success(response.paymentResult))
        else emit(RequestState.Error(response.message))
    }.onStart { emit(RequestState.Loading) }.catch { emit(RequestState.Error(Constanta.Uncaught)) }

    fun cekStatusPayment() = flow {
        val token = pref.dataStore.data.map { it.token }.first()
        val transactionId = pref.dataStore.data.map { it.transactionId }.first()
        val response = callApi.cekStatus("Bearer $token", transactionId!!)
        if (!response.error) emit(TransactionState.Success(response.status))
        else emit(TransactionState.Error(response.message))
    }.onStart { emit(TransactionState.Loading) }
        .catch { emit(TransactionState.Error(Constanta.Uncaught)) }
}
