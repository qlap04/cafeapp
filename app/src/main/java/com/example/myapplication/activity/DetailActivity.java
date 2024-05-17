package com.example.myapplication.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.Product;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private RatingBar ratingBar;
    private Button addBtn;
    private ImageView imageView, back1Btn;
    private TextView titleTxt, priceTxt, starTxt, totalTxt, numTxt, minusBtn, plusBtn;
    private Product product;
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        ArrayList<String> selectedOptions = new ArrayList<>();
        imageView = findViewById(R.id.imageView5);
        titleTxt = findViewById(R.id.title3Txt);
        ratingBar = findViewById(R.id.ratingBar);
        priceTxt = findViewById(R.id.price2Txt);
        starTxt = findViewById(R.id.rate1Txt);
        totalTxt = findViewById(R.id.totalTxt);
        back1Btn = findViewById(R.id.back1Btn);
        addBtn = findViewById(R.id.addBtn);
        numTxt = findViewById(R.id.numTxt);
        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("PRODUCT")) {
            product = (Product) intent.getSerializableExtra("PRODUCT");
            titleTxt.setText(product.getTitle());
            ratingBar.setRating(product.getStar().floatValue());
            priceTxt.setText(product.getPrice() + "00đ");
            totalTxt.setText(product.getPrice() + "00đ");
            starTxt.setText(product.getStar() + " đánh giá");
            Picasso.get().load(product.getImage()).into(imageView);
            if (product.getCategory().equals("cake")) {
                LinearLayout linearLayout = findViewById(R.id.optionLayout);
                linearLayout.setVisibility(View.GONE);
            }
        }
        back1Btn.setOnClickListener(v -> back1BtnOnClick());
        minusBtn.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updateQuantity();
            }
        });
        plusBtn.setOnClickListener(v -> {
            quantity++;
            updateQuantity();
        });
        addBtn.setOnClickListener(v -> {
            String drinkOption = getSelectedRadioButtonValue(findViewById(R.id.radiogroup_drink));
            String sizeOption = getSelectedRadioButtonValue(findViewById(R.id.radiogroup_size));
            String sugarOption = getSelectedRadioButtonValue(findViewById(R.id.radiogroup_sugar));
            String iceOption = getSelectedRadioButtonValue(findViewById(R.id.radiogroup_ice));

            selectedOptions.clear();

            if (drinkOption != null) {
                selectedOptions.add("Đồ uống " + drinkOption.toLowerCase());
            }
            if (sizeOption != null) {
                selectedOptions.add("Kích thước " + sizeOption.toLowerCase());
            }
            if (sugarOption != null) {
                selectedOptions.add("Đường " + sugarOption.toLowerCase());
            }
            if (iceOption != null) {
                selectedOptions.add("Đá " + iceOption.toLowerCase());
            }
            addBtnOnClick(product, selectedOptions);
        });
    }

    private void updateQuantity() {
        double total = quantity * product.getPrice();
        numTxt.setText(String.valueOf(quantity));
        totalTxt.setText(String.format("%s00đ", String.valueOf(total)));
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private String getSelectedRadioButtonValue(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
        return null;
    }
    private void addBtnOnClick(Product product, ArrayList<String> selectedOptions) {
        String username = getUsernameFromSharedPreferences();
        int quantity = Integer.parseInt(numTxt.getText().toString());
        Cart cart = new Cart(username, product, quantity);
        APIService.apiService.addToCart(cart, selectedOptions)
                .enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(DetailActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(DetailActivity.this, "Thêm sản phẩm thấy bại", Toast.LENGTH_LONG).show();
                            try {
                                Log.e("DetailActivity", "Lỗi khi thêm sản phẩm: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                        Log.e("DetailActivity", "Lỗi khi gửi yêu cầu thêm sản phẩm: " + t.getMessage(), t);
                    }
                });
    }


    private void back1BtnOnClick() {
        Intent intent = new Intent(DetailActivity.this, ListProductActivity.class);
        startActivity(intent);
    }
}
