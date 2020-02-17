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
import com.ezen.lolketing.model.ShopDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ShopAdapter extends FirestoreRecyclerAdapter<ShopDTO, ShopAdapter.ShopHolder> {

    public ShopAdapter(@NonNull FirestoreRecyclerOptions<ShopDTO> options) {
        super(options);
    }

    @NonNull
    @Override
    public ShopAdapter.ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);
        return new ShopHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShopHolder shopHolder, final int position,
                                    @NonNull ShopDTO shopDTO) {

        Glide.with(shopHolder.itemView.getContext()).load(shopDTO.getImages().get(0)).into(shopHolder.product_image);
        shopHolder.product_name.setText(shopDTO.getName());
        shopHolder.product_price.setText(shopDTO.getPrice()+"");
    }

    class ShopHolder extends RecyclerView.ViewHolder {
        TextView product_name;
        TextView product_price;
        ImageView product_image;

        public ShopHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
        }
    }
}
