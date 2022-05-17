package com.brunoponte.everythinglisboa.ui.lisboaAberta

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.brunoponte.everythinglisboa.databinding.FragmentCameraBinding
import com.brunoponte.everythinglisboa.databinding.FragmentLisboaAbertaBinding
import com.brunoponte.everythinglisboa.ui.camera.CameraViewModel

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