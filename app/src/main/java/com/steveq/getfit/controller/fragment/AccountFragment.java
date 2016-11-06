package com.steveq.getfit.controller.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.steveq.getfit.R;
import com.steveq.getfit.model.UserManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends Fragment {

    @BindView(R.id.newLoginEditText) EditText newLoginEditText;
    @BindView(R.id.oldPasswordEditText) EditText oldPasswordEditText;
    @BindView(R.id.newPasswordEditText) EditText newPasswordEditText;
    @BindView(R.id.changeLoginButton) Button changeLoginButton;
    @BindView(R.id.changePasswordButton) Button changePasswordButton;
    @BindView(R.id.deleteUserButton) Button deleteUserButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.account_list, container, false);
        ButterKnife.bind(this, view);

        changeLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance().changeUsername(getActivity(),
                                                        newLoginEditText.getText().toString(),
                                                        oldPasswordEditText.getText().toString());
            }
        });

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance().changePassword(getActivity(),
                                                        oldPasswordEditText.getText().toString(),
                                                        newPasswordEditText.getText().toString());
            }
        });

        deleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserManager.getInstance().removeUser(getActivity(), oldPasswordEditText.getText().toString());
            }
        });

        return view;
    }
}
