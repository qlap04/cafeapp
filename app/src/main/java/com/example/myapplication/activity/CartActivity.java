package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.CartAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Cart;
import com.example.myapplication.R;
import com.example.myapplication.modelResponse.TotalPriceResponse;
import com.example.myapplication.token.TokenManager;
import com.example.myapplication.token.TokenValidator;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnQuantityChangeListener, CartAdapter.OnDeleteListener {
    private TextView totalTxt, subTotalTxt;
    private Button orderBtn;
    private RecyclerView rcProductsInCart;
    private List<Cart> productListInCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        totalTxt = findViewById(R.id.totalTxt);
        subTotalTxt = findViewById(R.id.subTotalTxt);
        rcProductsInCart = findViewById(R.id.cardView);
        orderBtn = findViewById(R.id.orderBtn);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rcProductsInCart.setLayoutManager(linearLayoutManager);
        productListInCart = new ArrayList<>();

        callApiGetProductsInCart();
        getTotalPrice();
        ImageView backBtn = findViewById(R.id.logoutBtn);
        backBtn.setOnClickListener(v -> backBtnOnClick());
        orderBtn.setOnClickListener(v -> {
            if (productListInCart.isEmpty()) {
                Toast.makeText(CartActivity.this, "Vui lòng thêm sản phẩm trước khi đặt hàng", Toast.LENGTH_SHORT).show();
            } else {
                finish();
                setOrderIdForProductInCart();
                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });

    }

    private void backBtnOnClick() {
        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private void callApiGetProductsInCart() {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getListProductsIncart(username)
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        productListInCart = response.body();
                        CartAdapter cartAdapter = new CartAdapter(productListInCart);
                        rcProductsInCart.setAdapter(cartAdapter);
                        cartAdapter.setOnQuantityChangeListener(CartActivity.this);
                        cartAdapter.setOnDeleteListener(CartActivity.this);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void getTotalPrice() {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getTotalPrice(username)
                .enqueue(new Callback<TotalPriceResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TotalPriceResponse> call, @NonNull Response<TotalPriceResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            DecimalFormat decimalFormat = new DecimalFormat("#,###đ");
                            TotalPriceResponse totalPriceResponse = response.body();
                            double totalPrice = totalPriceResponse.getTotalPrice() * 1000;
                            totalTxt.setText(decimalFormat.format(totalPrice));
                            subTotalTxt.setText(decimalFormat.format(totalPrice));
                        } else {
                            Toast.makeText(CartActivity.this, "Failed to get total price", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TotalPriceResponse> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    @Override
    public void onQuantityChange(int position, int newQuantity) {
        updateCartItem(position, newQuantity);
    }
    @Override
    public void onDelete(int position, Cart cart) {
        deleteItemFromDatabase(cart.get_id());
    }
    private void updateCartItem(int position, int newQuantity) {
        Cart cart = productListInCart.get(position);
        int cartId = cart.get_id();
        cart.setQuantity(newQuantity);
        String username = getUsernameFromSharedPreferences();
        if (!username.isEmpty()) {
            APIService.apiService.updateCartItem(username, cartId, cart)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                            if (response.isSuccessful()) {
                                productListInCart.set(position, cart);
                                rcProductsInCart.getAdapter().notifyItemChanged(position);
                                getTotalPrice();
                            } else {
                                Log.e("API Error", "Update cart item failed: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                            Log.e("API Error", "Call API error: " + t.getMessage(), t);
                        }
                    });
        }
    }

    private void deleteItemFromDatabase(int id) {
        String username = getUsernameFromSharedPreferences();
        APIService.apiService.deleteProduct(username, id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            callApiGetProductsInCart();
                            getTotalPrice();
                            Log.d("CartAdapter", "Item deleted successfully");
                        } else {
                            Log.e("CartAdapter", "Failed to delete item. Error: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("CartAdapter", "Failed to delete item. Error: " + t.getMessage());
                    }
                });
    }
    private void setOrderIdForProductInCart() {
        String username = getUsernameFromSharedPreferences();
        APIService.apiService.setOrderIdProduct(username).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("CartActivity", "Set orderId product successfully");
                } else {
                    Log.d("CartActivity", "Set orderId product failed");

                }
            }
            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("Call api error", "Error: " + t.getMessage());
            }
        });
    }
}