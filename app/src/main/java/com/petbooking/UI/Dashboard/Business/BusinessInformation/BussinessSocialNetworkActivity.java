package com.petbooking.UI.Dashboard.Business.BusinessInformation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebView;

import com.petbooking.BaseActivity;
import com.petbooking.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by victorneves on 01/02/18.
 */

public class BussinessSocialNetworkActivity extends BaseActivity {
    @BindView(R.id.webview)
    WebView webView;
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_social_network);
        ButterKnife.bind(this);
        url = getIntent().getStringExtra("url");
        
    }
}
