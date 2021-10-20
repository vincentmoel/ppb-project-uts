package com.vincent.ppb_project.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.vincent.ppb_project.R;
import com.vincent.ppb_project.model.CartModel;
import com.vincent.ppb_project.model.PelajaranModel;

import java.util.ArrayList;

public class PelajaranAdapter extends FirestoreRecyclerAdapter<PelajaranModel, PelajaranAdapter.PelajaranViewHolder> {

    private OnItemClickCallback onItemClickCallback;
    private ArrayList<CartModel> dataCart;

    public PelajaranAdapter(
            @NonNull FirestoreRecyclerOptions<PelajaranModel> options,
            OnItemClickCallback onItemClickCallback,
            ArrayList<CartModel> dataCart
    ) {
        super(options);
        this.onItemClickCallback = onItemClickCallback;
        this.dataCart = dataCart;
    }

    public static class PelajaranViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvKelas;
        private final TextView tvMateri;
        private final TextView tvWaktu;
        private final TextView tvBentrokTitle;
        private final TextView tvBentrokMateri;
        private final TextView tvKuota;
        private final ImageView ivPlus;
        private final ImageView ivMinus;

        public PelajaranViewHolder(@NonNull View itemView) {
            super(itemView);
            tvKelas = itemView.findViewById(R.id.tv_kelas);
            tvMateri = itemView.findViewById(R.id.tv_materi);
            tvWaktu = itemView.findViewById(R.id.tv_waktu);
            tvBentrokTitle = itemView.findViewById(R.id.tv_bentrok_title);
            tvBentrokMateri = itemView.findViewById(R.id.tv_bentrok_materi);
            ivPlus = itemView.findViewById(R.id.iv_plus);
            ivMinus = itemView.findViewById(R.id.iv_minus);
            tvKuota = itemView.findViewById(R.id.tv_kuota);
        }

        public TextView getTvKuota() {
            return tvKuota;
        }

        public TextView getTvKelas() {
            return tvKelas;
        }

        public TextView getTvMateri() {
            return tvMateri;
        }

        public TextView getTvWaktu() {
            return tvWaktu;
        }

        public TextView getTvBentrokTitle() {
            return tvBentrokTitle;
        }

        public TextView getTvBentrokMateri() {
            return tvBentrokMateri;
        }

        public ImageView getIvPlus() {
            return ivPlus;
        }

        public ImageView getIvMinus() {
            return ivMinus;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull PelajaranViewHolder holder, int position, @NonNull PelajaranModel model) {
        holder.getTvKelas().setText("Kelas " + model.getKelas());
        holder.getTvMateri().setText(model.getMateri());

        String startAt = String.valueOf(model.getStart_at());
        String finishAt = String.valueOf(model.getFinish_at());

        // Menambahkan : ke Start At
        StringBuilder sbStartAt = new StringBuilder(startAt);
        if (startAt.length() == 3) {
            sbStartAt.insert(1, ":");
        } else {
            sbStartAt.insert(2, ":");
        }

        // Menambahkan : ke Finish At
        StringBuilder sbFinishAt = new StringBuilder(finishAt);
        if (finishAt.length() == 3) {
            sbFinishAt.insert(1, ":");
        } else {
            sbFinishAt.insert(2, ":");
        }


        holder.getTvWaktu().setText(model.getHari() + ", " + sbStartAt + " - " + sbFinishAt + " WIB");
        holder.getTvKuota().setText(String.valueOf(model.getKuota()));
//        holder.getTvBentrokTitle().setVisibility(View.GONE);
        holder.getTvBentrokTitle().setText(String.valueOf(dataCart.size()));
        holder.getTvBentrokMateri().setVisibility(View.GONE);

        holder.ivMinus.setVisibility(View.GONE);

        // On Item Click Callback
        holder.ivPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemAddToCart(model);
            }
        });

        holder.ivMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemDeleteFromCart(model);
            }
        });
    }

    @NonNull
    @Override
    public PelajaranViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_pelajaran, parent, false);
        return new PelajaranViewHolder(view);
    }

    public interface OnItemClickCallback {
        void onItemAddToCart(PelajaranModel dataPelajaran);
        void onItemDeleteFromCart(PelajaranModel dataPelajaran);
    }


}
