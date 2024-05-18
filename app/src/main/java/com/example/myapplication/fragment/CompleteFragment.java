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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapter.CompleteAdapter;
import com.example.myapplication.adapter.ProcessingAdapter;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Cart;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompleteFragment extends Fragment {
    private String username, role;
    private boolean isOnline;
    private RecyclerView rcComplete;
    private List<Cart> productListInCart;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_complete, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        rcComplete = rootView.findViewById(R.id.rcComplete);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(requireContext(), 1);
        rcComplete.setLayoutManager(linearLayoutManager);

        swipeRefreshLayout.setOnRefreshListener(this::loadProcessingData);


        loadProcessingData();

        return rootView;
    }
    private void loadProcessingData() {
        getRoleFromSharedPreferences();
        getUsernameFromSharedPreferences();
        getWorkingStatusUser(username);
    }
    private void getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = sharedPreferences.getString("username", "");
    }

    private void getRoleFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", MODE_PRIVATE);
        role = sharedPreferences.getString("userRole", "");
    }

    private void getWorkingStatusUser(String username) {
        APIService.apiService.getWorkingStatusUser(username).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isOnline = response.body();
                    if (isOnline) {
                        if (!role.equals("client")) {
                            callApiGetProductsInCartForStaff();
                        }
                    } else {
                        if (role.equals("client")) {
                            callApiGetProductsInCart();
                        } else if (role.equals("admin")) {
                            callApiGetProductsInCartForStaff();
                        }
                    }
                } else {
                    Log.e("ProcessingFragment", "Get working status user failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    private void callApiGetProductsInCart() {
        APIService.apiService.getProductInComplete(username)
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        productListInCart = response.body();
                        CompleteAdapter completeAdapter = new CompleteAdapter(productListInCart);
                        rcComplete.setAdapter(completeAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void callApiGetProductsInCartForStaff() {
        APIService.apiService.getProductInCompleteForStaff()
                .enqueue(new Callback<List<Cart>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Cart>> call, @NonNull Response<List<Cart>> response) {
                        productListInCart = response.body();
                        CompleteAdapter completeAdapter = new CompleteAdapter(productListInCart);
                        rcComplete.setAdapter(completeAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Cart>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
}
