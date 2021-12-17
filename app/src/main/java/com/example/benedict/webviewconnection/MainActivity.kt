package com.example.benedict.webviewconnection

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.benedict.webviewconnection.databinding.MainBinder

public class MainActivity : AppCompatActivity(), OnClickListener {

    companion object {
        public val TAG = MainActivity::class.java.getSimpleName()
    }

    private val binding : MainBinder by lazy(LazyThreadSafetyMode.NONE, initializer = { DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main) } )
    private val viewModel : MainViewModel by lazy(LazyThreadSafetyMode.NONE, initializer = { ViewModelProvider(this).get(MainViewModel::class.java) } )

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        binding.setViewModel(viewModel)
        binding.setLifecycleOwner(this@MainActivity)

        setWebView()
        observeData()
    }

    private fun setWebView() {
        viewModel.setWebContentsDebuggingEnabled(applicationInfo)
        val webSettings: WebSettings = binding.webView.getSettings()
        binding.webView.getSettings().setJavaScriptEnabled(true)
        binding.webView.addJavascriptInterface(viewModel.getWebAppInterface(), WebAppInterface.TAG)
        binding.webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                view.loadUrl(viewModel.getJavascriptAndroidVersion("called by Android"))
                view.loadUrl(viewModel.getJavascriptAlert("Hello Web View"))
            }
        })
        binding.webView.setWebChromeClient(object : WebChromeClient() {
            override fun onJsAlert(view : WebView, url : String, message : String, result : JsResult) : Boolean {
                Log.d("Javascript Alert", "onJsAlert(" + view + ", " + url + ", " + message + ", " + result +")")
                result.confirm()
                return if (viewModel.isUndefined(message) == true) true
                else return super.onJsAlert(view, url, message, result)
            }
        })
        binding.webView.loadUrl(viewModel.getHTMLPath())
    }

    private fun observeData() {
        viewModel.observeLiveText().observe(this@MainActivity, object : Observer<String> {
            override fun onChanged(value : String) {
                binding.textFromWeb.setText(value)
            }
        })
    }

    override fun onClick(view : View) {
        if(view.getId() == binding.buttonShowAlert.getId() && viewModel.isGreaterThanEqualKitKat()) {
            binding.webView.evaluateJavascript(viewModel.getJavascriptAlert(binding.editTextToWeb.getText().toString()), null);
        } else if(view.getId() == binding.buttonShowAlert.getId() && viewModel.isNotGreaterThanEqualKitKat()) {
            binding.webView.loadUrl(viewModel.getJavascriptAlert(binding.editTextToWeb.getText().toString()));
        } else if (view.getId() == binding.buttonSendToWeb.getId() && viewModel.isGreaterThanEqualKitKat()) {
            binding.webView.evaluateJavascript(viewModel.getJavascriptParagraph(binding.editTextToWeb.getText().toString()), null);
        } else if (view.getId() == binding.buttonSendToWeb.getId() && viewModel.isNotGreaterThanEqualKitKat()) {
            binding.webView.loadUrl(viewModel.getJavascriptParagraph(binding.editTextToWeb.getText().toString()));
        }
    }

    override fun onResume() {
        super.onResume()
        binding.buttonShowAlert.setOnClickListener(this@MainActivity)
        binding.buttonSendToWeb.setOnClickListener(this@MainActivity)
    }

    override fun onPause() {
        super.onPause()
        binding.buttonShowAlert.setOnClickListener(null)
        binding.buttonSendToWeb.setOnClickListener(null)
    }

    override fun onDestroy() {
        binding.webView.removeJavascriptInterface(WebAppInterface.TAG)
        super.onDestroy()
    }
}