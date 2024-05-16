package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ProductAdapter1;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListProduct1Activity extends AppCompatActivity {
    private RecyclerView rcProducts;
    private List<Product> productList;
    private ProgressBar progressBar1;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_product1);

        backBtn = findViewById(R.id.backBtn);
        rcProducts = findViewById(R.id.rcProducts);
        progressBar1 = findViewById(R.id.progressBar1);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcProducts.setLayoutManager(linearLayoutManager);

        callApiGetProducts();

        backBtn.setOnClickListener(v -> finish());
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

                            // Khởi tạo ItemTouchHelper và gắn vào RecyclerView
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeActivity(product1Adapter));
                            itemTouchHelper.attachToRecyclerView(rcProducts);

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