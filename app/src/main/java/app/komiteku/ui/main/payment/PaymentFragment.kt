package app.komiteku.ui.main.payment

import android.content.Context
import android.os.Bundle
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import app.komiteku.R
import app.komiteku.base.BaseFragment
import app.komiteku.data.model.PaymentRequire
import app.komiteku.databinding.DialogConfirmPaymentBinding
import app.komiteku.databinding.MainPaymentBinding
import app.komiteku.ui.PaymentViewModel
import app.komiteku.utils.currentDate
import app.komiteku.utils.dotPixel
import app.komiteku.utils.format
import app.komiteku.utils.runWhenCreated
import app.komiteku.utils.runWhenNeed
import app.komiteku.utils.rupiahFormat
import app.komiteku.utils.showSnackbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.first
import org.koin.androidx.viewmodel.ext.android.viewModel

class PaymentFragment : BaseFragment<MainPaymentBinding>(MainPaymentBinding::inflate) {
    private val viewModel: PaymentViewModel by viewModel()
    private var scrollRange = -1

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        with(binding) {
            appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
                if (scrollRange == -1) scrollRange = appBarLayout?.totalScrollRange!!
                if (scrollRange + verticalOffset != 0) collapseBar.title = ""
                else collapseBar.title = paymentToolbar.title
            }
            paymentToolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            runWhenCreated {
                with(viewModel) {
                    val recentNominal = getBillBillings.first()?.minus(getBillRecentBill.first()!!)
                    if (recentNominal != null) {
                        aMonthPay.setOnClickListener {
                            if (recentNominal < 70000) showSnackbar(getString(R.string.confirm_nominal_payment))
                            else showConfirmDialog(70000, "1 Bulan")
                        }
                        twoMonthPay.setOnClickListener {
                            if (recentNominal < 140000) showSnackbar(getString(R.string.confirm_nominal_payment))
                            else showConfirmDialog(140000, "2 Bulan")
                        }
                        quarterPay.setOnClickListener {
                            if (recentNominal < 210000) showSnackbar(getString(R.string.confirm_nominal_payment))
                            else showConfirmDialog(210000, "3 Bulan")
                        }
                        fourMonthPay.setOnClickListener {
                            if (recentNominal < 280000) showSnackbar(getString(R.string.confirm_nominal_payment))
                            else showConfirmDialog(280000, "4 Bulan")
                        }
                        semiAnnuallyPay.setOnClickListener {
                            if (recentNominal < 420000) showSnackbar(getString(R.string.confirm_nominal_payment))
                            else showConfirmDialog(420000, "1 Semester")
                        }
                        annuallyPay.setOnClickListener {
                            if (recentNominal < 840000) showSnackbar(getString(R.string.confirm_nominal_payment))
                            else showConfirmDialog(840000, "2 Semester")
                        }
                    }
                }
            }
        }
    }

    private fun showConfirmDialog(amount: Int, expire: String) {
        val dialog = DialogConfirmPaymentBinding.inflate(layoutInflater)
        val form = BottomSheetDialog(requireContext()).apply {
            setContentView(dialog.root)
            setCancelable(false)
            behavior.maxHeight = 480.dotPixel()
        }
        with(dialog) {
            root.doOnPreDraw { form.behavior.peekHeight = it.height }
            runWhenNeed {
                nameContent.text = viewModel.getName.first()
            }
            amountContent.text = rupiahFormat(amount)
            expireContent.text = expire
            dateContent.text = currentDate.format("d MMM yyyy hh:mm")
            totalContent.text = rupiahFormat(amount)
            cancelButton.setOnClickListener { form.dismiss() }
            confirmButton.setOnClickListener {
                val direction =
                    PaymentFragmentDirections.actionPaymentToConfirm(PaymentRequire(amount, expire))
                findNavController().navigate(direction)
                form.dismiss()
            }
        }
        form.show()
    }

}
