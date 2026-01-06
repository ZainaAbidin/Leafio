package com.example.leafio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {

    private ArrayList<Tanaman> plantList;
    private OnPlantLongClickListener longClickListener;

    // Interface untuk menangani klik lama (hapus)
    public interface OnPlantLongClickListener {
        void onPlantLongClick(int position);
    }

    public PlantAdapter(ArrayList<Tanaman> plantList, OnPlantLongClickListener listener) {
        this.plantList = plantList;
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Pastikan Anda sudah membuat file item_tanaman.xml di folder layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tanaman, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Tanaman currentPlant = plantList.get(position);
        holder.tvNama.setText(currentPlant.getNama());
        holder.tvSiram.setText("Penyiraman berikutnya: " + currentPlant.getSiram());

        // Logika klik lama untuk hapus
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onPlantLongClick(position);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return plantList != null ? plantList.size() : 0;
    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvSiram;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama_tanaman_item);
            tvSiram = itemView.findViewById(R.id.tv_info_siram);
        }
    }
}