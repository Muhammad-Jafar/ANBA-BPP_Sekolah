package app.komiteku.base

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import app.komiteku.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val gravity: Int = Gravity.BOTTOM
    private val isSystemBarUsed: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setOnExitAnimationListener { viewProvider ->
            viewProvider.iconView
                .animate()
                .setDuration(300)
                .alpha(0f)
                .withEndAction {
                    viewProvider.remove()
                    setContentView(binding.root)
                }.start()
        }

        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initWindowInsets()
    }

    private fun initWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val imeVisible = windowInsets.isVisible(WindowInsetsCompat.Type.ime())
            val imeHeight = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom

            val insetsSystemBar = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
            val insetsSystemGesture =
                windowInsets.getInsets(WindowInsetsCompat.Type.systemGestures())

            if (isSystemBarUsed) when (gravity) {
                Gravity.TOP -> view.updatePadding(top = insetsSystemBar.top)
                Gravity.BOTTOM -> {
                    if (imeVisible) view.updatePadding(bottom = insetsSystemBar.bottom + imeHeight)
                    else view.updatePadding(bottom = insetsSystemBar.bottom)
                }
            }
            else when (gravity) {
                Gravity.TOP -> view.updatePadding(top = insetsSystemGesture.top)
                Gravity.BOTTOM -> {
                    if (imeVisible) view.updatePadding(bottom = insetsSystemBar.bottom + imeHeight)
                    else view.updatePadding(bottom = insetsSystemBar.bottom)
                }
            }
            windowInsets
        }
    }
}
