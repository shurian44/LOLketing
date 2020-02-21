package com.ezen.lolketing.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezen.lolketing.R;
import com.ezen.lolketing.model.PurchaseDTO;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class PurchaseAdapter extends
        FirestoreRecyclerAdapter<PurchaseDTO, PurchaseAdapter.PurchaseHolder> {

    public PurchaseAdapter(@NonNull FirestoreRecyclerOptions<PurchaseDTO> options) {
        super(options);
    }

    @NonNull
    @Override
    public PurchaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_purchase_result, parent, false);
        return new PurchaseHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull PurchaseHolder purchaseHolder,
                                    int position, @NonNull PurchaseDTO purchaseDTO) {


    }

    class PurchaseHolder extends RecyclerView.ViewHolder {


        public PurchaseHolder(@NonNull View itemView) {
            super(itemView);


        }
    }
}
