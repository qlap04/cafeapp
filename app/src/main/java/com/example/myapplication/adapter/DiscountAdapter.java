package com.example.myapplication.adapter;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.api.APIService;
import com.example.myapplication.model.Discount;
import com.example.myapplication.modelResponse.TotalPriceResponse;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscountAdapter extends RecyclerView.Adapter<DiscountAdapter.DiscountViewHolder>{
    //
    private final List<Discount> discountList;
    private final DiscountAdapter.OnItemClickListener listener;
    private final Context mContext;
    private double total = -1;

    private int selectedPosition = -1;
    public interface OnItemClickListener {
        void onItemClick(int value);
    }

    public DiscountAdapter(Context context, List<Discount> discountList, OnItemClickListener listener) {
        this.mContext = context;
        this.discountList = discountList;
        this.listener = listener;
        getTotalPrice();
    }

    @NonNull
    @Override
    public DiscountAdapter.DiscountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_discount, parent, false);
        return new DiscountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscountViewHolder holder, int position) {
        Discount discount = discountList.get(position);
        holder.discountTxt.setText(String.format("Giảm giá %d%%", discount.getValueDiscount()));
        holder.discountConditionTxt.setText(discount.getConditionDiscount());

        if (total < discount.getConditionTotal()) {
            double totalNeeded = discount.getConditionTotal() - total;
            holder.subDiscountConditionTxt.setText(String.format("Hãy mua thêm %,d.000đ để nhận được khuyến mãi này", (int) totalNeeded));
            holder.itemView.setAlpha(0.5f);
            holder.radioButton.setVisibility(View.GONE);
        } else {
            holder.subDiscountConditionTxt.setVisibility(View.GONE);
            holder.radioButton.setVisibility(View.VISIBLE);
            holder.itemView.setAlpha(1f);
            holder.radioButton.setEnabled(true);
        }

        holder.radioButton.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            listener.onItemClick(discount.getValueDiscount());
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
        });

        holder.radioButton.setOnClickListener(v -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            listener.onItemClick(discount.getValueDiscount());
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        if (discountList != null) {
            return discountList.size();
        }
        return 0;
    }

    public static class DiscountViewHolder extends RecyclerView.ViewHolder {
        private final TextView discountTxt;
        private final TextView discountConditionTxt;
        private final TextView subDiscountConditionTxt;
        private final RadioButton radioButton;
        public DiscountViewHolder(@NonNull View itemView) {
            super(itemView);
            discountTxt = itemView.findViewById(R.id.discountTxt);
            discountConditionTxt = itemView.findViewById(R.id.discountConditionTxt);
            subDiscountConditionTxt = itemView.findViewById(R.id.subDiscountConditionTxt);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }

    private String getUsernameFromSharedPreferences() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("username", "");
    }

    private void getTotalPrice() {
        String username = getUsernameFromSharedPreferences();
        if (username.isEmpty()) {
            return;
        }
        APIService.apiService.getTotalPrice(username)
                .enqueue(new Callback<TotalPriceResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<TotalPriceResponse> call, @NonNull Response<TotalPriceResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            TotalPriceResponse totalPriceResponse = response.body();
                            total = totalPriceResponse.getTotalPrice();
                            notifyDataSetChanged();
                        } else {
                            Log.e("Discount Adapter", "Get total price failed ");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<TotalPriceResponse> call, @NonNull Throwable t) {
                        Log.e("API Error", "Call API error: " + t.getMessage(), t);
                    }
                });
    }

}
