package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.DiscountAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Discount;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscountActivity extends AppCompatActivity implements DiscountAdapter.OnItemClickListener{
    //
    private RecyclerView rcDiscount;
    private List<Discount> discountList;
    private String username;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_discount);
        backBtn = findViewById(R.id.backBtn);
        rcDiscount = findViewById(R.id.rcDiscount);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcDiscount.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcDiscount.addItemDecoration(itemDecoration);
        getUsernameFromSharedPreferences();
        callApiGetAllDiscounts();

        backBtn.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(DiscountActivity.this, AddressActivity.class);
            startActivity(intent);
        });
    }
    @Override
    public void onItemClick(int value) {
        setValueDiscount(value);
        finish();
        Intent intent = new Intent(DiscountActivity.this, PaymentMethodsActivity.class);
        startActivity(intent);
    }
    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }
    private void setValueDiscount(int value) {
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.setValueDiscount(username, value).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("Discount Activity", "Save value discount:  " + "Successfully");
                } else {
                    Log.e("Discount Activity", "Save value discount:  " + "Falied");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }

    private void callApiGetAllDiscounts() {
        APIService.apiService.getAllDiscoutns().enqueue(new Callback<List<Discount>>() {
            @Override
            public void onResponse(@NonNull Call<List<Discount>> call, @NonNull Response<List<Discount>> response) {
                if (response.isSuccessful()) {
                    discountList = response.body();
                    DiscountAdapter discountAdapter = new DiscountAdapter(DiscountActivity.this, discountList, DiscountActivity.this);
                    rcDiscount.setAdapter(discountAdapter);
                } else {
                    Log.e("Discount Activity", "Call api get list discounts failed ");

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Discount>> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
}