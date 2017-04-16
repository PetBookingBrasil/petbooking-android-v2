package com.petbooking.UI.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.API.Auth.AuthService;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Auth.Models.SessionResp;
import com.petbooking.API.Generic.ErrorResp;
import com.petbooking.BaseActivity;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Interfaces.SocialCallback;
import com.petbooking.Managers.FacebookAuthManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.SocialUser;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseActivity {

    private SessionManager mSessionManager;
    private AuthService mAuthService;
    private FacebookAuthManager mFacebookAuthManager;

    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private Button mBtnLogin;
    private Button mBtnFacebookLogin;
    private Button mBtnSignup;
    private TextView mTvForgotPassword;

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.login) {
                login();
            } else if (id == R.id.facebookLogin) {
                mFacebookAuthManager.auth(LoginActivity.this);
            } else if (id == R.id.signup) {

            } else if (id == R.id.forgotPassword) {
                recoverPassword();
            }
        }
    };

    SocialCallback fbRequestCallback = new SocialCallback() {
        @Override
        public void onFacebookLoginSuccess(SocialUser user) {
            Log.d("USERS", new Gson().toJson(user));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSessionManager = SessionManager.getInstance();
        mAuthService = new AuthService();
        mFacebookAuthManager = new FacebookAuthManager();

        mTvForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        mEdtEmail = (EditText) findViewById(R.id.email);
        mEdtPassword = (EditText) findViewById(R.id.password);
        mBtnSignup = (Button) findViewById(R.id.signup);
        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnFacebookLogin = (Button) findViewById(R.id.facebookLogin);

        mBtnLogin.setOnClickListener(clickListener);
        mBtnSignup.setOnClickListener(clickListener);
        mTvForgotPassword.setOnClickListener(clickListener);

        mBtnFacebookLogin.setOnClickListener(clickListener);
        mFacebookAuthManager.init(fbRequestCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mFacebookAuthManager.getCallback().onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Login User with
     * email and password
     */
    public void login() {
        String email = mEdtEmail.getText().toString();
        String password = mEdtPassword.getText().toString();

        mAuthService.authUser(email, password, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                SessionResp sessionResp = (SessionResp) response;
                mSessionManager.setSessionToken(sessionResp.data.attributes.token);
                requestData(sessionResp.data.attributes.userID);
            }

            @Override
            public void onError(Object error) {

            }
        });
    }

    public void recoverPassword() {
        Log.d("RECOVER", "RECOVER");
    }

    /**
     * Go to Dashboard after logged
     */
    public void goToDashboard() {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashboardIntent);
    }

    /**
     * Get User information
     */
    public void requestData(String id) {
        mAuthService.getUser(id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AuthUserResp authUserResp = (AuthUserResp) response;
                User user = APIUtils.parseUser(authUserResp);
                mSessionManager.setUserLogged(user);
                goToDashboard();
            }

            @Override
            public void onError(Object error) {
                Log.d("ERROR", new Gson().toJson(error));
            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("EXIT", "EXIT");
    }
}
