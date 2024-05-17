package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;

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
    private ImageView backBtn, searchIcon;
    private SearchView searchView;
    private Button addBtn;

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
        searchView = findViewById(R.id.searchView);
        addBtn = findViewById(R.id.addBtn);
        rcProducts = findViewById(R.id.rcProducts);
        progressBar1 = findViewById(R.id.progressBar1);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcProducts.setLayoutManager(linearLayoutManager);

        callApiGetProducts();

        searchIcon.setOnClickListener(v -> {
            searchIcon.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
        });

        addBtn.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(ListProduct1Activity.this, AddProductActivity.class);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> finish());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
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
                            ProductAdapter1 product1Adapter = new ProductAdapter1(productList);
                            rcProducts.setAdapter(product1Adapter);
                            progressBar1.setVisibility(View.GONE);
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeActivity(product1Adapter));
                            itemTouchHelper.attachToRecyclerView(rcProducts);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }

    private void search(String keyword) {
        List<Product> searchedList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                searchedList.add(product);
            }
        }
        ProductAdapter1 product1Adapter = new ProductAdapter1(searchedList);
        rcProducts.setAdapter(product1Adapter);
    }
}
