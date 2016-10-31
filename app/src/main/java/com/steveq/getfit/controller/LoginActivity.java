package com.steveq.getfit.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.steveq.getfit.R;
import com.steveq.getfit.model.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;

public class LoginActivity extends Activity {

    @BindView(R.id.usernameEditText) EditText mUsernameEditText;
    @BindView(R.id.passwordEditText) EditText mPasswordEditText;
    @BindView(R.id.loginButton) Button mLoginButton;
    @BindView(R.id.createNewUserButton) Button mCreateNewUserButton;
    UserManager mUserManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        mUserManager = UserManager.getInstance();
        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!mUserManager.logIn(LoginActivity.this, mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString())){
                  mCreateNewUserButton.setVisibility(View.VISIBLE);
              }
            }
        });

        mCreateNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserManager.createNewUser(LoginActivity.this, mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                mUserManager.logIn(LoginActivity.this, mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
                mUsernameEditText.setText("");
                mPasswordEditText.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
