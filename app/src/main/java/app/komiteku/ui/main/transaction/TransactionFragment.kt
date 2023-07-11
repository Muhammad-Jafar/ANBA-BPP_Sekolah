package app.komiteku.ui.main.transaction

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import app.komiteku.base.BaseFragment
import app.komiteku.data.model.ListTransactionState
import app.komiteku.databinding.MainTransactionBinding
import app.komiteku.ui.MainViewModel
import app.komiteku.utils.loadingDialog
import app.komiteku.utils.runWhenCreated
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransactionFragment : BaseFragment<MainTransactionBinding>(MainTransactionBinding::inflate),
    SwipeRefreshLayout.OnRefreshListener {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var loading: AlertDialog
    private val transactionAdapter =
        TransactionItemAdapter().apply { stateRestorationPolicy = PREVENT_WHEN_EMPTY }

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        with(binding) {
            loading = loadingDialog()
            loading.dismiss()
            getListTransaction()
            transactionToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            swipeRefresh.setOnRefreshListener { onRefresh() }
        }
    }

    private fun getListTransaction() = with(binding) {
        runWhenCreated {
            launch {
                viewModel.getListTransaction().collect { state ->
                    when (state) {
                        is ListTransactionState.Loading -> loading.show()
                        is ListTransactionState.Error -> {
                            loading.dismiss()
                            errorState.visibility = View.VISIBLE
                        }

                        is ListTransactionState.Success -> {
                            loading.dismiss()
                            errorState.visibility = View.GONE
                            if (state.data.isEmpty()) emptyState.visibility = View.VISIBLE
                            else {
                                contentState.visibility = View.VISIBLE
                                transactionAdapter.submitList(state.data)
                            }
                        }
                    }
                }
            }
            contentState.adapter = transactionAdapter
        }
    }

    override fun onRefresh() {
        binding.swipeRefresh.isRefreshing = false
        getListTransaction()
    }
}
