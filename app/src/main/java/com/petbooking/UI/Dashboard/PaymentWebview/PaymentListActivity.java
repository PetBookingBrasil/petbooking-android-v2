package com.petbooking.UI.Dashboard.PaymentWebview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.petbooking.API.Appointment.APIAppointmentConstants;
import com.petbooking.API.Appointment.AppointmentService;
import com.petbooking.BuildConfig;
import com.petbooking.Managers.AppointmentManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.R;

/**
 * Created by Victor on 03/02/18.
 */

public class PaymentListActivity extends AppCompatActivity {
    private String url;
    private AppointmentManager mAppointmentManager;
    private AppointmentService mAppointmentService;

    private WebView mPaymentWebview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mAppointmentManager = AppointmentManager.getInstance();
        mAppointmentService = new AppointmentService();


        url = String.format(APIAppointmentConstants.PAYMENT_WEBVIEW_USER, BuildConfig.BASE_URL,
                 SessionManager.getInstance().getSessionToken());
        url = url.replace("/api", "");

        mPaymentWebview = (WebView) findViewById(R.id.payment_webview);
        mPaymentWebview.loadUrl(url);
    }
}
