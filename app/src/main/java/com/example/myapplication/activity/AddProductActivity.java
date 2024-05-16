package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;
import com.example.myapplication.modelRequest.ProductRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends AppCompatActivity {
    private EditText urlImageEdt;
    private EditText titleTxt;
    private EditText priceTxt;
    private EditText categoryTxt;
    private Button addBtn;
    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_add_product);
        backBtn = findViewById(R.id.backBtn);
        urlImageEdt = findViewById(R.id.urlImageEdt);
        titleTxt = findViewById(R.id.titleTxt);
        priceTxt = findViewById(R.id.priceTxt);
        categoryTxt = findViewById(R.id.categoryTxt);
        addBtn = findViewById(R.id.addBtn);

        backBtn.setOnClickListener(v -> finish());
        addBtn.setOnClickListener(v -> addProduct());
    }
    private void addProduct() {
        String urlImage = urlImageEdt.getText().toString();
        String title = titleTxt.getText().toString();
        String price = priceTxt.getText().toString();
        String category = categoryTxt.getText().toString();
        if (urlImage.isEmpty() || title.isEmpty() || price.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        ProductRequest product = new ProductRequest();
        product.setImageUrl(urlImage);
        product.setName(title);
        product.setPrice(Double.parseDouble(price));
        product.setCategory(category);

        APIService.apiService.addProduct(product)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddProductActivity.this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                        Toast.makeText(AddProductActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}