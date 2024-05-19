package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.APIService;
import com.example.myapplication.model.User;
import com.example.myapplication.R;

import com.example.myapplication.utils.ToastUtils;
import com.google.android.material.textfield.TextInputLayout;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    //
    private EditText username;
    private EditText password;
    private User mUser;
    private TextInputLayout userContainer, passwordContainer;
    private List<User> mListUser;
    private ImageView facebookBtn, googleBtn, twitterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.userEdt);
        password = findViewById(R.id.passEdt);
        Button loginBtn = findViewById(R.id.loginBtn);
        facebookBtn = findViewById(R.id.facebookBtn);
        googleBtn = findViewById(R.id.googleBtn);
        twitterBtn = findViewById(R.id.twitterBtn);
        userContainer = findViewById(R.id.userContainer);
        passwordContainer = findViewById(R.id.passwordContainer);
        TextView forgetPassTxt = findViewById(R.id.forgetPassTxt);
        TextView mTextSignup = findViewById(R.id.textSignup);


        mListUser = new ArrayList<>();

        checkLoggedInUser();

        getListUsers();

        username.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                userContainer.setHelperTextEnabled(false);
            } else {
                if (username.getText().toString().isEmpty()) {
                    userContainer.setHelperTextEnabled(true);
                }
            }
        });

        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                passwordContainer.setHelperTextEnabled(false);
            } else {
                if (username.getText().toString().isEmpty()) {
                    passwordContainer.setHelperTextEnabled(true);
                }
            }
        });
        forgetPassTxt.setOnClickListener(v -> {
            String strUsername;
            if (username.getText().toString().isEmpty()) {
                ToastUtils.showCustomToast(LoginActivity.this, "Vui lòng nhập email");
                return;
            } else {
                strUsername = username.getText().toString().toLowerCase();
            }
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            intent.putExtra("USERNAME", strUsername);
            startActivity(intent);
        });
        loginBtn.setOnClickListener(v -> clickLogin());
        mTextSignup.setOnClickListener(v -> clickTextSignup());
    }


    private void clickLogin() {
        String strUsername = username.getText().toString().trim();
        String strPassword = password.getText().toString().trim();
        if (strUsername.isEmpty() || strPassword.isEmpty()) {
            ToastUtils.showCustomToast(LoginActivity.this, "Vui lòng nhập đấy đủ thông tin");
            return;
        }
        if (mListUser == null || mListUser.isEmpty()) {
            return;
        }
        boolean isHasUser = false;
        for (User user : mListUser) {
            if (strUsername.equals(user.getUsername()) && validatePassword(strPassword, user.getPassword())) {
                isHasUser = true;
                mUser = user;
                break;
            }
        }
        if (isHasUser) {
            saveUsername(strUsername);
            saveRole(mUser.getRole());
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("object_user", mUser);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            ToastUtils.showCustomToast(LoginActivity.this, "Sai tài khoản hoặc mật khẩu");
        }
    }


    private boolean validatePassword(String inputPassword, String storedPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(inputPassword.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            String hashedInputPassword = hexString.toString();

            return hashedInputPassword.equals(storedPassword);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clickTextSignup() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
    }

    private void checkLoggedInUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        if (!savedUsername.isEmpty()) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getListUsers() {
        APIService.apiService.getListUsers()
                .enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                        mListUser = response.body();
                        assert mListUser != null;
                        Log.e("List user: ", String.valueOf(mListUser.size()));
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }

    private void saveUsername(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }
    private void saveRole(String userRole) {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userRole", userRole);
        editor.apply();
    }

}

