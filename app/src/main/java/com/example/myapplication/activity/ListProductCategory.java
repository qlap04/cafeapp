package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class ListProductCategory extends AppCompatActivity {
    //
    private ImageView backBtn;
    private TextView title1Txt;
    private RecyclerView rcProducts;
    private List<Product> productList;
    private ProgressBar progressBar;
    private String categoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_list_product_category);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("CATEGORY_TITLE")) {
            categoryTitle = intent.getStringExtra("CATEGORY_TITLE");
        } else {
            finish();
        }

        rcProducts = findViewById(R.id.cafeListView);
        backBtn = findViewById(R.id.logoutBtn);
        title1Txt = findViewById(R.id.title1Txt);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 2);
        rcProducts.setLayoutManager(linearLayoutManager);
        productList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar1);
        title1Txt.setText(capitalizeFirstLetterOfEachWord(categoryTitle));
        callApiGetProductsByCategory();

        ListProductAdapter listProductAdapter = new ListProductAdapter(productList);
        listProductAdapter.setOnItemClickListener(this::openDetailActivity);
        backBtn.setOnClickListener(v -> backBtnOnClick());
    }

    private void backBtnOnClick() {
        Intent intent = new Intent(ListProductCategory.this, HomeActivity.class);
        startActivity(intent);
    }

    private void callApiGetProductsByCategory() {
        APIService.apiService.getProductsByCategory(categoryTitle)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        if (response.isSuccessful()) {
                            productList = response.body();
                            showProducts(productList);
                        } else {
                            Log.e("API Error", "Response unsuccessful: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }

    private void showProducts(List<Product> products) {
        progressBar.setVisibility(View.GONE);
        ListProductAdapter listProductAdapter = new ListProductAdapter(products);
        listProductAdapter.setOnItemClickListener(this::openDetailActivity);
        rcProducts.setAdapter(listProductAdapter);
    }

    private void openDetailActivity(Product product) {
        Intent intent = new Intent(ListProductCategory.this, DetailActivity.class);
        intent.putExtra("PRODUCT", product);
        startActivity(intent);
    }
    public static String capitalizeFirstLetterOfEachWord(String input) {
        StringBuilder output = new StringBuilder();
        String[] words = input.toLowerCase().split(" ");
        for (String word : words) {
            String capitalizedWord = word.substring(0, 1).toUpperCase() + word.substring(1);
            output.append(capitalizedWord).append(" ");
        }
        return output.toString().trim();
    }
}
