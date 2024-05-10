package com.example.myapplication.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.myapplication.api.APIService;
import com.example.myapplication.activity.CartActivity;
import com.example.myapplication.activity.ListProductActivity;
import com.example.myapplication.activity.ListProductCategory;
import com.example.myapplication.activity.MainActivity;
import com.example.myapplication.adapter.CategoryAdapter;
import com.example.myapplication.adapter.ProductAdapter;
import com.example.myapplication.model.Cart;
import com.example.myapplication.model.Category;
import com.example.myapplication.model.Product;
import com.example.myapplication.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements CategoryAdapter.OnCategoryClickListener, ProductAdapter.OnAddToCartClickListener{
    private static final int SPEECH_REQUEST_CODE = 0;
    private EditText searchTxt;
    private ProgressBar prBestCafe, prBestCake;
    private RecyclerView rcCafe, rcCake;
    private List<Product> productCafeList, productCakeList;
    private ActivityResultLauncher<Intent> launcher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ImageSlider imageSlider = rootView.findViewById(R.id.imageSlider);
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel(R.drawable.image1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image3, ScaleTypes.FIT));
        imageSlider.setImageList(slideModels, ScaleTypes.FIT);
        rcCafe = rootView.findViewById(R.id.rcCafes);
        rcCake = rootView.findViewById(R.id.rcCakes);
        prBestCafe = rootView.findViewById(R.id.prBestCafe);
        prBestCake = rootView.findViewById(R.id.prBestCake);
        TextView viewAllTxt = rootView.findViewById(R.id.viewAllTxt);
        ImageView backBtn = rootView.findViewById(R.id.back);
        ImageView cartBtn = rootView.findViewById(R.id.cartBtn);
        ImageView searchBtn = rootView.findViewById(R.id.searchBtn);
        searchTxt = rootView.findViewById(R.id.searchTxt);
        ImageView micBtn = rootView.findViewById(R.id.micBtn);
        Spinner locateSp = rootView.findViewById(R.id.locateSp);
        Spinner evaluateSp = rootView.findViewById(R.id.timeSp);
        Spinner priceSp = rootView.findViewById(R.id.priceSp);
        TextView nameTxt = rootView.findViewById(R.id.nameTxt);

        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);

        rcCafe.setLayoutManager(linearLayoutManager1);
        rcCake.setLayoutManager(linearLayoutManager2);

        productCafeList = new ArrayList<>();
        productCakeList = new ArrayList<>();
        List<String> locations = new ArrayList<>();
        List<String> evaluates = new ArrayList<>();
        List<String> prices = new ArrayList<>();

        locations.add("Thủ Đức");
        locations.add("Bình Thạnh");

        evaluates.add("Đánh giá");
        evaluates.add("Đánh giá tăng dần");
        evaluates.add("Đánh giá giảm dần");

        prices.add("Giá");
        prices.add("Giá tăng dần");
        prices.add("Giá giảm dần");

        nameTxt.setText(getUsernameFromSharedPreferences());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_layout, locations);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_layout, evaluates);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(requireContext(), R.layout.spinner_item_layout, prices);



        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        locateSp.setAdapter(adapter);
        evaluateSp.setAdapter(adapter1);
        priceSp.setAdapter(adapter2);

        callApiGetListBestCafe();
        callApiGetListBestCake();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                ArrayList<String> resultList = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (resultList != null && !resultList.isEmpty()) {
                    String searchText = resultList.get(0);
                    searchTxt.setText(searchText);
                }
            }
        });

        viewAllTxt.setOnClickListener(v -> viewAllTxtOnClick());
        backBtn.setOnClickListener(v -> backBtnOnclick());
        cartBtn.setOnClickListener(v -> cartBtnOnClick());
        searchBtn.setOnClickListener(v -> searchBtnOnClick());
        micBtn.setOnClickListener(v -> startSpeechToText());

        evaluateSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedEvaluate = adapterView.getItemAtPosition(position).toString();
                filterProductsByStar(selectedEvaluate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        priceSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String selectedPrice = adapterView.getItemAtPosition(position).toString();
                filterProductsByPrice(selectedPrice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }
    private void filterProductsByPrice(String selectedPrice) {
        String priceValue;
        switch (selectedPrice) {
            case "Giá giảm dần":
                priceValue = "desc";
                break;
            case "Giá tăng dần":
                priceValue = "asc";
                break;
            default:
                return;
        }
        Intent intent = new Intent(requireContext(), ListProductActivity.class);
        intent.putExtra("PRICE_VALUE", priceValue);
        startActivity(intent);
    }
    private void filterProductsByStar(String selectedStarOption) {
        String starValue;
        switch (selectedStarOption) {
            case "Đánh giá giảm dần":
                starValue = "desc";
                break;
            case "Đánh giá tăng dần":
                starValue = "asc";
                break;
            default:
                return;
        }
        Intent intent = new Intent(requireContext(), ListProductActivity.class);
        intent.putExtra("EVALUATE_VALUE", starValue);
        startActivity(intent);
    }

    private void searchBtnOnClick() {
        String value = searchTxt.getText().toString();
        Intent intent = new Intent(requireContext(), ListProductActivity.class);
        intent.putExtra("SEARCH_VALUE", value);
        startActivity(intent);
    }

    private void cartBtnOnClick() {
        Intent intent = new Intent(requireContext(), CartActivity.class);
        startActivity(intent);
    }

    private void backBtnOnclick() {
        clearSavedUsername();
        Intent intent = new Intent(requireContext(), MainActivity.class);
        startActivity(intent);
    }

    private void viewAllTxtOnClick() {
        Intent intent = new Intent(requireContext(), ListProductActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(Category category) {
        Intent intent = new Intent(requireContext(), ListProductCategory.class);
        intent.putExtra("CATEGORY_TITLE", category.getTitle().toLowerCase());
        startActivity(intent);
    }
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...");
        launcher.launch(intent);
    }
    private void clearSavedUsername() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("username");
        editor.apply();
    }
    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String searchText = result.get(0);
                searchTxt.setText(searchText);
            }
        }
    }

    private void callApiGetListBestCafe() {
        APIService.apiService.getListBestCafe()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        prBestCafe.setVisibility(View.GONE);
                        productCafeList = response.body();
                        showListBestCafe(productCafeList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void callApiGetListBestCake() {
        APIService.apiService.getListBestCake()
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Product>> call, @NonNull Response<List<Product>> response) {
                        prBestCake.setVisibility(View.GONE);
                        productCakeList = response.body();
                        showListBestCake(productCakeList);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Product>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
    private void showListBestCafe(List<Product> products) {
        if (products != null) {
            ProductAdapter productAdapter = new ProductAdapter(products);
            productAdapter.setOnAddToCartClickListener(this);
            rcCafe.setAdapter(productAdapter);
        }
    }
    private void showListBestCake(List<Product> products) {
        if (products != null) {
            ProductAdapter productAdapter = new ProductAdapter(products);
            productAdapter.setOnAddToCartClickListener(this);
            rcCake.setAdapter(productAdapter);
        }
    }

    @Override
    public void onAddToCartClick(Product product) {
        String username = getUsernameFromSharedPreferences();
        Cart cart = new Cart(username, product, 1);
        ArrayList<String> selectedOptions = new ArrayList<>();
        APIService.apiService.addToCart(cart, selectedOptions)
                .enqueue(new Callback<Cart>() {
                    @Override
                    public void onResponse(@NonNull Call<Cart> call, @NonNull Response<Cart> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(), "Thêm sản phẩm thành công", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(requireContext(), "Thêm sản phẩm thấy bại", Toast.LENGTH_LONG).show();
                            try {
                                assert response.errorBody() != null;
                                Log.e("SignupActivity", "Lỗi khi thêm sản phẩm: " + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Cart> call, @NonNull Throwable t) {
                        Log.e("HomeActivity", "Lỗi khi gửi yêu cầu thêm sản phẩm: " + t.getMessage(), t);
                    }

                });
    }
}