package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.myapplication.api.APIService;
import com.example.myapplication.adapter.TrackingAdapter;
import com.example.myapplication.model.Cart;
import com.example.myapplication.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackingActivity extends AppCompatActivity {
    private String role;
    private ImageView backBtn;
    private RecyclerView rcTracking;
    private Button cancelOrderBtn, receiveOrderBtn;
    private TextView orderDateTxt, deliverDateTxt;
    private List<Cart> productListInCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tracking);

        backBtn = findViewById(R.id.backBtn);
        orderDateTxt = findViewById(R.id.orderDateTxt);
        deliverDateTxt = findViewById(R.id.deliverDateTxt);
        cancelOrderBtn = findViewById(R.id.cancelOrderBtn);
        receiveOrderBtn = findViewById(R.id.receiveOrderBtn);
        rcTracking = findViewById(R.id.rcTracking);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcTracking.setLayoutManager(linearLayoutManager);

        getRoleFromSharedPreferences();
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("ID-PRODUCT")) {
            int idValue = intent.getIntExtra("ID-PRODUCT", 1);
            if (role.equals("client")) {
                callApiGetProductsInTraking(idValue);
            }else {
                callApiGetProductsInTrakingForStaff(idValue);
            }
            cancelOrderBtn.setOnClickListener(v -> cancelOrder(idValue));
            receiveOrderBtn.setOnClickListener(v -> receiveOrder(idValue));
        }

        backBtn.setOnClickListener(v -> {
            navigateToHome();
        });
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private void getRoleFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        role = sharedPreferences.getString("userRole", "");
    }
    private void navigateToHome() {
        finish();
        Intent intent = new Intent(TrackingActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    private void cancelOrder(int id) {
        APIService.apiService.cancelOrder(id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            navigateToHome();
                            Log.e("Tracking Activity", "Cancel order" + "Cancel order successfully");
                        } else {
                            Log.e("Tracking Activity", "Cancel order" + "Cancel order failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void receiveOrder(int id) {
        APIService.apiService.receiveOrder(id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            navigateToHome();
                            Log.e("Tracking Activity", "Receive order: " + "Receive order successfully");
                        } else {
                            Log.e("Tracking Activity", "Receive order: " + "Receive order failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void callApiGetProductsInTraking(int id) {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getProductInTracking(username, id)
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        productListInCart = response.body();
                        TrackingAdapter trackingAdapter = new TrackingAdapter(productListInCart);
                        rcTracking.setAdapter(trackingAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void callApiGetProductsInTrakingForStaff(int id) {
        APIService.apiService.getProductInTrackingForStaff(id)
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        productListInCart = response.body();
                        TrackingAdapter trackingAdapter = new TrackingAdapter(productListInCart);
                        rcTracking.setAdapter(trackingAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
}