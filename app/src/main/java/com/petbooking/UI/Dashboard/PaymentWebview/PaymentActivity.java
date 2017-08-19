package com.petbooking.UI.Dashboard.PaymentWebview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.petbooking.API.Appointment.APIAppointmentConstants;
import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.BuildConfig;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.Utils.AppUtils;

import java.util.HashMap;
import java.util.Objects;

public class PaymentActivity extends AppCompatActivity {

    private String url;
    private AppointmentManager mAppointmentManager;
    private AppointmentService mAppointmentService;

    private WebView mPaymentWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mAppointmentManager = AppointmentManager.getInstance();
        mAppointmentService = new AppointmentService();

        String cartId = mAppointmentManager.getCartId().replace("\"", "");

        url = String.format(APIAppointmentConstants.PAYMENT_WEBVIEW_URL, BuildConfig.BASE_URL,
                cartId, SessionManager.getInstance().getSessionToken());
        url = url.replace("/api", "");

        mPaymentWebview = (WebView) findViewById(R.id.payment_webview);
        createPaymentWebView();
    }

    /**
     * Create Webview
     */
    private void createPaymentWebView() {
        AppUtils.showLoadingDialog(this);
        WebSettings webSettings = mPaymentWebview.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mPaymentWebview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                AppUtils.hideDialog();
            }
        });

        mPaymentWebview.loadUrl(url);
        mPaymentWebview.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                if (cm.message().equals(APIConstants.PAYMENT_OK_TAG)) {
                    goToDashboard();
                }

                return true;
            }


        });
    }

    /**
     * Go to dashboard
     */
    private void goToDashboard() {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashboardIntent);
    }
}
