package com.example.myapplication.activity;

import static com.example.myapplication.utils.ToastUtils.showCustomToast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EvaluateActivity extends AppCompatActivity {
    //
    private ImageView backBtn;
    private RatingBar ratingBar;
    private EditText contentEdt;
    private Button evaluateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_evaluate);
        backBtn = findViewById(R.id.backBtn);
        ratingBar = findViewById(R.id.ratingBar);
        contentEdt = findViewById(R.id.contentEdt);
        evaluateBtn = findViewById(R.id.evaluateBtn);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ID-PRODUCT")) {
            int idValue = intent.getIntExtra("ID-PRODUCT", -1);
            evaluateBtn.setOnClickListener(v -> {
                float rating = ratingBar.getRating();
                String content = contentEdt.getText().toString();
                evaluateProduct(idValue, rating, content);
            });
        }
        backBtn.setOnClickListener(v -> finish());
    }

    private void evaluateProduct(int idValue, float rating, String content) {
        APIService.apiService.evaluateProduct(idValue, rating, content).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    showCustomToast(EvaluateActivity.this, "Đánh giá thành công");
                } else {
                    finish();
                    Log.e("Evaluate Activity", "Evaluate order" + "Evaluate order failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
}