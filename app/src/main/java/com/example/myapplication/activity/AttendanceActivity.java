package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

    private User user;
    private String username;
    private ImageView backBtn;
    private Button attendanceBtn;
    private TextView idTxt, nameTxt, roleTxt;
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
        nameTxt = findViewById(R.id.nameTxt);
        roleTxt = findViewById(R.id.roleTxt);
        attendanceBtn = findViewById(R.id.attendanceBtn);

        getUsernameFromSharedPreferences();
        callApiGetInforUser(username);

        backBtn.setOnClickListener(v -> finish());
        attendanceBtn.setOnClickListener(v -> setOnLine(user.getUserId()));

    }
    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
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

    private void callApiGetInforUser(String username) {
        APIService.apiService.getInforUser(username).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    idTxt.setText(String.valueOf(user.getUserId()));
                    nameTxt.setText(user.getUsername());
                    roleTxt.setText(user.getRole());
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