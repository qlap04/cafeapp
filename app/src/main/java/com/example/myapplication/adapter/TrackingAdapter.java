package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Cart;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.TrackingViewHolder> {
    //
    private final List<Cart> productsInCart;

    public TrackingAdapter(List<Cart> productsInCart) {
        this.productsInCart = productsInCart;
    }

    @NonNull
    @Override
    public TrackingAdapter.TrackingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tracking, parent, false);
        return new TrackingAdapter.TrackingViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TrackingAdapter.TrackingViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd - MM - yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);
        Cart cart = productsInCart.get(position);
        if (productsInCart.isEmpty()) {
            return;
        }
        Picasso.get().load(cart.getImage()).into(holder.image);
        ArrayList<String> options = cart.getOptions();
        if (options != null && !options.isEmpty()) {
            StringBuilder optionsString = new StringBuilder();
            for (int i = 0; i < options.size(); i++) {
                optionsString.append(options.get(i));
                if (i < options.size() - 1) {
                    optionsString.append(", ");
                }
            }
            holder.optionTxt.setText("Lựa chọn: " + optionsString.toString());
        } else {
            holder.optionTxt.setText("Lựa chọn: Không có");
        }


        holder.titleTxt.setText(cart.getTitle());
        holder.dateTxt.setText(String.format("Ngày giao: %s", formattedDate));
        holder.priceTxt.setText(String.format("Giá: %s", decimalFormat.format(cart.getPrice() * 1000)));
        holder.quantityTxt.setText("SL:" + cart.getQuantity());
    }

    @Override
    public int getItemCount() {
        if (productsInCart == null || productsInCart.isEmpty()) {
            return 0;
        }
        return productsInCart.size();
    }

    public static class TrackingViewHolder extends RecyclerView.ViewHolder {
        private final TextView priceTxt;
        private final TextView titleTxt;
        private final TextView quantityTxt;
        private final TextView dateTxt;
        private final TextView optionTxt;
        private final ImageView image;
        public TrackingViewHolder(@NonNull View itemView) {
            super(itemView);
            optionTxt = itemView.findViewById(R.id.optionTxt);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            dateTxt = itemView.findViewById(R.id.dateTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            quantityTxt = itemView.findViewById(R.id.quantityTxt);
            image = itemView.findViewById(R.id.image);
        }
    }
}
