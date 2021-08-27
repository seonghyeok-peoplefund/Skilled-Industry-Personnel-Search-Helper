package com.ray.personnel.ui.login

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.ray.personnel.Constants.TOKEN

class LoginWebClient : WebViewClient() {
    var onTokenListener: ((String) -> Unit)? = null
    override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
        if (view.url != null && onTokenListener != null) {
            getCookie(view.url!!, TOKEN)?.also { token ->
                onTokenListener!!(token)
            }
        }
        return true
    }

    private fun getCookie(siteName: String, CookieName: String): String? {
        val cookies = CookieManager.getInstance()
            .getCookie(siteName)
            ?: return null
        cookies.split(";")
            .forEach { cookie ->
                if (cookie.contains(CookieName)) return cookie.split("=")[1]
            }
        return null
    }
}