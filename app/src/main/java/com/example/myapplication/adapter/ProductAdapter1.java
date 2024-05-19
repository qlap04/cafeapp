package com.example.myapplication.adapter;

import static com.example.myapplication.utils.ToastUtils.showCustomToast;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.EditProductActivity;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter1 extends RecyclerView.Adapter<ProductAdapter1.Product1ViewHolder> implements Filterable {
    private List<Product> productList;
    private List<Product> productListFull;
    private Context context;

    public ProductAdapter1(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.productListFull = new ArrayList<>(productList);
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
            Intent intent = new Intent(v.getContext(), EditProductActivity.class);
            intent.putExtra("TITLE-PRODUCT", product.getTitle());
            v.getContext().startActivity(intent);
        });

        holder.btnDelete.setOnClickListener(v -> {
            deleteProduct(product.getTitle());
        });
        showButtons(holder);
        hideButtons(holder);
    }

    @Override
    public int getItemCount() {
        return productList != null ? productList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return productFilter;
    }

    private Filter productFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Product> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(productListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Product product : productListFull) {
                    if (product.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(product);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            productList.clear();
            productList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class Product1ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView nameProductTxt;
        private TextView priceProductTxt;
        private Switch switchBest;
        public LinearLayout item_view, swipeButtons;
        public Button btnEdit, btnDelete;

        public Product1ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nameProductTxt = itemView.findViewById(R.id.nameProductTxt);
            priceProductTxt = itemView.findViewById(R.id.priceProductTxt);
            switchBest = itemView.findViewById(R.id.switchBest);
            item_view = itemView.findViewById(R.id.item_view);
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
    private void deleteProduct(String productName) {
        APIService.apiService.deleteProduct(productName).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    showCustomToast(context, "Xóa sản phẩm thành công");
                } else {
                    showCustomToast(context, "Xóa sản phẩm thất bại");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.e("API Error", "Call API error: " + t.getMessage(), t);
            }
        });
    }
    //huhu
    private void updatePopularStatus(String productName, String popularStatus) {
        APIService.apiService.updateProductPopularStatus(productName, popularStatus)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Log.e("ProductAdapter1", "Update status product successfully");
                        } else {
                            Log.e("ProductAdapter1", "Update status product failed");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }
}
