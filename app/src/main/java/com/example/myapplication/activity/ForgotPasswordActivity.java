package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Email;
import com.example.myapplication.R;
import com.example.myapplication.utils.ToastUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private Button resetPasswordBtn;
    private String username;
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        TextInputEditText emailEdt = findViewById(R.id.emailEdt);
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);
        backBtn = findViewById(R.id.backBtn);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USERNAME")) {
            username = intent.getStringExtra("USERNAME");
        }
        backBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            startActivity(intent1);
        });
        resetPasswordBtn.setOnClickListener(v -> {
            if(!emailEdt.getText().toString().isEmpty()) {
                callApiToGetResetPassWord(emailEdt.getText().toString());
            } else {
                ToastUtils.showCustomToast(ForgotPasswordActivity.this, "Vui lòng nhập email");
            }
        });
    }

    private void callApiToGetResetPassWord(String email) {
        APIService.apiService.callApiToGetResetPassWord(email)
                .enqueue(new Callback<Email>() {
                    @Override
                    public void onResponse(@NonNull Call<Email> call, @NonNull Response<Email> response) {
                        if (response.isSuccessful()) {
                            Email resetPassword = response.body();
                            Log.e("Reset password", "Reset password: " + resetPassword);
                            assert resetPassword != null;
                            updatePassword(resetPassword.getResetPassword());
                        } else {
                            ToastUtils.showCustomToast(ForgotPasswordActivity.this, "Reset Failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Email> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void updatePassword(String password) {
        password = hashPassword(password);
        APIService.apiService.updatePassword(username, password)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            ToastUtils.showCustomToast(ForgotPasswordActivity.this, "Reset Failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}