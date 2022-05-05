package com.ezen.lolketing.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezen.lolketing.R;
import com.ezen.lolketing.view.main.shop.ShopDetailActivity;
import com.ezen.lolketing.model.ShopDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ShopAdapter extends FirestoreRecyclerAdapter<ShopDTO, ShopAdapter.ShopHolder> {

    MoveActivityListener listener;

    public ShopAdapter(@NonNull FirestoreRecyclerOptions<ShopDTO> options, MoveActivityListener listener) {
        super(options);
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShopAdapter.ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);
        return new ShopHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull final ShopHolder shopHolder, final int position,
                                    @NonNull final ShopDTO shopDTO) {

        Glide.with(shopHolder.itemView.getContext()).load(shopDTO.getImages().get(0)).into(shopHolder.product_image);
        shopHolder.product_name.setText(shopDTO.getName());
        shopHolder.product_name.setSingleLine(true); // 한줄로 표시하기
        shopHolder.product_name.setEllipsize(TextUtils.TruncateAt.MARQUEE); // 흐르게 만들기
        shopHolder.product_name.setSelected(true); // 선택하기
        shopHolder.product_price.setText(shopDTO.getPrice()+"" + "원");

        shopHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shopHolder.itemView.getContext(), ShopDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("category", shopDTO.getGroup());
                intent.putExtra("name", shopDTO.getName());
                intent.putExtra("image", shopDTO.getImages());
                intent.putExtra("price", shopDTO.getPrice());
                listener.MoveActivity(intent);
            }
        });
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

    public interface MoveActivityListener{
        void MoveActivity(Intent intent);
    }
}
