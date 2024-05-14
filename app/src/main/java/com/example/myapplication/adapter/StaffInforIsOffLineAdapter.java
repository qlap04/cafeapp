package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Helper.SwipeHelper;
import com.example.myapplication.R;
import com.example.myapplication.activity.StaffInforEditActivity;
import com.example.myapplication.interface1.ItemTouchListener;
import com.example.myapplication.interface1.SwipeListener;
import com.example.myapplication.model.User;

import java.util.List;

public class StaffInforIsOffLineAdapter extends RecyclerView.Adapter<StaffInforIsOffLineAdapter.StaffInforIsOffLineViewHolder> {
    private List<User> userList;
    private Context context;
    private SwipeListener swipeListener;
    private ItemTouchListener itemTouchListener;

    public void setItemTouchListener(ItemTouchListener itemTouchListener) {
        this.itemTouchListener = itemTouchListener;
    }

    public void setSwipeListener(SwipeListener swipeListener) {
        this.swipeListener = swipeListener;
    }

    public StaffInforIsOffLineAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public StaffInforIsOffLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_staff_is_offline, parent, false);
        return new StaffInforIsOffLineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StaffInforIsOffLineViewHolder holder, int position) {
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
        return userList.size();
    }

    public class StaffInforIsOffLineViewHolder extends RecyclerView.ViewHolder {
        private TextView idTxt;
        private TextView nameTxt;
        private TextView roleTxt;

        public StaffInforIsOffLineViewHolder(@NonNull View itemView) {
            super(itemView);
            idTxt = itemView.findViewById(R.id.idTxt);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            roleTxt = itemView.findViewById(R.id.roleTxt);


        }
    }
}
