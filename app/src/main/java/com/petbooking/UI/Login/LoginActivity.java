package com.petbooking.UI.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.petbooking.Managers.SessionManager;
import com.petbooking.R;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private SessionManager mSessionManager;

    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private Button mBtnLogin;
    private Button mBtnFacebookLogin;
    private Button mBtnSignup;
    private TextView mTvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSessionManager = SessionManager.getInstance();

        mTvForgotPassword = (TextView) findViewById(R.id.forgotPassword);
        mEdtEmail = (EditText) findViewById(R.id.email);
        mEdtPassword = (EditText) findViewById(R.id.password);
        mBtnSignup = (Button) findViewById(R.id.signup);
        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnFacebookLogin = (Button) findViewById(R.id.facebookLogin);

    }
}
