package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private String username, role;
    private boolean isConfirmed, isOrdered;
    private ImageView backBtn, imageStage2;
    private RecyclerView rcTracking;
    private ProgressBar progressBar;
    private Button cancelOrderBtn, receiveOrderBtn, receiveOrderForStaffBtn;
    private TextView orderDateTxt, deliverDateTxt, billTxt;
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
        progressBar = findViewById(R.id.progressBar);
        imageStage2 = findViewById(R.id.imageStage2);
        cancelOrderBtn = findViewById(R.id.cancelOrderBtn);
        receiveOrderBtn = findViewById(R.id.receiveOrderBtn);
        receiveOrderForStaffBtn = findViewById(R.id.receiveOrderForStaffBtn);
        billTxt = findViewById(R.id.billTxt);
        rcTracking = findViewById(R.id.rcTracking);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcTracking.setLayoutManager(linearLayoutManager);

        getUsernameFromSharedPreferences();
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
            billTxt.setOnClickListener(v -> {
                Intent intent1 = new Intent(v.getContext(), BillActivity.class);
                intent1.putExtra("ID-PRODUCT", idValue);
                v.getContext().startActivity(intent1);
            });
        }
        if (!role.equals("client")) {
            receiveOrderBtn.setVisibility(View.GONE);
        } else {
            receiveOrderForStaffBtn.setVisibility(View.GONE);
        }
        backBtn.setOnClickListener(v -> {
            finish();
        });
        receiveOrderForStaffBtn.setOnClickListener(v -> {
            Toast.makeText(this, "Nhận hàng thành công", Toast.LENGTH_SHORT).show();
            finish();
            setProductIncartIsOrdered();
        });
    }



    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
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
    private void setProductIncartIsOrdered() {
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.setProductInCartIsOdered(username)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e("PaymentMethodActivity", "Set is ordered:  " + "Successfully");
                        } else {
                            Log.e("PaymentMethodActivity", "Set is ordered:  " + "Failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void callApiGetProductsInTraking(int id) {
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getProductInTracking(username, id)
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        productListInCart = response.body();
                        assert productListInCart != null;
                        isOrdered = productListInCart.get(0).getOrdered();
                        int progressColor = ContextCompat.getColor(TrackingActivity.this, R.color.md_green_500);
                        if (isOrdered) {
                            progressBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
                            imageStage2.setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
                            cancelOrderBtn.setVisibility(View.GONE);
                            receiveOrderForStaffBtn.setVisibility(View.GONE);
                            receiveOrderBtn.setVisibility(View.VISIBLE);
                        }
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
                        assert productListInCart != null;
                        isOrdered = productListInCart.get(0).getOrdered();
                        int progressColor = ContextCompat.getColor(TrackingActivity.this, R.color.md_green_500);
                        if (isOrdered) {
                            progressBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
                            imageStage2.setColorFilter(progressColor, PorterDuff.Mode.SRC_IN);
                            cancelOrderBtn.setVisibility(View.GONE);
                            receiveOrderForStaffBtn.setVisibility(View.GONE);
                            receiveOrderBtn.setVisibility(View.VISIBLE);
                        }
                        Log.e("Tracking Activity", "isOrdered" + isOrdered);
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