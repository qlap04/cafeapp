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

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final List<Product> productList;
    private OnAddToCartClickListener addToCartClickListener;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }
    public void setOnAddToCartClickListener(OnAddToCartClickListener listener) {
        this.addToCartClickListener = listener;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }
        Picasso.get().load(product.getImage()).into(holder.pic);
        holder.title.setText(product.getTitle());
        holder.price.setText(product.getPrice() + "00đ");
        holder.star.setText(product.getStar().toString());
        holder.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addToCartClickListener != null) {
                    addToCartClickListener.onAddToCartClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView pic;
        private final TextView title;
        private final TextView price;
        private final TextView star;
        private final TextView addToCartBtn;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            title = itemView.findViewById(R.id.titleTxt);
            price = itemView.findViewById(R.id.priceTxt);
            star = itemView.findViewById(R.id.starTxt);
            addToCartBtn = itemView.findViewById(R.id.addToCartBtn);
        }
    }
    public interface OnAddToCartClickListener {
        void onAddToCartClick(Product product);
    }
}
