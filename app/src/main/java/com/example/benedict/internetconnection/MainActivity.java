package com.example.benedict.internetconnection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    public static String TAG = MainActivity.class.getSimpleName();
    private Button btnShowAlert, btnSendToWeb;
    private EditText edtTextToWeb;
    private TextView txtFromWeb;
    private MainViewModel viewModel;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnShowAlert = (Button) findViewById(R.id.button_show_alert);
        btnSendToWeb = (Button) findViewById(R.id.button_send_to_web);
        edtTextToWeb = (EditText) findViewById(R.id.edit_text_to_web);
        txtFromWeb = (TextView) findViewById(R.id.text_from_web);
        webView = (WebView) findViewById(R.id.web_view);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setWebView();
        observeData();
    }

    private void setWebView() {
        viewModel.setWebContentsDebuggingEnabled(getApplicationInfo());
        WebSettings webSettings = webView.getSettings();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(viewModel.getWebAppInterface(), WebAppInterface.TAG);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl(viewModel.getJavascriptAndroidVersion("called by Android"));
                view.loadUrl(viewModel.getJavascriptAlert("Hello Web View"));

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d("Javascript Alert", "onJsAlert(" + view + ", " + url + ", " + message + ", " + result +")");
                result.confirm();
                return viewModel.isUndefined(message) ? true : super.onJsAlert(view, url, message, result);
            }
        });
        webView.loadUrl(viewModel.getHTMLPath());
    }

    private void observeData() {
        viewModel.observeLiveText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String value) {
                txtFromWeb.setText(value);
            }
        });
    }

    @Override
    public void onClick(View view) { Log.d(TAG,"onClick()");
        if(view.getId() == btnShowAlert.getId() && viewModel.isGreaterThanEqualKitKat()) {
            webView.evaluateJavascript(viewModel.getJavascriptAlert(edtTextToWeb.getText().toString()), null);
        } else if(view.getId() == btnShowAlert.getId() && viewModel.isNotGreaterThanEqualKitKat()) {
            webView.loadUrl(viewModel.getJavascriptAlert(edtTextToWeb.getText().toString()));
        } else if (view.getId() == btnSendToWeb.getId() && viewModel.isGreaterThanEqualKitKat()) {
            webView.evaluateJavascript(viewModel.getJavascriptParagraph(edtTextToWeb.getText().toString()), null);
        } else if (view.getId() == btnSendToWeb.getId() && viewModel.isNotGreaterThanEqualKitKat()) {
            webView.loadUrl(viewModel.getJavascriptParagraph(edtTextToWeb.getText().toString()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnShowAlert.setOnClickListener(this);
        btnSendToWeb.setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        btnShowAlert.setOnClickListener(null);
        btnSendToWeb.setOnClickListener(null);
    }

    @Override
    protected void onDestroy() {
        webView.removeJavascriptInterface(WebAppInterface.TAG);
        super.onDestroy();
    }
}