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
    private RecyclerView rcTracking;
    private ImageView backBtn;
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
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID-PRODUCT")) {
            int idValue = intent.getIntExtra("ID-PRODUCT", 1);
            callApiGetProductsInTraking(idValue);
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
    private void navigateToHome() {
        finish();
        Intent intent = new Intent(TrackingActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    private void cancelOrder(int id) {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.cancelOrder(username, id)
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
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.receiveOrder(username, id)
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
//                        Date orderedDate = productListInCart.get(0).getOrderedAt();
//                        if (orderedDate != null) {
//                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//                            String orderedDateString = dateFormat.format(orderedDate);
//                            deliverDateTxt.setText(orderedDateString);
//                        } else {
//                            deliverDateTxt.setText("Không có ngày đặt hàng");
//                        }
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