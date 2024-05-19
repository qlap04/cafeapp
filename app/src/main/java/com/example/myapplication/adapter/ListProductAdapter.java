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

public class ListProductAdapter extends RecyclerView.Adapter<ListProductAdapter.ListProductViewHolder> {
    //
    private final List<Product> productList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product product);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ListProductAdapter(List<Product> productList) {
        this.productList = productList;
    }


    @NonNull
    @Override
    public ListProductAdapter.ListProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_product, parent, false);
        return new ListProductAdapter.ListProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListProductAdapter.ListProductViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product == null) {
            return;
        }
        Picasso.get().load(product.getImage()).into(holder.pic);
        holder.title.setText(product.getTitle());
        holder.price.setText(product.getPrice() + ".000");
        holder.star.setText(product.getStar().toString());
        holder.bind(product, (OnItemClickListener) listener);

    }

    @Override
    public int getItemCount() {
        if (productList != null) {
            return productList.size();
        }
        return 0;
    }

    public static class ListProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView pic;
        private final TextView title;
        private final TextView price;
        private final TextView star;

        public ListProductViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title2Txt);
            price = itemView.findViewById(R.id.price1Txt);
            star = itemView.findViewById(R.id.rateTxt);

        }

        public void bind(final Product product, final OnItemClickListener listener) {
            Picasso.get().load(product.getImage()).into(pic);
            title.setText(product.getTitle());
            price.setText(product.getPrice() + "00đ");
            star.setText(product.getStar().toString());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(product);
                }
            });
        }
    }
}
