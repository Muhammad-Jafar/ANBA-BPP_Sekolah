package app.komiteku.ui.main.bill

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import app.komiteku.base.BaseFragment
import app.komiteku.databinding.MainBillBinding
import app.komiteku.ui.MainViewModel
import app.komiteku.utils.runWhenStarted
import app.komiteku.utils.rupiahFormat
import kotlinx.coroutines.flow.first
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class BillFragment : BaseFragment<MainBillBinding>(MainBillBinding::inflate) {
    private val viewModel: MainViewModel by viewModel()

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        with(binding) {
            billToolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            runWhenStarted {
                with(viewModel) {
                    val id = getBillId.first()
                    val billings = getBillBillings.first()
                    val recentBill = getBillRecentBill.first()
                    if (id == 0) {
                        progressBillIndicator.apply {
                            max = 100
                            setProgressCompat(0, true)
                        }
                        titleProgressBillIndicator.text = "0"
                        hasDoneContent.text = rupiahFormat(0)
                        notYetContent.text = rupiahFormat(0)
                    } else {
                        progressBillIndicator.apply {
                            max = billings!!
                            setProgressCompat(recentBill!!, true)
                        }
                        val progress =
                            ((recentBill!!.toFloat() / billings!!.toFloat()) * 100F).roundToInt()
                        titleProgressBillIndicator.text = progress.toString()
                        hasDoneContent.text = rupiahFormat(recentBill)
                        notYetContent.text = rupiahFormat(billings - recentBill)
                    }
                }
            }
        }
    }

}
