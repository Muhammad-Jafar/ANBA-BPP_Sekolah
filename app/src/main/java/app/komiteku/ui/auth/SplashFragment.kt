package app.komiteku.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import app.komiteku.R
import app.komiteku.base.BaseFragment
import app.komiteku.data.model.BillState
import app.komiteku.databinding.DialogNetworkStateBinding
import app.komiteku.databinding.MainSplashBinding
import app.komiteku.ui.MainViewModel
import app.komiteku.utils.dotPixel
import app.komiteku.utils.isNetworkAvailable
import app.komiteku.utils.runWhenStarted
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.flow.first
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<MainSplashBinding>(MainSplashBinding::inflate) {
    private val viewModel: MainViewModel by viewModel()

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        if (isNetworkAvailable()) with(viewModel) {
            runWhenStarted {
                if (getToken.first().isNullOrEmpty())
                    findNavController().navigate(R.id.action_splash_to_login)
                else billings.collect { result ->
                    when (result) {
                        is BillState.Loading -> return@collect
                        is BillState.Error -> findNavController().navigate(R.id.action_splash_to_login)
                        is BillState.Success -> findNavController().navigate(R.id.action_splash_to_home)
                    }
                }
            }
        }
        else showNetworkNotif()
    }

    private fun showNetworkNotif() {
        with(binding) {
            waitingTitle.visibility = View.GONE
            waitingProgressBar.visibility = View.GONE
        }

        val dialog = DialogNetworkStateBinding.inflate(layoutInflater)
        val form = BottomSheetDialog(requireContext()).apply {
            setContentView(dialog.root)
            setCancelable(false)
            behavior.maxHeight = 480.dotPixel()
        }
        with(dialog) {
            root.doOnPreDraw { form.behavior.peekHeight = it.height }
            confirmExit.setOnClickListener { requireActivity().finishAffinity() }
        }
        form.show()
    }
}
