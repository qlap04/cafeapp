package com.example.myapplication.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.activity.ChangePasswordActivity;
import com.example.myapplication.activity.ContactActivity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.ReplyActivity;
import com.example.myapplication.R;

public class AccountFragment extends Fragment {
    private TextView usernameTxt;
    private LinearLayout replyTxT, contactTxt, changePassTxt, logoutTxT;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        usernameTxt = rootView.findViewById(R.id.usernameTxT);
        replyTxT = rootView.findViewById(R.id.replyTxt);
        contactTxt = rootView.findViewById(R.id.contactTxt);
        changePassTxt = rootView.findViewById(R.id.changePassTxt);
        logoutTxT = rootView.findViewById(R.id.logoutTxt);
        String username = getUsernameFromSharedPreferences();
        usernameTxt.setText(username);
        replyTxT.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ReplyActivity.class);
            startActivity(intent);
        });
//        contactTxT.setOnClickListener(v -> {
//
//        });
        contactTxt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ContactActivity.class);
            startActivity(intent);
        });
        changePassTxt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });
        logoutTxT.setOnClickListener(v -> {
            clearSavedUsername();
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
        return rootView;
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private void clearSavedUsername() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.apply();
    }
}