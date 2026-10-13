package com.example

import android.app.PictureInPictureParams
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.MainGifScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.GifViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GifViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme(darkTheme = true) {
                MainGifScreen(
                    viewModel = viewModel,
                    onEnterSystemPip = { enterPipWindowMode() },
                    onLaunchYouTubeApp = { launchYouTubeApp() },
                    onCopyToClipboard = { text, title -> copyTextToClipboard(text, title) }
                )
            }
        }
    }

    private fun enterPipWindowMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                val aspectRatio = Rational(16, 9)
                val params = PictureInPictureParams.Builder()
                    .setAspectRatio(aspectRatio)
                    .build()
                enterPictureInPictureMode(params)
                viewModel.setSystemPiPActive(true)
            } catch (e: Exception) {
                Toast.makeText(this, "Pencere modu açılamadı: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Bu cihaz sistem pencere modunu desteklemiyor, uygulama içi pencere kullanılıyor.", Toast.LENGTH_SHORT).show()
            viewModel.toggleFloatingOverlay()
        }
    }

    @Suppress("DEPRECATION")
    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        viewModel.setSystemPiPActive(isInPictureInPictureMode)
    }

    private fun copyTextToClipboard(text: String, title: String) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("YouTube GIF", text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, "Kopyalandı: $title (YouTube'a yapıştırabilirsiniz)", Toast.LENGTH_SHORT).show()
    }

    private fun launchYouTubeApp() {
        try {
            val intent = packageManager.getLaunchIntentForPackage("com.google.android.youtube")
            if (intent != null) {
                startActivity(intent)
            } else {
                val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
                startActivity(webIntent)
            }
        } catch (e: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com"))
            startActivity(webIntent)
        }
    }
}
