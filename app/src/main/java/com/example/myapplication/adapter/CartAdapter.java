package com.example.myapplication.adapter;

import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.activity.CartActivity;
import com.example.myapplication.model.Cart;
import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Callback;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    //
    private final List<Cart> productsInCart;
    private OnQuantityChangeListener onQuantityChangeListener;
    private OnDeleteListener onDeleteListener;

    private CartActivity cartActivity;

    public interface OnQuantityChangeListener {
        void onQuantityChange(int position, int newQuantity);
    }
    public interface OnDeleteListener {
        void onDelete(int position, Cart cart);
    }
    public void setOnQuantityChangeListener(OnQuantityChangeListener listener) {
        this.onQuantityChangeListener = listener;
    }
    public void setOnDeleteListener(OnDeleteListener listener) {
        this.onDeleteListener = listener;
    }
    public CartAdapter(List<Cart> productsInCart, CartActivity cartActivity) {
        this.cartActivity = cartActivity;
        this.productsInCart = productsInCart;
    }
    public CartAdapter(List<Cart> productsInCart) {
        this.productsInCart = productsInCart;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        DecimalFormat decimalFormat = new DecimalFormat("#,### đ");
        Cart cart = productsInCart.get(position);
        if (productsInCart.isEmpty()) {
            return;
        }
        Picasso.get().load(cart.getImage()).into(holder.pic);
        holder.titleTxt.setText(cart.getTitle());
        holder.priceTxt.setText(decimalFormat.format(cart.getPrice() * 1000));
        holder.quantityTxt.setText(String.valueOf(cart.getQuantity()));
        holder.totalTxt.setText(decimalFormat.format(cart.getTotal() * 1000));
        holder.minusBtn.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantityTxt.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                double total = cart.getPrice() * currentQuantity;
                holder.quantityTxt.setText(String.valueOf(currentQuantity));
                holder.totalTxt.setText(decimalFormat.format(cart.getTotal() * 1000));
                if (onQuantityChangeListener != null) {
                    onQuantityChangeListener.onQuantityChange(holder.getAdapterPosition(), currentQuantity);
                }
            } else {
                showDeleteConfirmationDialog(holder);
            }
        });


        holder.addBtn.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.quantityTxt.getText().toString());
            currentQuantity++;
            double total = cart.getPrice() * currentQuantity;
            holder.quantityTxt.setText(String.valueOf(currentQuantity));
            holder.totalTxt.setText(decimalFormat.format(cart.getTotal() * 1000));
            if (onQuantityChangeListener != null) {
                onQuantityChangeListener.onQuantityChange(holder.getAdapterPosition(), currentQuantity);
            }
        });
        holder.trashBtn.setOnClickListener(v -> {
            showDeleteConfirmationDialog(holder);
        });

    }
    @Override
    public int getItemCount() {
        if (productsInCart != null) {
            return productsInCart.size();
        }
        return 0;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        private final ImageView pic;
        private final TextView titleTxt;
        private final TextView priceTxt;
        private final TextView totalTxt;
        private final TextView quantityTxt;
        private final TextView minusBtn;
        private final TextView addBtn;
        private final ImageView trashBtn;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            priceTxt = itemView.findViewById(R.id.priceTxt);
            quantityTxt = itemView.findViewById(R.id.numTxt);
            totalTxt = itemView.findViewById(R.id.totalTxt);
            minusBtn = itemView.findViewById(R.id.minusBtn);
            addBtn = itemView.findViewById(R.id.addBtn);
            trashBtn = itemView.findViewById(R.id.trashBtn);
        }
    }
    private void showDeleteConfirmationDialog(CartViewHolder holder) {
        Dialog dialog = new Dialog(holder.itemView.getContext());
        dialog.setContentView(R.layout.custom_dialog_delete);
        Button btnDelete = dialog.findViewById(R.id.btnDialogDelete);
        Button btnCancel = dialog.findViewById(R.id.btnDialogCancel);

        btnDelete.setOnClickListener(v1 -> {
            if (onDeleteListener != null) {
                onDeleteListener.onDelete(holder.getAdapterPosition(), productsInCart.get(holder.getAdapterPosition()));
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v12 -> dialog.dismiss());

        dialog.show();
    }


}
