package com.steveq.getfit.controller.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.steveq.getfit.R;
import com.steveq.getfit.controller.DismissKeybord;
import com.steveq.getfit.model.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends Activity {

    @BindView(R.id.usernameEditText) EditText mUsernameEditText;
    @BindView(R.id.passwordEditText) EditText mPasswordEditText;
    @BindView(R.id.loginButton) Button mLoginButton;
    @BindView(R.id.createNewUserButton) Button mCreateNewUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = this.getLayoutInflater().inflate(R.layout.login_activity, null, false);
        view.setOnClickListener(new DismissKeybord(this));
        setContentView(view);
        ButterKnife.bind(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!UserManager.getInstance().logIn(LoginActivity.this, mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString())){
                  mCreateNewUserButton.setVisibility(View.VISIBLE);
              }
            }
        });

        mCreateNewUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance().createNewUser(LoginActivity.this, mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());

                UserManager.getInstance().logIn(LoginActivity.this, mUsernameEditText.getText().toString(), mPasswordEditText.getText().toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUsernameEditText.setText("");
        mPasswordEditText.setText("");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
