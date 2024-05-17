package com.example.myapplication.activity;

import static com.example.myapplication.utils.ToastUtils.showCustomToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Product;
import com.example.myapplication.modelRequest.ProductRequest;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProductActivity extends AppCompatActivity {
    private String titleProduct;
    private Product product;
    private ImageView image, backBtn;
    private EditText titleEdt, priceEdt, categoryEdt, urlImageEdt;
    private Button saveBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_product);

        backBtn = findViewById(R.id.backBtn);
        image = findViewById(R.id.image);
        titleEdt = findViewById(R.id.titleEdt);
        priceEdt = findViewById(R.id.priceEdt);
        categoryEdt = findViewById(R.id.categoryEdt);
        urlImageEdt = findViewById(R.id.urlImageEdt);
        saveBtn = findViewById(R.id.saveBtn);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("TITLE-PRODUCT")) {
            titleProduct = intent.getStringExtra("TITLE-PRODUCT");
            callApiGetInforProduct(titleProduct);
        }
        backBtn.setOnClickListener(v -> finish());
        saveBtn.setOnClickListener(v -> callapiUpdateInforProduct(titleProduct));
    }

    private void callapiUpdateInforProduct(String titleProduct) {
        String urlImage = urlImageEdt.getText().toString();
        String title = titleEdt.getText().toString();
        String price = priceEdt.getText().toString();
        String category = categoryEdt.getText().toString();
        if (urlImage.isEmpty() || title.isEmpty() || price.isEmpty() || category.isEmpty()) {
            showCustomToast(this, "Vui lòng điền đầy đủ thông tin");
            return;
        }
        ProductRequest product = new ProductRequest();
        product.setImageUrl(urlImage);
        product.setName(title);
        product.setPrice(Double.parseDouble(price));
        product.setCategory(category);

        APIService.apiService.updateInforProduct(titleProduct, product).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    showCustomToast(EditProductActivity.this, "Cập nhật thành công");
                } else {
                    showCustomToast(EditProductActivity.this, "Cập nhật thất bại");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);

            }
        });
    }

    private void callApiGetInforProduct(String titleProduct) {
        APIService.apiService.getInforProduct(titleProduct).enqueue(new Callback<Product>() {
            @Override
            public void onResponse(@NonNull Call<Product> call, @NonNull Response<Product> response) {
                if (response.isSuccessful()) {
                    product = response.body();
                    Picasso.get().load(product.getImage()).into(image);
                    titleEdt.setText(product.getTitle());
                    priceEdt.setText(String.valueOf(product.getPrice()));
                    categoryEdt.setText(product.getCategory());
                    urlImageEdt.setText(product.getImage());
                } else {
                    Log.e("EditProductActivity", "Get infor product failed ");

                }
            }

            @Override
            public void onFailure(@NonNull Call<Product> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);

            }
        });
    }

}