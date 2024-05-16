package com.example.myapplication.adapter;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.interface1.OnAddressClickListener;
import com.example.myapplication.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private final List<Address> listAddress;
    private final OnAddressClickListener onAddressClickListener;

    private int selectedPosition = -1;
    public AddressAdapter(List<Address> listAddress, OnAddressClickListener onAddressClickListener) {
        this.listAddress = listAddress;
        this.onAddressClickListener = onAddressClickListener;
    }


    @NonNull
    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address address = listAddress.get(position);
        holder.nameTxt.setText(address.getName());
        holder.phoneTxt.setText(address.getPhone());
        holder.addressTxt.setText(address.getAddress());
        holder.radioButton.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            onAddressClickListener.onAddressClick(selectedPosition);
            notifyDataSetChanged();
        });

        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            onAddressClickListener.onAddressClick(selectedPosition);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        if (listAddress != null) {
            return listAddress.size();
        }
        return 0;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTxt;
        private final TextView phoneTxt;
        private final TextView addressTxt;
        private final RadioButton radioButton;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButton.setOnClickListener(v -> handleRadioButtonChecks(getAdapterPosition()));
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleRadioButtonChecks(int adapterPosition) {
        boolean isNewRadioButtonChecked = true;
        listAddress.get(selectedPosition).setSelected(false);
        listAddress.get(adapterPosition).setSelected(true);
        selectedPosition = adapterPosition;
        notifyDataSetChanged();
    }

}
