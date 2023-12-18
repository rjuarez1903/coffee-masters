package app.itmaster.mobile.coffeemasters

import android.webkit.JavascriptInterface

class JSInterface(private val showSnackbarCallback: (String) -> Unit) {
    @JavascriptInterface
    fun showSnackbar(text: String) {
        showSnackbarCallback(text)
    }
}
