package app.itmaster.mobile.coffeemasters.pages

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.viewinterop.AndroidView
import app.itmaster.mobile.coffeemasters.JSInterface
import app.itmaster.mobile.coffeemasters.composables.CustomSnackbarHost
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InfoPage() {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { CustomSnackbarHost(snackbarHostState) }
    ) {
        AndroidView(
            factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    settings.javaScriptEnabled = true
                    addJavascriptInterface(JSInterface { text ->
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = text,
                                actionLabel = "CLOSE"
                            )
                        }
                    }, "Android")
                    webViewClient = WebViewClient()
                    webChromeClient = object : WebChromeClient() {
                        override fun onJsAlert(
                            view: WebView,
                            url: String,
                            message: String,
                            result: JsResult
                        ): Boolean {
                            println("Alerta de JS: $message")
                            return true
                        }
                    }
                    loadUrl("file:///android_asset/index.html")
                }
            }
        )
    }
}

