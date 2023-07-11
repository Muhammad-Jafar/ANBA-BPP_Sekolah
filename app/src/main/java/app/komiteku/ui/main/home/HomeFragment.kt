package app.komiteku.ui.main.home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import app.komiteku.R
import app.komiteku.base.BaseFragment
import app.komiteku.data.model.BillState
import app.komiteku.data.model.ListTransactionState
import app.komiteku.databinding.DialogConfirmSessionExpireBinding
import app.komiteku.databinding.DialogLogoutBinding
import app.komiteku.databinding.MainHomeBinding
import app.komiteku.ui.MainViewModel
import app.komiteku.ui.main.transaction.TransactionItemAdapter
import app.komiteku.utils.dotPixel
import app.komiteku.utils.isNetworkAvailable
import app.komiteku.utils.popupDialog
import app.komiteku.utils.runWhenCreated
import app.komiteku.utils.rupiahFormat
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<MainHomeBinding>(MainHomeBinding::inflate) {
    private val viewModel: MainViewModel by viewModel()
    private val transactionAdapter =
        TransactionItemAdapter().apply { stateRestorationPolicy = PREVENT_WHEN_EMPTY }

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        getBillAndTransaction()
        with(binding) {
            mainToolbar.apply {
                inflateMenu(R.menu.main_menu)
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.logoutButton -> showLogoutDialog()
                        R.id.profilButton -> findNavController().navigate(R.id.action_home_to_profile)
                    }
                    true
                }
            }
            content.content.seeAllTransaction.setOnClickListener { findNavController().navigate(R.id.action_home_to_transaction) }
            billButton.setOnClickListener { findNavController().navigate(R.id.action_home_to_bill) }
            payBillButton.setOnClickListener { findNavController().navigate(R.id.action_home_to_payment) }
        }
    }

    private fun getBillAndTransaction() = with(binding) {
        runWhenCreated {
            viewModel.apply {
                nameUser.text = getName.first()
                nisContent.text = getNis.first()

                if (isNetworkAvailable()) {
                    launch {
                        billings.collect { result ->
                            when (result) {
                                is BillState.Loading -> return@collect
                                is BillState.Error -> showStateError()
                                is BillState.Success -> {
                                    saveBillings(result.data)
                                    val billings =
                                        result.data.billings.minus(result.data.recentBill)
                                    shimmerBill.visibility = View.GONE
                                    billNominal.visibility = View.VISIBLE
                                    billNominal.text = rupiahFormat(billings)
                                }
                            }
                        }
                    }
                    launch {
                        getListTransaction(3).collect { state ->
                            when (state) {
                                is ListTransactionState.Loading -> return@collect
                                is ListTransactionState.Error -> content.apply {
                                    error.visibility = View.VISIBLE
                                    loading.root.visibility = View.GONE
                                }

                                is ListTransactionState.Success -> content.apply {
                                    loading.root.visibility = View.GONE
                                    content.root.visibility = View.VISIBLE
                                    transactionAdapter.submitList(state.data)
                                }
                            }
                        }
                    }
                    content.content.listAtHome.adapter = transactionAdapter
                } else popupDialog(
                    getString(R.string.conn_failed),
                    getString(R.string.check_network_connection)
                ).setPositiveButton(getString(R.string.button_ok)) { dialog, _ -> dialog.dismiss() }
            }
        }
    }

    private fun showStateError() {
        val dialog = DialogConfirmSessionExpireBinding.inflate(layoutInflater)
        val form = BottomSheetDialog(requireContext()).apply {
            setContentView(dialog.root)
            behavior.maxHeight = 480.dotPixel()
            setCancelable(false)
        }
        with(dialog) {
            root.doOnPreDraw { form.behavior.peekHeight = it.height }
            confirmButton.setOnClickListener {
                form.dismiss()
                findNavController().navigate(R.id.action_home_to_login)
            }
        }
        form.show()
    }

    private fun showLogoutDialog() {
        val dialog = DialogLogoutBinding.inflate(layoutInflater)
        val form = BottomSheetDialog(requireContext()).apply {
            setContentView(dialog.root)
            behavior.maxHeight = 480.dotPixel()
        }
        with(dialog) {
            root.doOnPreDraw { form.behavior.peekHeight = it.height }
            cancelButton.setOnClickListener { form.dismiss() }
            confirmButton.setOnClickListener {
                form.dismiss()
                findNavController().navigate(R.id.action_home_to_login)
            }
        }
        form.show()
    }
}
