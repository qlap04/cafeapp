package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.PaymentMethodAdapter;
import com.example.myapplication.model.PaymentMethod;

import java.util.ArrayList;
import java.util.List;

public class PaymentMethodsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentMethodAdapter adapter;
    private List<PaymentMethod> paymentMethods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment_methods);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        paymentMethods = new ArrayList<>();
        paymentMethods.add(new PaymentMethod(R.drawable.ic_cash, "Thanh toán tiền mặt", "Thanh toán khi nhận hàng"));
        paymentMethods.add(new PaymentMethod(R.drawable.ic_credit_card, "Credit or debit card", "Thẻ Visa hoặc Mastercard"));
        paymentMethods.add(new PaymentMethod(R.drawable.ic_bank_transfer, "Chuyển khoản ngân hàng", "Tự động xác nhận"));
        paymentMethods.add(new PaymentMethod(R.drawable.ic_zalopay, "ZaloPay", "Tự động xác nhận"));

        adapter = new PaymentMethodAdapter(paymentMethods);
        recyclerView.setAdapter(adapter);

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }
}