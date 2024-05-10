package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.api.APIService;
import com.example.myapplication.model.User;
import com.example.myapplication.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private String username;
    private String oldPassword;
    private EditText oldPassEdt, passEdt1, passEdt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_change_password);
        Button changePassBtn = findViewById(R.id.changePassBtn);
        oldPassEdt = findViewById(R.id.oldPassEdt);
        passEdt1 = findViewById(R.id.passEdt1);
        passEdt2 = findViewById(R.id.passEdt2);
        ImageView backBtn = findViewById(R.id.backBtn);
        getPassword();
        backBtn.setOnClickListener(v -> finish());
        changePassBtn.setOnClickListener(v -> {
            if(hashPassword(oldPassEdt.getText().toString()).equals(oldPassword) && passEdt1.getText().toString().equals(passEdt2.getText().toString())) {
                updatePassword(passEdt1.getText().toString());
            }
        });
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private void getPassword() {
        username = getUsernameFromSharedPreferences();
        APIService.apiService.getUserByUsername(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    oldPassword = user.getPassword();
                } else {
                    Log.e("ChangePasswordActivity", "Fail to get password from api");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("Api Error", "Fail to get password from api", t);
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
                            Toast.makeText(ChangePasswordActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
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
