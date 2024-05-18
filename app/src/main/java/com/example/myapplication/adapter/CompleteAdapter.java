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
import com.example.myapplication.activity.BillActivity;
import com.example.myapplication.activity.OrderSuccessActivity;
import com.example.myapplication.activity.TrackingActivity;
import com.example.myapplication.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.ProcessingViewHolder>{
    private final List<Cart> productList;

    public CompleteAdapter(List<Cart> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public CompleteAdapter.ProcessingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complete, parent, false);
        return new ProcessingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompleteAdapter.ProcessingViewHolder holder, int position) {
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
        holder.billTxt.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), BillActivity.class);
            intent.putExtra("ID-PRODUCT", product.get_id());
            v.getContext().startActivity(intent);
        });
        holder.starTxt.setText(String.valueOf(product.getStarEvaluate()));
        holder.evaluateTxt.setText(product.getContentEvaluate());
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
        private final TextView billTxt;
        private TextView starTxt;
        private TextView evaluateTxt;
        public ProcessingViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            codeOrderTxt = itemView.findViewById(R.id.codeOrderTxt);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            billTxt = itemView.findViewById(R.id.billTxt);
            starTxt = itemView.findViewById(R.id.starTxt);
            evaluateTxt = itemView.findViewById(R.id.evaluateTxt);
        }
    }
}
