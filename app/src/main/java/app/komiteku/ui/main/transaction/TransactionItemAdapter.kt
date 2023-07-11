package app.komiteku.ui.main.transaction

import android.content.res.ColorStateList.valueOf
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import app.komiteku.R
import app.komiteku.data.model.ListTransactionResult
import app.komiteku.databinding.ItemTransactionBinding
import app.komiteku.ui.main.home.HomeFragmentDirections
import app.komiteku.utils.Constanta
import app.komiteku.utils.rupiahFormat
import com.google.android.material.color.MaterialColors

class TransactionItemAdapter :
    ListAdapter<ListTransactionResult, TransactionItemAdapter.ViewHolder>(Constanta.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder = ViewHolder(
        ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListTransactionResult) = with(binding) {
            when (item.status) {
                "PENDING" -> statusTransaksi.apply {
                    val color =
                        MaterialColors.getColor(context, R.attr.colorOrangeContainer, Color.BLACK)
                    chipBackgroundColor = valueOf(color)
                    setTextColor(getColor(context, R.color.orange))
                    text = "PENDING"
                }

                "APPROVED" -> statusTransaksi.apply {
                    val color =
                        MaterialColors.getColor(context, R.attr.colorLeafContainer, Color.BLACK)
                    chipBackgroundColor = valueOf(color)
                    setTextColor(getColor(context, R.color.leaf))
                    text = "APPROVED"
                }

                "REJECTED" -> statusTransaksi.apply {
                    val color =
                        MaterialColors.getColor(context, R.attr.colorRoseContainer, Color.BLACK)
                    chipBackgroundColor = valueOf(color)
                    setTextColor(getColor(context, R.color.rose))
                    text = "REJECTED"
                }
            }

            nominalTitle.text = rupiahFormat(item.amount)
            nominaLimit.text = when (item.amount) {
                70000 -> "Pembayaran untuk 1 Bulan"
                140000 -> "Pembayaran untuk 2 BUlan"
                210000 -> "Pembayaran untuk 3 Bulan"
                280000 -> "Pembayaran untuk 4 Bulan"
                420000 -> "Pembayaran untuk 1 Semester"
                840000 -> "Pembayaran untuk 2 Semester"
                else -> null
            }

            itemView.setOnClickListener {
                if (item.id.isNotEmpty()) {
                    val directionFromTransaction =
                        TransactionFragmentDirections.actionTransactionToDetail(item)
                    val directionFromHome =
                        HomeFragmentDirections.actionHomeToTransactionDetail(item)
                    val destination =
                        if (it.findNavController().currentDestination?.id == R.id.homeFragment) directionFromHome
                        else directionFromTransaction
                    it.findNavController().navigate(destination)
                } else return@setOnClickListener
            }
        }
    }
}
