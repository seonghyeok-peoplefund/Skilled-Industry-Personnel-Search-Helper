package com.ray.personnel.ui.mainpage.login

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.BindingAdapter
import com.ray.personnel.Constants

@BindingAdapter("webViewClient")
fun webViewClient(webView: WebView, webClient: WebViewClient) {
    webView.webViewClient = webClient
    webView.settings.javaScriptEnabled = true
    webView.loadUrl(Constants.WANTED_LOGIN)
}