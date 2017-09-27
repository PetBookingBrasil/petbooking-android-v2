package com.petbooking.UI.Menu.Settings.Privacy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.petbooking.R;
import com.petbooking.Utils.AppUtils;

public class PrivacyActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        AppUtils.showLoadingDialog(this);

        mWebView = (WebView) findViewById(R.id.webview);
        webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://petbooking.com.br/v2/webviews/privacidade");

        mWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                AppUtils.hideDialog();
            }
        });
    }
}
