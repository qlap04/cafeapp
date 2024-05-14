package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffInforEditActivity extends AppCompatActivity {
    private int userId;
    private User user;
    private ImageView backBtn;
    private TextView idTxt, nameTxt;
    String[] item = {"admin", "staff"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_staff_infor_edit);

        idTxt = findViewById(R.id.idTxt);
        backBtn = findViewById(R.id.backBtn);
        nameTxt = findViewById(R.id.nameTxt);
        autoCompleteTextView = findViewById(R.id.auto_complete_txt);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID-USER")) {
            userId = intent.getIntExtra("ID-USER", 1);
            callApiGetInforUser(userId);
        }
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, item);

        autoCompleteTextView.setAdapter(adapterItems);
        callApiGetInforUser(userId);
        autoCompleteTextView.setOnItemClickListener((adapterView, view, position, id) -> {
            String item = adapterView.getItemAtPosition(position).toString();
            updateRole(userId, item);
        });
        backBtn.setOnClickListener(v -> finish());
    }

    private void updateRole(int userId, String role) {
        APIService.apiService.updateRoleForUserForAdmin(userId, role).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("StaffInforEditActivity", "Update role for user successfully");
                } else {
                    Log.e("StaffInforEditActivity", "Update role for user failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }

    private void setOnLine(int idValue) {
        APIService.apiService.setOnLineForUserForAdmin(idValue).enqueue(new Callback<Void>() {
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

    private void callApiGetInforUser(int id) {
        APIService.apiService.getInforUserForAdmin(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    user = response.body();
                    idTxt.setText(String.valueOf(user.getUserId()));
                    nameTxt.setText(user.getUsername());

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