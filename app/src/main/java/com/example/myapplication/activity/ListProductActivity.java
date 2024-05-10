package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.APIService;
import com.example.myapplication.adapter.ListProductAdapter;
import com.example.myapplication.model.Product;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProductActivity extends AppCompatActivity {
    private RecyclerView rcProducts;
    private List<Product> productList;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_product);

        rcProducts = findViewById(R.id.cafeListView);
        ImageView backBtn = findViewById(R.id.logoutBtn);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        rcProducts.setLayoutManager(linearLayoutManager);
        productList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar1);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SEARCH_VALUE")) {
            String searchValue = intent.getStringExtra("SEARCH_VALUE");
            callApiGetProductsWithSearchValue(searchValue);
        } else if (intent != null && intent.hasExtra("PRICE_VALUE")) {
            String priceValue = intent.getStringExtra("PRICE_VALUE");
            callApiGetProductsWithPriceValue(priceValue);
        } else if (intent != null && intent.hasExtra("EVALUATE_VALUE")) {
            String evaluateValue = intent.getStringExtra("EVALUATE_VALUE");
            callApiGetProductsWithStarValue(evaluateValue);
        } else {
            callApiGetProducts();
        }
        ListProductAdapter listProductAdapter = new ListProductAdapter(productList);
        listProductAdapter.setOnItemClickListener(this::openDetailActivity);
        backBtn.setOnClickListener(v -> backBtnOnClick());
    }

    private void backBtnOnClick() {
        Intent intent = new Intent(ListProductActivity.this, HomeActivity.class);
        startActivity(intent);
    }
    private void callApiGetProductsWithStarValue(String evaluateValue) {
        APIService.apiService.callApiGetProductsWithEvaluateValue(evaluateValue)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        progressBar.setVisibility(View.GONE);
                        productList = response.body();
                        ListProductAdapter listProductAdapter = new ListProductAdapter(productList);
                        listProductAdapter.setOnItemClickListener(product -> openDetailActivity(product));
                        rcProducts.setAdapter(listProductAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void callApiGetProductsWithPriceValue(String priceValue) {
        APIService.apiService.callApiGetProductsWithPriceValue(priceValue)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        progressBar.setVisibility(View.GONE);
                        productList = response.body();
                        ListProductAdapter listProductAdapter = new ListProductAdapter(productList);
                        listProductAdapter.setOnItemClickListener(product -> openDetailActivity(product));
                        rcProducts.setAdapter(listProductAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void callApiGetProductsWithSearchValue(String searchValue) {
        progressBar.setVisibility(View.VISIBLE);
        APIService.apiService.getListProductsWithSearchValue(searchValue)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        progressBar.setVisibility(View.GONE);
                        productList = response.body();
                        ListProductAdapter listProductAdapter = new ListProductAdapter(productList);
                        listProductAdapter.setOnItemClickListener(product -> openDetailActivity(product));
                        rcProducts.setAdapter(listProductAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });

    }
    private void callApiGetProducts() {
        APIService.apiService.getListProducts()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        productList = response.body();
                        ListProductAdapter listProductAdapter = new ListProductAdapter(productList);
                        listProductAdapter.setOnItemClickListener(product -> openDetailActivity(product));
                        rcProducts.setAdapter(listProductAdapter);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void openDetailActivity(Product product) {
        Intent intent = new Intent(ListProductActivity.this, DetailActivity.class);
        intent.putExtra("PRODUCT", product);
        startActivity(intent);
    }
}
