package com.example.myapplication.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.PaymentMethodAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentMethodsActivity extends AppCompatActivity implements PaymentMethodAdapter.OnItemClickListener{

    private RecyclerView recyclerView;
    private PaymentMethodAdapter adapter;
    private List<PaymentMethod> paymentMethods;
    private String username;
    private String selectedPaymentMethod = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment_methods);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod(R.drawable.ic_cash, "Thanh toán tiền mặt", "Thanh toán khi nhận hàng"));
        paymentMethods.add(new PaymentMethod(R.drawable.ic_credit_card, "Credit or debit card", "Thẻ Visa hoặc Mastercard"));
        paymentMethods.add(new PaymentMethod(R.drawable.ic_bank_transfer, "Chuyển khoản ngân hàng", "Tự động xác nhận"));
        paymentMethods.add(new PaymentMethod(R.drawable.ic_zalopay, "ZaloPay", "Tự động xác nhận"));

        adapter = new PaymentMethodAdapter(paymentMethods, this);
        recyclerView.setAdapter(adapter);
        getUsernameFromSharedPreferences();
        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());

        Button paymentBtn = findViewById(R.id.paymentBtn);
        paymentBtn.setOnClickListener(v -> {
            if (selectedPaymentMethod != null) {
                setPaymentMethodInCart(selectedPaymentMethod);
                setProductInCartIsOdered();
                Intent intent = new Intent(PaymentMethodsActivity.this, OrderSuccessActivity.class);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(PaymentMethodsActivity.this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onItemClick(String title) {
        selectedPaymentMethod = title; // Lưu phương thức thanh toán được chọn
    }

    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }
    private void setProductInCartIsOdered() {
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
    private void setPaymentMethodInCart(String title) {
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.setPaymentMethodInCart(username, title)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e("PaymentMethodActivity", "Save payment method:  " + "Successfully");
                        } else {
                            Log.e("PaymentMethodActivity", "Save payment method:  " + "Failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
}