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
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TrackingAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Address;
import com.example.myapplication.model.Cart;
import com.example.myapplication.modelResponse.AddressResponse;
import com.example.myapplication.modelResponse.TotalPriceResponse;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {
    private TextView priceTxt, nameTxt, phoneNumTxt, addressTxt;
    private RecyclerView rcProduct;
    private List<Cart> productListInCart;
    private AddressResponse address;
    private ImageView backBtn;
    private Button evaluateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bill); setContentView(R.layout.activity_bill);
        rcProduct = findViewById(R.id.rcProduct);
        priceTxt = findViewById(R.id.priceTxt);
        nameTxt = findViewById(R.id.nameTxt);
        phoneNumTxt = findViewById(R.id.phoneNumTxt);
        addressTxt = findViewById(R.id.addressTxt);
        backBtn = findViewById(R.id.backBtn);
        evaluateBtn = findViewById(R.id.evaluateBtn);
        productListInCart = new ArrayList<>();
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcProduct.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID-PRODUCT")) {
            int idValue = intent.getIntExtra("ID-PRODUCT", 1);
            callApiGetProductForBill(idValue);
            callApitGetAddress(idValue);
            getPriceForBill(idValue);
        }
        backBtn.setOnClickListener(v -> {
            navigateToHome();
        });
        evaluateBtn.setOnClickListener(v -> navigateToEvaluate());
    }
    private void navigateToHome() {
        finish();
        Intent intent = new Intent(BillActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    private void navigateToEvaluate() {
        finish();
        Intent intent = new Intent(BillActivity.this, EvaluateActivity.class);
        startActivity(intent);
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private void callApiGetProductForBill(int id) {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getProductForBill(username, id).enqueue(new Callback<List<Cart>>() {
            @Override
            public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productListInCart = response.body();
                    TrackingAdapter trackingAdapter = new TrackingAdapter(productListInCart);
                    rcProduct.setAdapter(trackingAdapter);
                    Log.e("Success", "Success" + "Success");
                } else {
                    Log.e("Failed", "Failed" + "Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void getPriceForBill(int id) {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getPriceForBill(username, id).enqueue(new Callback<TotalPriceResponse>() {
            @Override
            public void onResponse(@NonNull Call<TotalPriceResponse> call, @NonNull Response<TotalPriceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
                    TotalPriceResponse totalPriceResponse = response.body();
                    double totalPrice = totalPriceResponse.getTotalPrice() * 1000;
                    priceTxt.setText(decimalFormat.format(totalPrice));
                    Log.e("Success", "Success" + "Success");
                } else {
                    Log.e("Failed", "Failed" + "Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<TotalPriceResponse> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void callApitGetAddress(int id) {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getAddressForBill(username, id).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddressResponse> call, @NonNull Response<AddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    address = response.body();
                    nameTxt.setText(address.getName());
                    phoneNumTxt.setText(address.getPhone());
                    addressTxt.setText(address.getAddress());
                } else {
                    Toast.makeText(BillActivity.this, "Failed to get address", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressResponse> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
}