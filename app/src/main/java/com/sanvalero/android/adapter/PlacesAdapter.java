package com.sanvalero.android.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanvalero.android.R;
import com.sanvalero.android.callback.PlacesCallback;
import com.sanvalero.android.model.Place;
import com.sanvalero.android.util.Utils;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private PlacesCallback callback;
    private List<Place> placeList;

    public PlacesAdapter(PlacesCallback callback, List<Place> placeList) {
        this.callback = callback;
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
        String address = Utils.parseCustomAddress(place.getAddress()).second;
        holder.addressTextView.setText(address.isEmpty() ? place.getAddress() : address);
        holder.equipmentTextView.setText(place.getEquipment());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PlaceAdapter", "Clicked: " + place.getId());
                callback.onPlaceClicked(place.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public static class PlaceViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, nameTextView, addressTextView, equipmentTextView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.placeIdItemTextView);
            nameTextView = itemView.findViewById(R.id.placeNameItemTextView);
            addressTextView = itemView.findViewById(R.id.placeAddressItemTextView);
            equipmentTextView = itemView.findViewById(R.id.placeEquipmentItemTextView);

        }
    }
}
