package com.ezen.lolketing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.R;
import com.ezen.lolketing.model.PurchaseDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PurchaseAdapter extends
        FirestoreRecyclerAdapter<PurchaseDTO, PurchaseAdapter.PurchaseHolder> {

    public PurchaseAdapter(@NonNull FirestoreRecyclerOptions<PurchaseDTO> options) {
        super(options);
    }

    @NonNull
    @Override
    public PurchaseAdapter.PurchaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_purchase, parent, false);
        return new PurchaseHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PurchaseAdapter.PurchaseHolder purchaseHolder,
                                    int position, @NonNull PurchaseDTO purchaseDTO) {
        // timestamp Long -> String으로 타입변환
        Date date = new Date(purchaseDTO.getTimestamp());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        purchaseHolder.timestamp.setText(format.format(date));
        purchaseHolder.product_name.setText(purchaseDTO.getName());
        purchaseHolder.product_amount.setText(Integer.toString(purchaseDTO.getAmount()));
        purchaseHolder.product_address.setText(purchaseDTO.getAddress());
        purchaseHolder.product_price.setText(Integer.toString(purchaseDTO.getPrice()));
        Glide.with(purchaseHolder.itemView.getContext()).load(purchaseDTO.getImage()).into(purchaseHolder.product_image);
    }

    class PurchaseHolder extends RecyclerView.ViewHolder {

        TextView timestamp;
        TextView product_name;
        TextView product_amount;
        TextView product_address;
        TextView product_price;
        ImageView product_image;

        public PurchaseHolder(@NonNull View itemView) {
            super(itemView);

            timestamp = itemView.findViewById(R.id.timestamp);
            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_amount = itemView.findViewById(R.id.product_amount);
            product_address = itemView.findViewById(R.id.product_address);
            product_price = itemView.findViewById(R.id.product_price);
        }
    }
}
