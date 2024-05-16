package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ProductAdapter1;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProduct1Activity extends AppCompatActivity {
    private RecyclerView rcProducts;
    private List<Product> productList;
    private ProgressBar progressBar1;
    private Button addProduct;
    private ImageView backBtn;
    private ImageView searchIcon;
    private TextView myWarehouseTxt;
    private SearchView searchView;
    private ProductAdapter1 productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_product1);

        backBtn = findViewById(R.id.backBtn);
        searchIcon = findViewById(R.id.searchIcon);
        myWarehouseTxt = findViewById(R.id.myWarehouseTxt);
        searchView = findViewById(R.id.searchView);
        addProduct = findViewById(R.id.addProduct);
        rcProducts = findViewById(R.id.rcProducts);
        progressBar1 = findViewById(R.id.progressBar1);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcProducts.setLayoutManager(linearLayoutManager);

        callApiGetProducts();

        backBtn.setOnClickListener(v -> finish());
        addProduct.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(ListProduct1Activity.this, AddProductActivity.class);
            startActivity(intent);
        });
        searchIcon.setOnClickListener(v -> {
            if (searchView.getVisibility() == View.GONE) {
                searchView.setVisibility(View.VISIBLE);
                myWarehouseTxt.setVisibility(View.GONE);
                searchIcon.setVisibility(View.GONE);
            } else {
                searchView.setVisibility(View.GONE);
                myWarehouseTxt.setVisibility(View.VISIBLE);
                searchIcon.setVisibility(View.VISIBLE);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterProducts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return false;
            }

            private void filterProducts(String query) {
                List<Product> filteredList = new ArrayList<>();
                for (Product product : productList) {
                    if (product.getTitle().toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(product);
                    }
                }
                productAdapter.updateData(filteredList);
            }
        });
    }

    private void callApiGetProducts() {
        APIService.apiService.getListProducts()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            productList = response.body();
                            productAdapter = new ProductAdapter1(productList);
                            rcProducts.setAdapter(productAdapter);
                            progressBar1.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
}
