package com.example.myapplication.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Product;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter1 extends RecyclerView.Adapter<ProductAdapter1.Product1ViewHolder> {
    private final List<Product> productList;
    private Context context;

    public ProductAdapter1(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public Product1ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_1, parent, false);
        return new Product1ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Product1ViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        Product product = productList.get(position);
        if (product == null) {
            return;
        }
        Picasso.get().load(product.getImage()).into(holder.image);
        holder.nameProductTxt.setText(product.getTitle());
        holder.priceProductTxt.setText(decimalFormat.format(product.getPrice() * 1000));

        holder.switchBest.setOnCheckedChangeListener(null);

        holder.switchBest.setChecked("best".equals(product.getPopular()));

        holder.switchBest.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String popularStatus = isChecked ? "best" : "";
            updatePopularStatus(product.getTitle(), popularStatus);
        });
        holder.btnEdit.setOnClickListener(v -> {
            Toast.makeText(context, "click edit", Toast.LENGTH_SHORT).show();
        });

        holder.btnDelete.setOnClickListener(v -> {
            Toast.makeText(context, "click delete", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class Product1ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView nameProductTxt;
        private TextView priceProductTxt;
        private Switch switchBest;
        public LinearLayout swipeButtons;
        public Button btnEdit, btnDelete;

        public Product1ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nameProductTxt = itemView.findViewById(R.id.nameProductTxt);
            priceProductTxt = itemView.findViewById(R.id.priceProductTxt);
            switchBest = itemView.findViewById(R.id.switchBest);
            swipeButtons = itemView.findViewById(R.id.swipe_buttons);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public void showButtons(Product1ViewHolder viewHolder) {
        viewHolder.swipeButtons.setVisibility(View.VISIBLE);
    }

    public void hideButtons(Product1ViewHolder viewHolder) {
        viewHolder.swipeButtons.setVisibility(View.GONE);
    }

    private void updatePopularStatus(String productTitle, String popularStatus) {
        APIService.apiService.updateProductPopularStatus(productTitle, popularStatus).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("ProductAdapter1", "Set popular product successfully");
                } else {
                    Log.e("ProductAdapter1", "Set popular product failed: " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }

}
