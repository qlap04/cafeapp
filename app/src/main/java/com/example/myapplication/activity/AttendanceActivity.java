package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AttendanceActivity extends AppCompatActivity {
    //
    private User user;
    private String username, role;
    private ImageView backBtn;
    private Button attendanceBtn, logoutBtn;
    private TextView idTxt, usernameTxt, nameTxt, emailTxt, phoneNumTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_attendance);

        backBtn = findViewById(R.id.backBtn);
        idTxt = findViewById(R.id.idTxt);
        usernameTxt = findViewById(R.id.usernameTxt);
        nameTxt = findViewById(R.id.nameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        phoneNumTxt = findViewById(R.id.phoneNumTxt);
        attendanceBtn = findViewById(R.id.attendanceBtn);
        logoutBtn = findViewById(R.id.logoutBtn);

        getUsernameFromSharedPreferences();
        getRoleFromSharedPreferences();
        callApiGetInforUser(username);
        getWorkingStatusUser(username);

        backBtn.setOnClickListener(v -> finish());
        attendanceBtn.setOnClickListener(v -> {
            setOnLine(user.getUserId());
            finish();
        });
        logoutBtn.setOnClickListener(v -> {
            setOffLine(user.getUserId());
            finish();
        });
    }
    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }
    private void getRoleFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        role = sharedPreferences.getString("username", "");
    }
    private void setOnLine(int id) {
        APIService.apiService.setOnLineForUser(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("StaffInforEditActivity", "Set online for user successfully");
                } else {
                    Log.e("StaffInforEditActivity", "Set online for user failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void setOffLine(int id) {
        APIService.apiService.setOffLineForUser(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("StaffInforEditActivity", "Set offline for user successfully");
                } else {
                    Log.e("StaffInforEditActivity", "Set offline for user failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void getWorkingStatusUser(String username) {
        APIService.apiService.getWorkingStatusUser(username).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body()) {
                        attendanceBtn.setVisibility(View.GONE);
                    } else {
                        logoutBtn.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("AttendaceActivity", "Get working status user failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void callApiGetInforUser(String username) {
        APIService.apiService.getInforUser(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    idTxt.setText(String.valueOf(user.getUserId()));
                    usernameTxt.setText(user.getUsername());
                    nameTxt.setText(user.getFullname());
                    emailTxt.setText(user.getEmail());
                    phoneNumTxt.setText(user.getPhoneNumber());
                    if (user.getRole().equals("admin") || user.getRole().equals("client")) {
                        attendanceBtn.setVisibility(View.GONE);
                    }
                } else {
                    Log.e("StaffInforEditActivity", "Cannot get infor user");
                }
            }

            @Override
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
}