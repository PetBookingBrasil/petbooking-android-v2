package com.petbooking.UI.Menu.Settings.Contact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.petbooking.R;

public class ContactActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mWebView = (WebView) findViewById(R.id.webview);
        webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://petbooking.com.br/webviews/contact");
    }
}
