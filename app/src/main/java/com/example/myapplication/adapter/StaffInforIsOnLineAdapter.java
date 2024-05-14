package com.example.myapplication.adapter;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.StaffInforEditActivity;
import com.example.myapplication.model.User;

import java.util.List;

public class StaffInforIsOnLineAdapter extends RecyclerView.Adapter<StaffInforIsOnLineAdapter.StaffInforIsOnLineViewHolder> {
    private List<User> userList;

    public StaffInforIsOnLineAdapter(List<User> userList) {
        this.userList = userList;
    }


    @NonNull
    @Override
    public StaffInforIsOnLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_is_online, parent, false);
        return new StaffInforIsOnLineViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull StaffInforIsOnLineViewHolder holder, int position) {
        User user = userList.get(position);
        if (user == null) {
            return;
        }
        holder.idTxt.setText(String.format("ID: %s", String.valueOf(user.getUserId())));
        holder.nameTxt.setText(String.format("Họ và tên: %s", user.getFullname()));
        holder.roleTxt.setText(String.format("SĐT: %s", user.getPhoneNumber()));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), StaffInforEditActivity.class);
            intent.putExtra("ID-USER", user.getUserId());
            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        if (!userList.isEmpty()) {
            return userList.size();
        }
        return 0;
    }

    public static class StaffInforIsOnLineViewHolder extends RecyclerView.ViewHolder {
        private TextView idTxt;
        private TextView nameTxt;
        private TextView roleTxt;
        public StaffInforIsOnLineViewHolder(@NonNull View itemView) {
            super(itemView);
            idTxt = itemView.findViewById(R.id.idTxt);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            roleTxt = itemView.findViewById(R.id.roleTxt);
        }
    }
}

