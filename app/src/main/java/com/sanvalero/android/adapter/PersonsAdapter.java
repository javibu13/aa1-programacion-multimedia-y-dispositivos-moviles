package com.sanvalero.android.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanvalero.android.R;
import com.sanvalero.android.model.Person;
import com.sanvalero.android.model.Place;

import java.util.List;

public class PersonsAdapter extends RecyclerView.Adapter<PersonsAdapter.PersonViewHolder> {

    private List<Person> personList;

    public PersonsAdapter(List<Person> personList) {
        this.personList = personList;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_person, parent, false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = personList.get(position);
        holder.idTextView.setText(String.format("%s", person.getId()));
        holder.fullNameTextView.setText(String.format("%s %s", person.getFirstName(), person.getLastName()));
        holder.emailTextView.setText(person.getEmail());
        holder.interestsTextView.setText(person.getInterests());
        if (person.getVerified()) {
            holder.verifiedFalseImageView.setVisibility(View.GONE);
        } else {
            holder.verifiedTrueImageView.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PersonAdapter", "Clicked: " + person.getFirstName() + " " + person.getLastName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return personList.size();
    }

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView idTextView, fullNameTextView, emailTextView, interestsTextView;
        ImageView verifiedTrueImageView, verifiedFalseImageView;

        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            // TextView
            idTextView = itemView.findViewById(R.id.personIdItemTextView);
            fullNameTextView = itemView.findViewById(R.id.personFullNameItemTextView);
            emailTextView = itemView.findViewById(R.id.personEmailItemTextView);
            interestsTextView = itemView.findViewById(R.id.personInterestsItemTextView);
            // ImageView
            verifiedTrueImageView = itemView.findViewById(R.id.personVerifiedTrueItemImageView);
            verifiedFalseImageView = itemView.findViewById(R.id.personVerifiedFalseItemImageView);
        }
    }
}
