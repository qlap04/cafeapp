package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.APIService;
import com.example.myapplication.model.User;
import com.example.myapplication.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private TextView strUsername;
    private TextView strPassword;
    private TextInputLayout userContainer, passwordContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        strUsername = findViewById(R.id.userEdt);
        strPassword = findViewById(R.id.passEdt);
        userContainer = findViewById(R.id.userContainer);
        passwordContainer = findViewById(R.id.passwordContainer);
        Button signupBtn = findViewById(R.id.signupBtn);
        TextView mTextLogin = findViewById(R.id.textLogin);

        strUsername.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                userContainer.setHelperTextEnabled(false);
            } else {
                if (strUsername.getText().toString().isEmpty()) {
                    userContainer.setHelperTextEnabled(true);
                }
            }
        });

        strPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                passwordContainer.setHelperTextEnabled(false);
            } else {
                if (strUsername.getText().toString().isEmpty()) {
                    passwordContainer.setHelperTextEnabled(true);
                }
            }
        });

        mTextLogin.setOnClickListener(v -> clickTextSignup());
        signupBtn.setOnClickListener(v -> startRegister());
    }

    private void startRegister() {
        String username =  strUsername.getText().toString().trim();
        String password = strPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Vui lòng nhập tên người dùng và mật khẩu", Toast.LENGTH_LONG).show();
            return;
        }

        String hashedPassword = hashPassword(password);

        User newUser = new User("1", username, hashedPassword);
        APIService.apiService.signUpUser(newUser)
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Đăng ký thành công", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(SignupActivity.this, "Đăng ký thất bại", Toast.LENGTH_LONG).show();
                            try {
                                assert response.errorBody() != null;
                                Log.e("SignupActivity", "Lỗi khi đăng ký: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        Log.e("SignupActivity", "Lỗi khi gửi yêu cầu đăng ký: " + t.getMessage(), t);
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

    private void clickTextSignup() {
        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
