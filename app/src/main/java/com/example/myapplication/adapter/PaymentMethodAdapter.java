package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.PaymentMethod;

import java.util.List;

public class PaymentMethodAdapter extends RecyclerView.Adapter<PaymentMethodAdapter.ViewHolder> {

    private final List<PaymentMethod> paymentMethods;
    private int selectedPosition = -1;

    public PaymentMethodAdapter(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentMethod paymentMethod = paymentMethods.get(position);
        holder.imageViewIcon.setImageResource(paymentMethod.getIcon());
        holder.textViewTitle.setText(paymentMethod.getTitle());
        holder.textViewDescription.setText(paymentMethod.getDescription());
        holder.radioButton.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });

        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return paymentMethods.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewIcon;
        TextView textViewTitle;
        TextView textViewDescription;
        RadioButton radioButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}
