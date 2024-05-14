package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Product;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class Product1Adapter extends RecyclerView.Adapter<Product1Adapter.Product1ViewHolder> {
    private final List<Product> productList;
    public Product1Adapter(List<Product> productList) {
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
        holder.popularProductTxt.setText(product.getPopular());

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
        private TextView popularProductTxt;
        public Product1ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            nameProductTxt = itemView.findViewById(R.id.nameProductTxt);
            priceProductTxt = itemView.findViewById(R.id.priceProductTxt);
            popularProductTxt = itemView.findViewById(R.id.popularProductTxt);
        }
    }
}
