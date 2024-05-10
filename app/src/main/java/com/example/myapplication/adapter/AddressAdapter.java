package com.example.myapplication.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder>{
    private final List<Address> listAddress;
    private boolean isNewRadioButtonChecked = false;
    private int lastCheckedPositon = -1;
    public AddressAdapter(List<Address> listAddress) {
        this.listAddress = listAddress;
    }

    @NonNull
    @Override
    public AddressAdapter.AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.AddressViewHolder holder, int position) {
        Address address = listAddress.get(position);
        if (address == null) {
            return;
        }
        holder.nameTxt.setText(address.getName());
        holder.phoneTxt.setText(address.getPhone());
        holder.addressTxt.setText(address.getAddress());
        if (isNewRadioButtonChecked) {
            holder.radioButton.setChecked(address.isSelected());
        } else {
            if (holder.getAdapterPosition() == 0) {
                holder.radioButton.setChecked(true);
                lastCheckedPositon = 0;
            }
        }

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
        private RadioButton radioButton;
        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            phoneTxt = itemView.findViewById(R.id.phoneTxt);
            addressTxt = itemView.findViewById(R.id.addressTxt);
            radioButton = itemView.findViewById(R.id.radioButton);
            radioButton.setOnClickListener(v -> handleRadioButtonChecks(getAdapterPosition()));
        }
    }
    private void handleRadioButtonChecks(int adapterPosition) {
        isNewRadioButtonChecked = true;
        listAddress.get(lastCheckedPositon).setSelected(false);
        listAddress.get(adapterPosition).setSelected(true);
        lastCheckedPositon = adapterPosition;
        notifyDataSetChanged();
    }
}
