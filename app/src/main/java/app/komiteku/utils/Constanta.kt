package app.komiteku.utils

import androidx.recyclerview.widget.DiffUtil
import app.komiteku.data.model.ListTransactionResult

object Constanta {
    /* API REQUEST TIME */
    const val READ_TIME_OUT = 5.toLong()
    const val WRITE_TIME_OUT = 5.toLong()
    const val CONNECT_TIME_OUT = 5.toLong()

    /* DEFAULT VALUE */
    const val Unauthorized = "Akun tidak terdaftar"
    const val Uncaught = "Terjadi kesalahan, silahkan coba lagi"

    /* Diff Callback RecycleView for TransactionItemEntity*/
    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListTransactionResult>() {
        override fun areItemsTheSame(
            oldItem: ListTransactionResult,
            newItem: ListTransactionResult
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ListTransactionResult,
            newItem: ListTransactionResult
        ): Boolean = oldItem.id == newItem.id
    }
}
