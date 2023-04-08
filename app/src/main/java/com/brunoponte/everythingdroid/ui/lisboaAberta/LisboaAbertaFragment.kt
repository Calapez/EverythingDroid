package com.brunoponte.everythingdroid.ui.lisboaAberta

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.brunoponte.everythingdroid.databinding.FragmentLisboaAbertaBinding

class LisboaAbertaFragment : Fragment() {

    private lateinit var binding: FragmentLisboaAbertaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLisboaAbertaBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                // Prevents opening Chromium from the Android app
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    return false
                }
            }

            // Use the existing survey URL or replace it with your web app. See /survey-web-app.
            loadUrl("https://lisboaaberta.cm-lisboa.pt/index.php/pt/")
            settings.javaScriptEnabled = true
            addJavascriptInterface(WebAppInterface(requireContext()), "Android")
        }

        // Create the callback, note it's initially set to false since the webview will have a single
        // page in the page stack, when the fragment opens
        val onBackPressedCallback = object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when {
                    binding.webView.canGoBack() -> binding.webView.goBack()
                }
            }
        }

        // Register the callback with the onBackPressedDispatcher
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)

        disableOnBackPressedCallback(binding.webView, onBackPressedCallback)
    }

    private fun disableOnBackPressedCallback(webView: WebView, onBackPressedCallback: OnBackPressedCallback) {
        webView.webViewClient = object: WebViewClient() {
            // Use webView.canGoBack() to determine whether or not the OnBackPressedCallback is enabled.
            // if the callback is enabled, the app takes control and determines what to do. If the
            // callbacks is disabled; the back nav gesture will go back to the topmost activity/fragment
            // in the back stack.
            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                onBackPressedCallback.isEnabled = webView.canGoBack()
            }
        }
    }
}