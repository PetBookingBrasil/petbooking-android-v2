package com.petbooking.UI.Menu.Settings.Terms;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

public class TermsActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        AppUtils.showLoadingDialog(this);

        mWebView = (WebView) findViewById(R.id.webview);
        webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://petbooking.com.br/v2/webviews/termos_de_uso");

        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                AppUtils.hideDialog();
            }
        });
    }
}
