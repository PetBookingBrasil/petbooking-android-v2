package com.petbooking.UI.Login;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.petbooking.API.Auth.AuthService;
import com.petbooking.API.Auth.Models.AuthUserResp;
import com.petbooking.API.Auth.Models.SessionResp;
import com.petbooking.API.Generic.APIError;
import com.petbooking.API.User.UserService;
import com.petbooking.App;
import com.petbooking.BaseActivity;
import com.petbooking.Constants.APIConstants;
import com.petbooking.Constants.AppConstants;
import com.petbooking.Events.ShowSnackbarEvt;
import com.petbooking.Interfaces.APICallback;
import com.petbooking.Interfaces.SocialCallback;
import com.petbooking.Managers.AlarmReceiver;
import com.petbooking.Managers.FacebookAuthManager;
import com.petbooking.Managers.SessionManager;
import com.petbooking.Models.User;
import com.petbooking.R;
import com.petbooking.UI.Dashboard.DashboardActivity;
import com.petbooking.UI.Menu.Settings.Privacy.PrivacyActivity;
import com.petbooking.UI.Menu.Settings.Terms.TermsActivity;
import com.petbooking.UI.RecoverPassword.RecoverPasswordActivity;
import com.petbooking.UI.SignUp.SignUpActivity;
import com.petbooking.Utils.APIUtils;
import com.petbooking.Utils.AppUtils;
import com.petbooking.Utils.CommonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity {

    private SessionManager mSessionManager;
    private AuthService mAuthService;
    private UserService mUserService;
    private FacebookAuthManager mFacebookAuthManager;
    private AlarmManager mAlarmManager;

    private ImageView mIvAppLogo;
    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private Button mBtnLogin;
    private Button mBtnFacebookLogin;
    private Button mBtnSignup;
    private TextView mTvForgotPassword;
    private TextView mTvTerms;

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == R.id.login) {
                login();
            } else if (id == R.id.facebookLogin) {
                mFacebookAuthManager.auth(LoginActivity.this);
            } else if (id == R.id.signup) {
                goToSignup(null);
            } else if (id == R.id.forgotPassword) {
                recoverPassword();
            } else if (id == R.id.appLogo) {
                CommonUtils.hideKeyboard(LoginActivity.this);
            }
        }
    };

    SocialCallback fbRequestCallback = new SocialCallback() {
        @Override
        public void onFacebookLoginSuccess(User user) {
            authFB(user);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSessionManager = SessionManager.getInstance();
        mAuthService = new AuthService();
        mUserService = new UserService();
        mFacebookAuthManager = new FacebookAuthManager();

        mIvAppLogo = (ImageView) findViewById(R.id.appLogo);
        mTvForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        mTvTerms = (TextView) findViewById(R.id.appTerms);
        mEdtEmail = (EditText) findViewById(R.id.email);
        mEdtPassword = (EditText) findViewById(R.id.password);
        mBtnSignup = (Button) findViewById(R.id.signup);
        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnFacebookLogin = (Button) findViewById(R.id.facebookLogin);

        mBtnLogin.setOnClickListener(clickListener);
        mBtnSignup.setOnClickListener(clickListener);
        mTvForgotPassword.setOnClickListener(clickListener);
        mIvAppLogo.setOnClickListener(clickListener);
        mBtnFacebookLogin.setOnClickListener(clickListener);

        mTvForgotPassword.setPaintFlags(mTvForgotPassword.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        mFacebookAuthManager.init(fbRequestCallback);
        createMultipleClick();
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
        final String email = mEdtEmail.getText().toString();
        final String password = mEdtPassword.getText().toString();

        if (email.equals("") || password.equals("")) {
            EventBus.getDefault().post(new ShowSnackbarEvt(R.string.error_fields_empty, Snackbar.LENGTH_SHORT));
            return;
        }

        if (!CommonUtils.isValidEmail(email)) {
            EventBus.getDefault().post(new ShowSnackbarEvt(R.string.error_invalid_email, Snackbar.LENGTH_SHORT));
            return;
        }

        CommonUtils.hideKeyboard(LoginActivity.this);
        AppUtils.showLoadingDialog(this);
        mAuthService.authUser(email, password, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                SessionResp sessionResp = (SessionResp) response;
                mSessionManager.setSessionToken(sessionResp.data.attributes.token);
                mSessionManager.setSessionExpirationDate(sessionResp.data.attributes.expiresAt);
                mSessionManager.setLastLogin(email, password);
                scheduleRefreshToken(AppConstants.SESSION_TOKEN);
                requestData(sessionResp.data.attributes.userID);
            }

            @Override
            public void onError(Object error) {
                APIError apiError = (APIError) error;
                if (apiError.code == APIConstants.ERROR_CODE_INVALID_LOGIN) {
                    EventBus.getDefault().post(new ShowSnackbarEvt(R.string.error_invalid_login, Snackbar.LENGTH_SHORT));
                    AppUtils.hideDialog();
                }
            }
        });
    }

    /**
     * Login With Facebook
     */
    public void authFB(final User user) {
        mAuthService.authUserSocial("facebook", user.providerToken, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                SessionResp sessionResp = (SessionResp) response;
                mSessionManager.setSessionToken(sessionResp.data.attributes.token);
                mSessionManager.setSessionExpirationDate(sessionResp.data.attributes.expiresAt);
                mSessionManager.setLastFBToken(user.providerToken);
                scheduleRefreshToken(AppConstants.SESSION_TOKEN);
                requestData(sessionResp.data.attributes.userID);
            }

            @Override
            public void onError(Object error) {
                APIError apiError = (APIError) error;
                if (apiError.code == APIConstants.ERROR_CODE_INVALID_LOGIN) {
                    goToSignup(user);
                }
            }
        });
    }

    /**
     * Get User information
     */
    public void requestData(String id) {
        mUserService.getUser(id, new APICallback() {
            @Override
            public void onSuccess(Object response) {
                AuthUserResp authUserResp = (AuthUserResp) response;
                User user = APIUtils.parseUser(authUserResp);
                mSessionManager.setUserLogged(user);
                goToDashboard();
                AppUtils.hideDialog();
            }

            @Override
            public void onError(Object error) {
            }
        });
    }

    /**
     * Go to Recover Password Screen
     */
    public void recoverPassword() {
        Intent recoverIntent = new Intent(this, RecoverPasswordActivity.class);
        startActivity(recoverIntent);
    }

    /**
     * Go to Dashboard after logged
     */
    public void goToDashboard() {
        Intent dashboardIntent = new Intent(this, DashboardActivity.class);
        startActivity(dashboardIntent);
    }

    /**
     * Go To Register Page
     */
    private void goToSignup(User user) {
        Intent signupIntent = new Intent(this, SignUpActivity.class);

        if (user != null) {
            String parsedUser = new Gson().toJson(user);
            signupIntent.putExtra(AppConstants.SOCIAL_LOGIN, true);
            signupIntent.putExtra(AppConstants.USER_WRAPPED, parsedUser);
        }

        startActivity(signupIntent);
    }

    /**
     * Refresh Auth Token
     */
    public void scheduleRefreshToken(String type) {
        Intent mIntent;
        PendingIntent mAlarmIntent;
        long dateMillis = CommonUtils.getRefreshDate(mSessionManager.getSessionExpirationDate());

        mAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mIntent = new Intent(this, AlarmReceiver.class);
        mIntent.putExtra(type, true);
        mAlarmIntent = PendingIntent.getBroadcast(this, AppConstants.REFRESH_SESSION, mIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, dateMillis, mAlarmIntent);
    }

    public void createMultipleClick() {
        String str = getResources().getString(R.string.app_terms_info);
        SpannableString ss = new SpannableString(str);
        final String first = "Termos de uso";
        final String second = "Política de Privacidade";

        int firstIndex = str.indexOf(first);
        int secondIndex = str.indexOf(second);

        ClickableSpan firstWord = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                goToTerms();
            }
        };

        ClickableSpan secondWord = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                goToPrivacy();
            }
        };
//        ss.setSpan(firstWord, firstIndex, firstIndex + first.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
  //      ss.setSpan(secondWord, secondIndex, secondIndex + second.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvTerms.setLinksClickable(true);
        mTvTerms.setMovementMethod(LinkMovementMethod.getInstance());
        mTvTerms.setText(ss, TextView.BufferType.SPANNABLE);
    }

    @Override
    public void onBackPressed() {
        Log.d("EXIT", "EXIT");
    }

    public void goToTerms() {
        Intent contactIntent = new Intent(this, TermsActivity.class);
        startActivity(contactIntent);
    }

    public void goToPrivacy() {
        Intent contactIntent = new Intent(this, PrivacyActivity.class);
        startActivity(contactIntent);
    }
}
