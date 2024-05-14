package com.example.myapplication.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapter.AddressAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Address;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {
    private RecyclerView rcAddress;
    private List<Address> addressList;
    private ImageView backBtn;
    private Button nextBtn, addAddressBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_address);
        rcAddress = findViewById(R.id.rcAddress);
        backBtn = findViewById(R.id.backBtn);
        nextBtn = findViewById(R.id.nextBtn);
        addAddressBtn = findViewById(R.id.addAddressBtn);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);
        rcAddress.setLayoutManager(linearLayoutManager);
        callApiToGetAddress();
        backBtn.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(AddressActivity.this, CartActivity.class);
            startActivity(intent);
        });
        nextBtn.setOnClickListener(v -> {
            Address selectedAddress = null;
            for (Address address : addressList) {
                if (address.isSelected()) {
                    selectedAddress = address;
                    break;
                }
            }
            if (selectedAddress != null) {
                updateDataAddress(selectedAddress);
                finish();
                Intent intent = new Intent(AddressActivity.this, PaymentMethodsActivity.class);
                startActivity(intent);
            } else {
                if(addressList == null ) {
                    Toast.makeText(AddressActivity.this, "Vui lòng chọn địa chỉ", Toast.LENGTH_SHORT).show();
                } else {
                    updateDataAddress(addressList.get(0));
                }
            }
        });
        addAddressBtn.setOnClickListener(v -> {
            finish();
            Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
            startActivity(intent);
        });
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private void updateDataAddress(Address selectedAddress) {
        String username = getUsernameFromSharedPreferences();
        APIService.apiService.updateDataAddress(username, selectedAddress).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("Address Activity", "Call api to get update successfully");
                } else {
                    Log.e("Address Activity", "Call api to get update failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void callApiToGetAddress() {
        String username = getUsernameFromSharedPreferences();
        APIService.apiService.getListAddress(username).enqueue(new Callback<List<Address>>() {
            @Override
            public void onResponse(@NonNull Call<List<Address>> call, @NonNull Response<List<Address>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    addressList = response.body();
                    AddressAdapter addressAdapter = new AddressAdapter(addressList);
                    rcAddress.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
                    Log.e("Address Activity", "Call api to get address successfully");
                } else {
                    Log.e("Address Activity", "Call api to get address failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Address>> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
}