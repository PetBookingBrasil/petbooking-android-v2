package com.petbooking.UI.Login;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.petbooking.R;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private EditText mEdtEmail;
    private EditText mEdtPassword;
    private Button mBtnLogin;
    private Button mBtnFacebookLogin;
    private TextView mTvSignup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEdtEmail = (EditText) findViewById(R.id.email);
        mEdtPassword = (EditText) findViewById(R.id.email);
        mTvSignup = (TextView) findViewById(R.id.signup);
        mBtnLogin = (Button) findViewById(R.id.login);
        mBtnFacebookLogin = (Button) findViewById(R.id.facebookLogin);
    }
}
