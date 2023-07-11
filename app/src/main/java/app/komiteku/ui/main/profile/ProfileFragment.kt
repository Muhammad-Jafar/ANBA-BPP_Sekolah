package app.komiteku.ui.main.profile

import android.content.Context
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import app.komiteku.base.BaseFragment
import app.komiteku.databinding.MainProfileBinding
import app.komiteku.ui.MainViewModel
import app.komiteku.utils.format
import app.komiteku.utils.runWhenStarted
import kotlinx.coroutines.flow.first
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<MainProfileBinding>(MainProfileBinding::inflate) {
    private val viewModel: MainViewModel by viewModel()

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        with(binding) {
            profileToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
            runWhenStarted {
                with(viewModel) {
                    fullNameContent.text = getName.first()
                    nisContent.text = getNis.first().toString()
                    kelasContent.text = getKelas.first()
                    jurusanContent.text = getJurusan.first()
                    emailContent.text = getEmail.first()
                    phoneContent.text = getPhone.first()
                    loginAtContent.text = getLoginAt.first()?.format("d MMM yy ◦ h.mm")
                }
            }
        }
    }

}
