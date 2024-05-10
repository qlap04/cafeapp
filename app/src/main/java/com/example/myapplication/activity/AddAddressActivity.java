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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Address;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAddressActivity extends AppCompatActivity {
    private ImageView backBtn;
    private Button addBtn;
    private TextView nameEdt, phoneNumEdt, addressEdt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_address);
        backBtn = findViewById(R.id.backBtn);
        nameEdt = findViewById(R.id.nameEdt);
        phoneNumEdt = findViewById(R.id.phoneNumEdt);
        addressEdt = findViewById(R.id.addressEdt);
        addBtn = findViewById(R.id.addBtn);
        backBtn.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
            startActivity(intent);
        });
        addBtn.setOnClickListener(v -> {
            addBtnOnClick();
            finish();
            Intent intent = new Intent(AddAddressActivity.this, AddressActivity.class);
            startActivity(intent);
        });
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private void addBtnOnClick() {
        String username = getUsernameFromSharedPreferences();
        String name = nameEdt.getText().toString();
        String phoneNum  = phoneNumEdt.getText().toString();
        String address = addressEdt.getText().toString();
        Address newAddress = new Address(username, name, phoneNum, address, false);
        APIService.apiService.saveDataAddress(newAddress).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("AddAddressActivity", "Success");
                } else {
                    Log.e("AddAddressActivity", "Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
}