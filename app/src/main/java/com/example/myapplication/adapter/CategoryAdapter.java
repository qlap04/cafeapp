package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Category;
import com.example.myapplication.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>{

    private final List<Category> categoryList;
    private OnCategoryClickListener onCategoryClickListener;
    public CategoryAdapter(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    public CategoryAdapter(List<Category> categoryList, OnCategoryClickListener listener) {
        this.categoryList = categoryList;
        this.onCategoryClickListener = listener;
    }
    public interface OnCategoryClickListener {
        void onCategoryClick(Category category);
    }
    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryAdapter.CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);
        if (category == null) {
            return;
        }
        holder.title.setText(category.getTitle());
        holder.pic.setImageResource(category.getImageResource());
    }

    @Override
    public int getItemCount() {
        if (categoryList != null) {
            return categoryList.size();
        }
        return 0;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        private final ImageView pic;
        private final TextView title    ;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.imgCat);
            title = itemView.findViewById(R.id.catNameTxt);
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Category category = categoryList.get(position);
                    onCategoryClickListener.onCategoryClick(category);
                }
            });
        }
    }
}
