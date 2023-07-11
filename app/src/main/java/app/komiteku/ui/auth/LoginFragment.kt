package app.komiteku.ui.auth

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import app.komiteku.R
import app.komiteku.base.BaseFragment
import app.komiteku.data.local.LoginSession
import app.komiteku.data.model.LoginRequest
import app.komiteku.data.model.LoginResult
import app.komiteku.data.model.RequestState
import app.komiteku.databinding.MainLoginBinding
import app.komiteku.ui.AuthViewModel
import app.komiteku.utils.FormValidator.isEmailValid
import app.komiteku.utils.FormValidator.isPasswordValid
import app.komiteku.utils.afterInputStringChanged
import app.komiteku.utils.currentDate
import app.komiteku.utils.hideSoftKeyboard
import app.komiteku.utils.isNetworkAvailable
import app.komiteku.utils.loadingDialog
import app.komiteku.utils.popupDialog
import app.komiteku.utils.runWhenNeed
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<MainLoginBinding>(MainLoginBinding::inflate) {
    private val viewModel: AuthViewModel by viewModel()
    private lateinit var loading: AlertDialog

    override fun renderView(context: Context?, savedInstanceState: Bundle?) {
        loading = loadingDialog()
        loading.dismiss()
        validateForm()
    }

    private fun validateForm() = with(binding) {
        emailInput.apply {
            requestFocus()
            afterInputStringChanged { email ->
                if (email != null) {
                    loginButton.isEnabled = !isEmailValid(email)
                    emailInputLayout.error = when {
                        email.isEmpty() -> getString(R.string.fill_data)
                        isEmailValid(email.toString()) -> getString(R.string.email_invalid)
                        else -> null
                    }
                }
            }
        }
        passwordInput.apply {
            afterInputStringChanged { password ->
                passwordInputLayout.error = when {
                    password.isNullOrEmpty() -> getString(R.string.fill_data)
                    isPasswordValid(password.toString()) -> getString(R.string.fill_password)
                    else -> null
                }
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) doLogin()
                false
            }
        }
        loginButton.setOnClickListener { doLogin() }
    }

    private fun doLogin() {
        hideSoftKeyboard(requireContext(), binding.root)
        if (isNetworkAvailable()) with(binding) {
            val email = emailInput.text.toString()
            val sandi = passwordInput.text.toString()

            if (isEmailValid(email)) emailInputLayout.error = getString(R.string.email_invalid)
            else if (sandi.isBlank()) passwordInputLayout.error = getString(R.string.fill_data)
            else if (isPasswordValid(sandi)) passwordInputLayout.error =
                getString(R.string.fill_password)
            else runWhenNeed {
                viewModel.doLogin(LoginRequest(email, sandi)).collect { state ->
                    when (state) {
                        is RequestState.Loading -> loading.show()
                        is RequestState.Error -> whenStateError(state.error)
                        is RequestState.Success -> whenStateSuccess(email, state.data)
                    }
                }
            }
        } else popupDialog(
            getString(R.string.login_failed),
            getString(R.string.check_network_connection)
        ).setPositiveButton(getText(R.string.button_ok)) { dialog, _ -> dialog.dismiss() }.show()
    }

    private fun whenStateError(message: String) {
        if (message.isNotEmpty()) loading.dismiss()
        popupDialog(getString(R.string.login_failed), message)
            .setPositiveButton(getString(R.string.button_try_again)) { _, _ -> doLogin() }
            .setNeutralButton(getString(R.string.button_ok)) { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun whenStateSuccess(email: String, result: LoginResult) {
        loading.dismiss()
        val session = LoginSession(
            loginAt = currentDate,
            token = result.token,
            id = result.id,
            nis = result.nis,
            name = result.name,
            kelas = result.kelas,
            jurusan = result.jurusan,
            email = email,
            hp = result.hp
        )
        viewModel.saveSession(session)
        findNavController().navigate(R.id.action_login_to_home)
    }
}
