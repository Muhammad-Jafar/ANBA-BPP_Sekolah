package app.komiteku.ui.main.payment

import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.komiteku.R
import app.komiteku.base.BaseFragment
import app.komiteku.data.model.PaymentRequest
import app.komiteku.data.model.PaymentResult
import app.komiteku.data.model.RequestState
import app.komiteku.databinding.MainPaymentConfirmBinding
import app.komiteku.ui.PaymentViewModel
import app.komiteku.utils.afterInputStringChanged
import app.komiteku.utils.hideSoftKeyboard
import app.komiteku.utils.loadingDialog
import app.komiteku.utils.popupDialog
import app.komiteku.utils.runWhenStarted
import app.komiteku.utils.rupiahFormat
import app.komiteku.utils.vibrate
import kotlinx.coroutines.flow.first
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmPaymentFragment :
    BaseFragment<MainPaymentConfirmBinding>(MainPaymentConfirmBinding::inflate) {
    private val viewModel: PaymentViewModel by viewModel()
    private val args: ConfirmPaymentFragmentArgs by navArgs()
    private lateinit var loading: AlertDialog

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        with(binding) {
            confirmToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            loading = loadingDialog()
            loading.dismiss()

            runWhenStarted {
                with(viewModel) {
                    pembayarContent.text = getName.first()
                    emailContent.text = getEmail.first()

                    tagihanContent.text = rupiahFormat(args.PaymentForm.amount)
                    totalContent.text = rupiahFormat(args.PaymentForm.amount)
                    waktuContent.text = args.PaymentForm.expire
                }
            }

            doPay.setOnClickListener {
                hideSoftKeyboard(requireContext(), it)
                doValidated()
            }
        }
    }

    private fun doValidated() = with(binding) {
        inputNote.afterInputStringChanged { note ->
            if (note.isNullOrBlank()) inputNoteLayout.error = getString(R.string.fill_note)
            else inputNoteLayout.error = null
        }
        if (inputNote.text.isNullOrEmpty()) with(inputNoteLayout) {
            startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.shake))
            error = getString(R.string.fill_note)
            vibrate()
        } else doPayment()
    }

    private fun doPayment() = runWhenStarted {
        with(viewModel) {
            val payment = PaymentRequest(
                getBillId.first()!!,
                getId.first()!!,
                getName.first()!!,
                getEmail.first()!!,
                args.PaymentForm.amount,
                binding.inputNote.text.toString()
            )
            doPayment(payment).collect { state ->
                when (state) {
                    is RequestState.Loading -> loading.show()
                    is RequestState.Error -> whenStateError(state.error)
                    is RequestState.Success -> whenStateSuccess(state.data)
                }
            }
        }
    }

    private fun whenStateError(message: String) {
        if (message.isNotEmpty()) loading.dismiss()
        popupDialog(getString(R.string.transaction_failed), message)
            .setPositiveButton(getString(R.string.button_try_again)) { _, _ -> doPayment() }
            .setNeutralButton(getString(R.string.button_ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun whenStateSuccess(result: PaymentResult) {
        loading.dismiss()
        viewModel.saveVaNumber(result)
        val destination = ConfirmPaymentFragmentDirections.actionConfirmToSend(result)
        findNavController().navigate(destination)
    }
}
