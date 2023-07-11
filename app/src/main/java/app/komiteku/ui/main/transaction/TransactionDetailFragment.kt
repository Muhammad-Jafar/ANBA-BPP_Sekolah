package app.komiteku.ui.main.transaction

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.komiteku.R
import app.komiteku.base.BaseFragment
import app.komiteku.databinding.MainTransactionDetailBinding
import app.komiteku.ui.MainViewModel
import app.komiteku.utils.runWhenStarted
import app.komiteku.utils.rupiahFormat
import kotlinx.coroutines.flow.first
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionDetailFragment :
    BaseFragment<MainTransactionDetailBinding>(MainTransactionDetailBinding::inflate) {
    private val viewModel: MainViewModel by viewModel()
    private val args: TransactionDetailFragmentArgs by navArgs()

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        with(binding) {
            transactionDetailToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            if (args.item.status == "PENDING") pending.apply {
                root.visibility = View.VISIBLE
                runWhenStarted {
                    with(viewModel) {
                        vaNumberContent.text = vaNumber.first()
                        amountContent.text = rupiahFormat(amount.first())
                        expireContent.text = expire.first()
                    }
                }
            } else notPending.apply {
                root.visibility = View.VISIBLE
                with(args.item) {
                    transCodeContent.text = transactionCode
                    amountContent.text = rupiahFormat(amount)
                    dateContent.text = tanggal
                    statusContent.apply {
                        isAllCaps = true
                        when (status) {
                            "APPROVED" -> {
                                setTextColor(resources.getColor(R.color.leaf))
                                text = resources.getText(R.string.status_approved)
                            }

                            "REJECTED" -> {
                                setTextColor(resources.getColor(R.color.rose))
                                text = resources.getText(R.string.status_rejected)
                            }
                        }
                    }
                    noteContent.text = note
                }
            }
        }
    }
}
