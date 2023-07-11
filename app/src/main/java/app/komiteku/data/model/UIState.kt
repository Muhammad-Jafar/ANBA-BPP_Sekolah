package app.komiteku.data.model

sealed class RequestState<out R> private constructor() {
    data class Success<out T>(val data: T) : RequestState<T>()
    data class Error(val error: String) : RequestState<Nothing>()
    object Loading : RequestState<Nothing>()
}

sealed class BillState private constructor() {
    data class Success(val data: BillResult) : BillState()
    data class Error(val error: String) : BillState()
    object Loading : BillState()
}

sealed class ListTransactionState private constructor() {
    data class Success(val data: List<ListTransactionResult>) : ListTransactionState()
    data class Error(val error: String) : ListTransactionState()
    object Loading : ListTransactionState()
}

sealed class TransactionState private constructor() {
    data class Success(val data: String) : TransactionState()
    data class Error(val error: String) : TransactionState()
    object Loading : TransactionState()
}
