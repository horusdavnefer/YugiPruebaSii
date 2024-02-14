package com.sii.yugiohcards.adapters;

import static android.view.LayoutInflater.from;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sii.yugiohcards.R;
import com.sii.yugiohcards.objects.Card;
import com.sii.yugiohcards.objects.CardSet;

import java.util.List;

public class SetsAdapter extends RecyclerView.Adapter<SetsAdapter.ViewHolder> {

    private List<CardSet> sets;
    private Context context;

    public SetsAdapter(Context context, List<CardSet> sets) {
        this.sets = sets;
        this.context = context;
    }

    @NonNull
    @Override
    public SetsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = from(parent.getContext()).inflate(R.layout.item_sets, parent, false);
        return new SetsAdapter.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull SetsAdapter.ViewHolder holder, int position) {
        CardSet set = sets.get(position);


        holder.titleset.setText(set.getSet_name());
        holder.priceset.setText("$"+set.getSet_price());


    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleset, priceset;


        public ViewHolder(View v) {
            super(v);
            titleset = v.findViewById(R.id.titleSets);
            priceset = v.findViewById(R.id.priceSets);
        }
    }
}
