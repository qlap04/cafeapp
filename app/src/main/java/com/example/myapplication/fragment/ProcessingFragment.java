package com.example.myapplication.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.ProcessingAdapter;
import com.example.myapplication.adapter.TrackingAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingFragment extends Fragment {
    private RecyclerView rcProcessing;
    private List<Cart> productListInCart;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_processing, container, false);
        rcProcessing = rootView.findViewById(R.id.rcProcessing);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(requireContext(), 1);
        rcProcessing.setLayoutManager(linearLayoutManager);
        String role = getRoleFromSharedPreferences();
        if (!role.equals("client")) {
            callApiGetProductsInCartForStaff();
        } else {
            callApiGetProductsInCart();
        }
        return rootView;
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }
    private String getRoleFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("userRole", "");
    }
    private void callApiGetProductsInCart() {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getProductInProcessing(username)
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        productListInCart = response.body();
                        ProcessingAdapter processingAdapter = new ProcessingAdapter(productListInCart);
                        rcProcessing.setAdapter(processingAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void callApiGetProductsInCartForStaff() {
        APIService.apiService.getProductInProcessingForStaff()
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        productListInCart = response.body();
                        ProcessingAdapter processingAdapter = new ProcessingAdapter(productListInCart);
                        rcProcessing.setAdapter(processingAdapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
}
