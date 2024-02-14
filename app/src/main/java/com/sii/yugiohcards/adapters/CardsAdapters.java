package com.sii.yugiohcards.adapters;

import static android.view.LayoutInflater.from;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sii.yugiohcards.Activities.DetailCardActivity;
import com.sii.yugiohcards.R;
import com.sii.yugiohcards.objects.Card;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CardsAdapters extends RecyclerView.Adapter<CardsAdapters.ViewHolder> implements Filterable {

    private List<Card> cardList;
    private List<Card> filteredCardList;
    private Context context;

    public CardsAdapters(Context context, List<Card> cardList) {
        this.cardList = cardList;
        this.context = context;
        this.filteredCardList = cardList;
    }

    @NonNull
    @Override
    public CardsAdapters.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = from(parent.getContext()).inflate(R.layout.item_card_home, parent, false);
        return new CardsAdapters.ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardsAdapters.ViewHolder holder, int position) {

        Card card = filteredCardList.get(position);

        if (!card.getCard_images().get(0).getImage_url_small().isEmpty() ) {
            Picasso.get().load(card.getCard_images().get(0).getImage_url_small()).into(holder.imageCard);
        }

        holder.nameCard.setText(card.getName());
        holder.type.setText(card.getType());
        holder.desc.setText(card.getDesc());
        holder.main.setOnClickListener(v -> {
            Intent i = new Intent(context, DetailCardActivity.class);
            i.putExtra("name",card.getName());
            context.startActivity(i);
        });

    }

    @Override
    public int getItemCount() {
        return filteredCardList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charSequenceString = constraint.toString();
                if (charSequenceString.isEmpty()) {
                    filteredCardList = cardList;
                } else {
                    List<Card> filteredList = new ArrayList<>();
                    for (Card name : cardList) {
                        if (name.getName().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filteredList.add(name);
                        }
                        filteredCardList = filteredList;
                    }



                }
                FilterResults results = new FilterResults();
                results.values = filteredCardList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredCardList = (List<Card>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameCard,type,desc;
        ImageView imageCard;
        RelativeLayout main;



        public ViewHolder(View v) {
            super(v);
            nameCard = v.findViewById(R.id.nameCard);
            type = v.findViewById(R.id.atackCard);
            desc = v.findViewById(R.id.defCard);
            imageCard = v.findViewById(R.id.imageCard);
            main = v.findViewById(R.id.main);
        }
    }


}
