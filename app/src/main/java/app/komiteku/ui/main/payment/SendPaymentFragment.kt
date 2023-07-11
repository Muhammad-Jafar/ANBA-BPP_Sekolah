package app.komiteku.ui.main.payment

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import app.komiteku.R
import app.komiteku.base.BaseFragment
import app.komiteku.databinding.MainPaymentSendBinding
import app.komiteku.utils.rupiahFormat

class SendPaymentFragment : BaseFragment<MainPaymentSendBinding>(MainPaymentSendBinding::inflate) {
    private val args: SendPaymentFragmentArgs by navArgs()

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        with(binding) {
            paymentToolbar.setNavigationOnClickListener { findNavController().navigate(R.id.action_send_to_home) }
            backToHome.setOnClickListener { findNavController().navigate(R.id.action_send_to_home) }

            vaContent.text = args.payment.va_number
            amountContent.text = rupiahFormat(args.payment.amount)
        }
    }

}
