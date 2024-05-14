package com.example.myapplication.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.TrackingActivity;
import com.example.myapplication.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class ProcessingAdapter extends RecyclerView.Adapter<ProcessingAdapter.ProcessingViewHolder>{
    private final List<Cart> productList;

    public ProcessingAdapter(List<Cart> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProcessingAdapter.ProcessingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_processing, parent, false);
        return new ProcessingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProcessingAdapter.ProcessingViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        Cart product = productList.get(position);
        if (product == null) {
            return;
        }
        Picasso.get().load(product.getImage()).into(holder.image);
        holder.codeOrderTxt.setText(String.valueOf(product.getOrderId()));
        holder.nameTxt.setText(product.getTitle());
        holder.priceTxt.setText(String.format("Giá: %s", decimalFormat.format(product.getPrice() * 1000)));
        holder.quantityTxt.setText(String.format("SL:%s", String.valueOf(product.getQuantity())));
        holder.orderTrackingTxt.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), TrackingActivity.class);
            intent.putExtra("ID-PRODUCT", product.get_id());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class ProcessingViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private final TextView codeOrderTxt;
        private final TextView nameTxt;
        private final TextView priceTxt;
        private final TextView quantityTxt;
        private final TextView orderTrackingTxt;
        public ProcessingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            codeOrderTxt = itemView.findViewById(R.id.codeOrderTxt);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            orderTrackingTxt = itemView.findViewById(R.id.billTxt);
        }
    }
}
