package com.example.myapplication.fragment;

import static android.content.Context.MODE_PRIVATE;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.activity.AddStaffActivity;
import com.example.myapplication.activity.AttendanceActivity;
import com.example.myapplication.activity.ChangePasswordActivity;
import com.example.myapplication.activity.ContactActivity;
import com.example.myapplication.activity.EditProfileActivity;
import com.example.myapplication.activity.ListProduct1Activity;
import com.example.myapplication.activity.LoginActivity;
import com.example.myapplication.activity.ReplyActivity;
import com.example.myapplication.R;
import com.example.myapplication.activity.StaffInforActivity;
import com.example.myapplication.activity.StaffInforEditActivity;

public class AccountFragment extends Fragment {
    private TextView usernameTxt;
    private Button editBtn;
    private LinearLayout linearLayout1;
    private RelativeLayout addTxt, attendanceTxt, listProductTxt, listStaffTxt, replyTxT, contactTxt, changePassTxt, logoutTxT;
    Dialog dialog;
    Button btnDialogCancel, btnDialogLogout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);

        linearLayout1 = rootView.findViewById(R.id.linearLayout1);
        usernameTxt = rootView.findViewById(R.id.usernameTxT);
        editBtn = rootView.findViewById(R.id.editBtn);
        addTxt = rootView.findViewById(R.id.addTxt);
        attendanceTxt = rootView.findViewById(R.id.attendanceTxt);
        listProductTxt = rootView.findViewById(R.id.listProductTxt);
        listStaffTxt = rootView.findViewById(R.id.listStaffTxt);
        replyTxT = rootView.findViewById(R.id.replyTxt);
        contactTxt = rootView.findViewById(R.id.contactTxt);
        changePassTxt = rootView.findViewById(R.id.changePassTxt);
        logoutTxT = rootView.findViewById(R.id.logoutTxt);
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.custom_dialog_box);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(getContext(),R.drawable.custom_dialog_bg));
        dialog.setCancelable(false);

        btnDialogLogout = dialog.findViewById(R.id.btnDialogLogout);
        btnDialogCancel = dialog.findViewById(R.id.btnDialogCancel);
        btnDialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDialogLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSavedUsername();
                clearSavedUserRole();
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        String username = getUsernameFromSharedPreferences();
        usernameTxt.setText(username);

        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), EditProfileActivity.class);
            startActivity(intent);
        });
        addTxt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AddStaffActivity.class);
            startActivity(intent);
        });
        attendanceTxt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), AttendanceActivity.class);
            startActivity(intent);
        });

        listProductTxt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ListProduct1Activity.class);
            startActivity(intent);
        });

        listStaffTxt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), StaffInforActivity.class);
            startActivity(intent);
        });

        replyTxT.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ReplyActivity.class);
            startActivity(intent);
        });

        contactTxt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ContactActivity.class);
            startActivity(intent);
        });

        changePassTxt.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ChangePasswordActivity.class);
            startActivity(intent);
        });

        logoutTxT.setOnClickListener(v -> {
                dialog.show();
        });

        String userRole = getUsernameRoleSharedPreferences();

        if ("client".equals(userRole)) {
            linearLayout1.setVisibility(View.GONE);
        } else {
            if ("staff".equals(userRole)) {
                addTxt.setVisibility(View.GONE);
                listStaffTxt.setVisibility(View.GONE);
                listProductTxt.setVisibility(View.GONE);
            } else if ("admin".equals(userRole)) {
                attendanceTxt.setVisibility(View.GONE);
            }
        }

        return rootView;
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private String getUsernameRoleSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("userRole", "");
    }
    private void clearSavedUsername() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.apply();
    }
    private void clearSavedUserRole() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("userRole");
        editor.apply();
    }
}