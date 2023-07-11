package app.komiteku.utils

import android.content.Context
import android.content.res.Resources
import android.icu.text.NumberFormat
import android.icu.text.SimpleDateFormat
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import app.komiteku.R
import app.komiteku.databinding.DialogLoadingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale

/* GET CURRENT DATE AND TIME */
val currentDate = Calendar.getInstance().timeInMillis

/* FORMAT RUPIAH INDONESIA */
fun rupiahFormat(number: Int?): String =
    NumberFormat.getCurrencyInstance(Locale("in", "ID")).format(number)

/* CONVERT CURRENT DATE TO LOCALE FORMAT */
fun Long.format(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault(Locale.Category.FORMAT)).format(Date(this))

/* GET SIZE IN PIXEL */
fun Int.dotPixel() = (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()

/* CUSTOM TOAST */
fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun Fragment.showSnackbar(message: String) =
    Snackbar.make(this.requireView(), message, Snackbar.ANIMATION_MODE_SLIDE).show()


/* CUSTOM POPUP DIALOG */
fun Fragment.popupDialog(title: String, message: String): MaterialAlertDialogBuilder =
    MaterialAlertDialogBuilder(requireContext()).setTitle(title).setMessage(message)

/* CUSTOM ALERT DIALOG */
fun Fragment.loadingDialog(): AlertDialog {
    val binding = DialogLoadingBinding.inflate(LayoutInflater.from(requireContext()), null, false)
    return MaterialAlertDialogBuilder(requireContext(), R.style.CustomDialogLoading)
        .setView(binding.root).setCancelable(false).show()
}

/*HIDE SOFT KEYBOARD*/
fun hideSoftKeyboard(context: Context, view: View) {
    (context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow(view.windowToken, 0)
}

/* EDIT TEXT VALIDATION TO STRING */
fun TextInputEditText.afterInputStringChanged(afterTextChanged: (String?) -> Unit) =
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })

/* COROUTINE RUN IN BACKGROUND VIEW MODEL */
fun ViewModel.runInBackground(action: suspend CoroutineScope.() -> Unit) =
    viewModelScope.launch(Dispatchers.IO) { action() }

/* COROUTINE RUN IN BACKGROUND FRAGMENT */
fun Fragment.runWhenCreated(action: suspend CoroutineScope.() -> Unit) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.CREATED) { action() }
}

fun Fragment.runWhenStarted(action: suspend CoroutineScope.() -> Unit) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.STARTED) { action() }
}

fun Fragment.runWhenNeed(action: suspend CoroutineScope.() -> Unit) = lifecycleScope.launch {
    repeatOnLifecycle(Lifecycle.State.RESUMED) { action() }
}

/* CHECK NETWORK STATE */
fun Fragment.isNetworkAvailable(): Boolean {
    if (context == null) return false
    val connectivityManager =
        requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }
        }
    }
    return false
}

/* PLAY SHAKE ANIMATION */
fun View.vibrate() {
    val vibe = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        vibe!!.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
    else vibe!!.vibrate(200)
}
