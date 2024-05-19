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
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.TrackingAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Address;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.Order;
import com.example.myapplication.modelResponse.AddressResponse;
import com.example.myapplication.modelResponse.TotalPriceResponse;
import com.example.myapplication.utils.ToastUtils;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderSuccessActivity extends AppCompatActivity {
    private TextView priceTxt, discountTxt, totalPriceTxt, nameTxt, phoneNumTxt, addressTxt, paymentMethodTxt;
    private RecyclerView rcProduct;
    private List<Cart> productListInCart;
    private Address address;
    private String username;
    private String strName, strPhone, strAddress;
    private Button backBtn;
    private double total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_success);
        rcProduct = findViewById(R.id.rcProduct);
        priceTxt = findViewById(R.id.priceTxt);
        discountTxt = findViewById(R.id.discountTxt);
        totalPriceTxt = findViewById(R.id.totalPriceTxt);
        nameTxt = findViewById(R.id.nameTxt);
        phoneNumTxt = findViewById(R.id.phoneNumTxt);
        addressTxt = findViewById(R.id.addressTxt);
        paymentMethodTxt = findViewById(R.id.paymentMethodTxt);
        backBtn = findViewById(R.id.backBtn);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcProduct.setLayoutManager(linearLayoutManager);
        getUsernameFromSharedPreferences();
        callApiGetProductsInTraking();
        getTotalPrice();
        getPaymentMethodForBill(-1);
        callApiGetAddress();
        getInforForBill();
        backBtn.setOnClickListener(v -> {
            navigateToHome();
        });
    }
    private void navigateToHome() {
        finish();
        Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }

    private void getTotalPrice() {
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getTotalPriceForBill(username)
                .enqueue(new Callback<TotalPriceResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TotalPriceResponse> call, @NonNull Response<TotalPriceResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
                            TotalPriceResponse totalPriceResponse = response.body();
                            total = totalPriceResponse.getTotalPrice();
                            double totalPrice = totalPriceResponse.getTotalPrice() * 1000;
                            priceTxt.setText(decimalFormat.format(totalPrice));
                        } else {
                            ToastUtils.showCustomToast(OrderSuccessActivity.this, "Failed to get total price");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TotalPriceResponse> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }

    private void callApiGetProductsInTraking() {
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getProductInTracking(username, -1)
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        if (response.isSuccessful() && response.body() != null ) {
                            Log.e("OrderSuccessActivity", "Success" + "Success");
                            productListInCart = response.body();
                            TrackingAdapter trackingAdapter = new TrackingAdapter(productListInCart);
                            rcProduct.setAdapter(trackingAdapter);
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

    private void callApiGetAddress() {
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getAddress(username).enqueue(new Callback<Address>() {
            @Override
            public void onResponse(@NonNull Call<Address> call, @NonNull Response<Address> response) {
                if (response.isSuccessful() && response.body() != null) {
                    address = response.body();
                    nameTxt.setText(address.getName());
                    phoneNumTxt.setText(address.getPhone());
                    addressTxt.setText(address.getAddress());
                    strName = address.getName();
                    strPhone = address.getPhone();
                    strAddress = address.getAddress();
                    saveAddressForBill();
                } else {
                    ToastUtils.showCustomToast(OrderSuccessActivity.this, "Failed to get address");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Address> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void saveAddressForBill() {
        AddressResponse requestBody = new AddressResponse(strName, strPhone, strAddress);
        APIService.apiService.saveAddressForCart(username, -1, requestBody).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("Save address", "Save address for cart succesfully");
                } else {
                    Log.e("Save address", "Save address for cart succesfully");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
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

    private void getInforForBill() {
        APIService.apiService.getInforForBill(username).enqueue(new Callback<Order>() {
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