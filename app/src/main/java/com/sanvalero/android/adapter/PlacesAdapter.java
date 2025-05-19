package com.sanvalero.android.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanvalero.android.R;
import com.sanvalero.android.model.Place;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private List<Place> placeList;

    public PlacesAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.idTextView.setText(place.getId().toString());
        holder.nameTextView.setText(place.getName());
        holder.equipmentTextView.setText(place.getEquipment());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PlaceAdapter", "Clicked: " + place.getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, nameTextView, equipmentTextView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.placeIdItemTextView);
            nameTextView = itemView.findViewById(R.id.placeNameItemTextView);
            equipmentTextView = itemView.findViewById(R.id.placeEquipmentItemTextView);

        }
    }
}
