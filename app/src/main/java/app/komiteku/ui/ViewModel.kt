package app.komiteku.ui

import androidx.lifecycle.ViewModel
import app.komiteku.data.local.LoginSession
import app.komiteku.data.model.BillResult
import app.komiteku.data.model.LoginRequest
import app.komiteku.data.model.PaymentRequest
import app.komiteku.data.model.PaymentResult
import app.komiteku.data.repo.AuthRepo
import app.komiteku.data.repo.MainRepo
import app.komiteku.utils.runInBackground

class AuthViewModel(private val repo: AuthRepo) : ViewModel() {
    fun doLogin(request: LoginRequest) = repo.doLogin(request)
    fun saveSession(session: LoginSession) = runInBackground { repo.saveSession(session) }
}

class MainViewModel(private val repo: MainRepo) : ViewModel() {
    val getToken = repo.getToken
    val getLoginAt = repo.getLoginAt

    val getNis = repo.getNis
    val getName = repo.getName
    val getKelas = repo.getKelas
    val getJurusan = repo.getJurusan
    val getPhone = repo.getPhone
    val getEmail = repo.getEmail

    val getBillId = repo.getBillId
    val getBillBillings = repo.getBillings
    val getBillRecentBill = repo.getRecentBill

    val transactionId = repo.getTransactionId
    val vaNumber = repo.getVaNumber
    val amount = repo.getAmount
    val expire = repo.getExpire

    val billings = repo.getBill()
    val getTransactionStatus = repo.cekStatusPayment()

    fun saveBillings(response: BillResult) = runInBackground { repo.saveBillings(response) }

    fun getListTransaction(limit: Int? = null) = repo.getListTransaction(limit)
}

class PaymentViewModel(private val repo: MainRepo) : ViewModel() {
    val getBillId = repo.getBillId
    val getBillBillings = repo.getBillings
    val getBillRecentBill = repo.getRecentBill

    val getId = repo.getId
    val getName = repo.getName
    val getEmail = repo.getEmail

    fun doPayment(request: PaymentRequest) = repo.doPayment(request)
    fun saveVaNumber(result: PaymentResult) = runInBackground { repo.saveVaNumber(result) }
}
