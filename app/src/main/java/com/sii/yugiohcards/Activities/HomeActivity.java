package com.sii.yugiohcards.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.sii.yugiohcards.R;
import com.sii.yugiohcards.adapters.CardsAdapters;
import com.sii.yugiohcards.helpers.AppExecutors;
import com.sii.yugiohcards.helpers.Utils;
import com.sii.yugiohcards.objects.Banlist;
import com.sii.yugiohcards.objects.Card;
import com.sii.yugiohcards.objects.CardImages;
import com.sii.yugiohcards.objects.CardPrices;
import com.sii.yugiohcards.objects.CardSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recyclerView;
    Context context;
    ProgressBar progressBar;
    List<Card> list = new ArrayList<>();
    CardsAdapters adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_home);
        searchView = findViewById(R.id.findCard);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.listCards);
        LinearLayoutManager Vertical = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(Vertical);
        recyclerView.setHasFixedSize(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s != null) {
                    if (!s.isEmpty()) {
                        adapter.getFilter().filter(s);
                    }
                }

                return false;
            }
        });

        if (Utils.isConnectInternet(context)) {
            getData();
        } else {
            alert();
        }


    }
    void alert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customLayout = li.inflate(R.layout.dialog_nointernet, null);
        TextView title = customLayout.findViewById(R.id.titulo);
        title.setText("Sin conexion");
        Button reintenar = customLayout.findViewById(R.id.reintentar);
        builder.setView(customLayout);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        reintenar.setOnClickListener(v -> {
            getData();
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void getData(){
        showLoading();
        list.clear();
        String url = getString(R.string.url) + "cardinfo.php";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                /**en caso de que el internet sea inestable o no se obtenga respuesta del servidor se enseñara el ultimo valor que se tenga**/
                Log.e("e", e.toString());
                AppExecutors.getInstance().mainThread().execute(() -> {
                    hideLoading();
                    Utils.alertInWorking(context);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String respuesta = response.body().string();
                    try {
                        AppExecutors.getInstance().mainThread().execute(() -> {
                            try {
                                JSONObject object = new JSONObject(respuesta);
                                JSONArray jsonArray = object.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject row = jsonArray.getJSONObject(i);
                                    Card card = new Card();
                                    card.setId(row.getInt("id"));
                                    card.setName(row.getString("name"));
                                    card.setType(row.getString("type"));
                                    card.setFrameType(row.getString("frameType"));
                                    card.setRace(row.getString("race"));
                                    if (row.has("archetype")){
                                        card.setArchetype(row.getString("archetype"));
                                    }
                                    card.setDesc(row.getString("desc"));
                                    card.setYgoprodeck_url(row.getString("ygoprodeck_url"));

                                    if (row.has("card_sets")) {
                                        JSONArray cardsetArray = row.getJSONArray("card_sets");
                                        List<CardSet> cardSetList = new ArrayList<>();
                                        for (int j = 0; j < cardsetArray.length(); j++) {
                                            JSONObject rowcardSet = cardsetArray.getJSONObject(j);
                                            CardSet cardSet = new CardSet();
                                            cardSet.setSet_name(rowcardSet.getString("set_name"));
                                            cardSet.setSet_code(rowcardSet.getString("set_code"));
                                            cardSet.setSet_price(rowcardSet.getString("set_price"));
                                            cardSet.setSet_rarity(rowcardSet.getString("set_rarity"));
                                            cardSet.setSet_rarity_code(rowcardSet.getString("set_rarity_code"));
                                            cardSetList.add(cardSet);
                                        }
                                        card.setCard_sets(cardSetList);
                                    }
                                    JSONArray imagesArray = row.getJSONArray("card_images");
                                    List<CardImages> imagesList = new ArrayList<>();
                                    for (int j = 0; j < imagesArray.length(); j++) {
                                        JSONObject rowimagesSet = imagesArray.getJSONObject(j);
                                        CardImages cardImages = new CardImages();
                                        cardImages.setId(rowimagesSet.getInt("id"));
                                        cardImages.setImage_url(rowimagesSet.getString("image_url"));
                                        cardImages.setImage_url_small(rowimagesSet.getString("image_url_small"));
                                        cardImages.setImage_url_cropped(rowimagesSet.getString("image_url_cropped"));

                                        imagesList.add(cardImages);
                                    }
                                    card.setCard_images(imagesList);

                                    JSONArray pricesArray = row.getJSONArray("card_prices");
                                    List<CardPrices> pricesList = new ArrayList<>();
                                    for (int j = 0; j < pricesArray.length(); j++) {
                                        JSONObject rowpricesSet = pricesArray.getJSONObject(j);
                                        CardPrices prices = new CardPrices();
                                        prices.setAmazon_price(rowpricesSet.getString("amazon_price"));
                                        prices.setCardmarket_price(rowpricesSet.getString("cardmarket_price"));
                                        prices.setCoolstuffinc_price(rowpricesSet.getString("coolstuffinc_price"));
                                        prices.setEbay_price(rowpricesSet.getString("ebay_price"));
                                        prices.setTcgplayer_price(rowpricesSet.getString("tcgplayer_price"));
                                        pricesList.add(prices);
                                    }
                                    card.setCard_prices(pricesList);
                                    if (row.has("atk")) {
                                        card.setAtk(row.getInt("atk"));
                                    }
                                    if (row.has("def")) {
                                        card.setDef(row.getInt("def"));
                                    }
                                    if (row.has("level")) {
                                        card.setLevel(row.getInt("level"));
                                    }
                                    if (row.has("banlist_info")) {
                                        JSONObject ban = row.getJSONObject("banlist_info");
                                        Banlist banlist = new Banlist();
                                        if (ban.has("ban_tcg")) {
                                            banlist.setBan_tcg("ban_tcg");
                                        }
                                        if (ban.has("ban_ocg")) {
                                            banlist.setBan_ocg("ban_ocg");
                                        }
                                        if (ban.has("ban_goat")) {
                                            banlist.setBan_goat("ban_goat");
                                        }
                                    } else {
                                        list.add(card);
                                    }
                                }
                                hideLoading();
                                adapter = new CardsAdapters(context, list);
                                recyclerView.setAdapter(adapter);


                            } catch (JSONException e) {
                                AppExecutors.getInstance().mainThread().execute(() -> {
                                    hideLoading();
                                    Utils.alertInWorking(context);
                                    //mProgressBar.setVisibility(View.GONE);
                                });
                            }
                        });


                    } catch (Exception e) {
                        AppExecutors.getInstance().mainThread().execute(() -> {
                            hideLoading();
                            Utils.alertInWorking(context);
                        });
                    }
                } else {
                    AppExecutors.getInstance().mainThread().execute(() -> {
                        hideLoading();
                        Utils.alertInWorking(context);
                    });
                }
            }
        });


    }

    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideLoading(){
        progressBar.setVisibility(View.GONE);
    }
}