package com.petbooking.UI.Dashboard.PaymentWebview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebMessage;
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
import com.petbooking.UI.Menu.Agenda.AgendaActivity;
import com.petbooking.Utils.AppUtils;

public class PaymentActivity extends AppCompatActivity {

    private String url;
    private AppointmentManager mAppointmentManager;
    private AppointmentService mAppointmentService;

    private WebView mPaymentWebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        mPaymentWebview.loadUrl(url);
        mPaymentWebview.addJavascriptInterface(this,"ok");
        mPaymentWebview.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                AppUtils.hideDialog();
            }

        });



        mPaymentWebview.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cm) {
                cm.messageLevel();
                if (cm.message().equals(APIConstants.PAYMENT_OK_TAG)) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mAppointmentManager.reset();
                            goToDashboard();
                        }
                    },8000);
                }

                return true;
            }
        });
    }

    /**
     * Go to dashboard
     */
    private void goToDashboard() {
        Intent dashboardIntent = new Intent(this, AgendaActivity.class);
        startActivity(dashboardIntent);
    }

   @JavascriptInterface
    public void postMessage(String ok){
        goToDashboard();
   }
}
