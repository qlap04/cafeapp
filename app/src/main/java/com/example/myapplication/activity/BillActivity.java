package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TrackingAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.Order;
import com.example.myapplication.modelResponse.AddressResponse;
import com.example.myapplication.modelResponse.TotalPriceResponse;
import com.example.myapplication.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillActivity extends AppCompatActivity {
    private TextView priceTxt, discountTxt, totalPriceTxt, nameTxt, phoneNumTxt, addressTxt, paymentMethodTxt;
    private RecyclerView rcProduct;
    private List<Cart> productListInCart;
    private AddressResponse address;
    private ImageView backBtn;
    private String username;
    private double total;

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
        discountTxt = findViewById(R.id.discountTxt);
        totalPriceTxt = findViewById(R.id.totalPriceTxt);
        nameTxt = findViewById(R.id.nameTxt);
        phoneNumTxt = findViewById(R.id.phoneNumTxt);
        addressTxt = findViewById(R.id.addressTxt);
        paymentMethodTxt = findViewById(R.id.paymentMethodTxt);
        backBtn = findViewById(R.id.backBtn);
        productListInCart = new ArrayList<>();
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcProduct.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID-PRODUCT")) {
            int idValue = intent.getIntExtra("ID-PRODUCT", 1);
            callApiGetProductForBill(idValue);
            getPriceForBill(idValue);
            getPaymentMethodForBill(idValue);
            callApitGetAddress(idValue);
            getInforForBill(idValue);
        }
        getUsernameFromSharedPreferences();
        backBtn.setOnClickListener(v -> {
            navigateToHome();
        });
    }
    private void navigateToHome() {
        finish();
    }
    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }
    private void callApiGetProductForBill(int id) {
        APIService.apiService.getProductForBill(id).enqueue(new Callback<List<Cart>>() {
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
        APIService.apiService.getPriceForBill(id).enqueue(new Callback<TotalPriceResponse>() {
            @Override
            public void onResponse(@NonNull Call<TotalPriceResponse> call, @NonNull Response<TotalPriceResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
                    TotalPriceResponse totalPriceResponse = response.body();
                    total = totalPriceResponse.getTotalPrice();
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
        APIService.apiService.getAddressForBill(id).enqueue(new Callback<AddressResponse>() {
            @Override
            public void onResponse(@NonNull Call<AddressResponse> call, @NonNull Response<AddressResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    address = response.body();
                    nameTxt.setText(address.getName());
                    phoneNumTxt.setText(address.getPhone());
                    addressTxt.setText(address.getAddress());
                } else {
                    ToastUtils.showCustomToast(BillActivity.this, "Lấy địa chỉ thất bại");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AddressResponse> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void getPaymentMethodForBill(int id) {
        APIService.apiService.getPaymentMethodForBill(username, id).enqueue(new Callback<String>() {
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if (response.isSuccessful() && response.body() != null) {
                    paymentMethodTxt.setText(response.body());
                } else {
                    Log.e("BillActivity", "Get payment methoid from api: Failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void getInforForBill(int id) {
        APIService.apiService.getInforForBill1(id).enqueue(new Callback<Order>() {
            @Override
            public void onResponse(@NonNull Call<Order> call, @NonNull Response<Order> response) {
                if (response.isSuccessful()) {
                    DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
                    Order order = response.body();
                    assert order != null;
                    double discountPrice = (((double) order.getValueDiscount() * total) / 100.0) * 1000;
                    String formattedDiscountPrice = decimalFormat.format(discountPrice);
                    discountTxt.setText(String.format("-%s", formattedDiscountPrice));
                    double totalPrice = (total*1000 - discountPrice);
                    String formattedTotalPrice = decimalFormat.format(totalPrice);
                    totalPriceTxt.setText(String.format("%s", formattedTotalPrice));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Order> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
}