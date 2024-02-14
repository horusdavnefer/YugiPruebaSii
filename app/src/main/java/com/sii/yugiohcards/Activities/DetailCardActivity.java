package com.sii.yugiohcards.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sii.yugiohcards.R;
import com.sii.yugiohcards.adapters.CardsAdapters;
import com.sii.yugiohcards.adapters.ImagesAdapter;
import com.sii.yugiohcards.adapters.SetsAdapter;
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
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailCardActivity extends AppCompatActivity {
    ViewPager slideImages;
    List<String> urlsImages = new ArrayList<>();
    ProgressBar progressBar;
    List<CardImages> imagesList = new ArrayList<>();

    int currentPage = 0;
    Timer timer;
    String name;
    final long DELAY_MS = 500;
    final long PERIOD_MS = 3000;
    List<Card> list = new ArrayList<>();
    TextView title,titleSets,txtATK,txtDEF,desc,type,typing,archetype,amazon_price,cardmarket_price,coolstuffinc_price,ebay_price,tcgplayer_price;
    RatingBar ratingBar;
    Context context;
    Button irUrl;
    RecyclerView sets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_card);
        context = this;
        name = getIntent().getStringExtra("name");
        slideImages = findViewById(R.id.slideImages);
        progressBar = findViewById(R.id.progressBar);
        irUrl = findViewById(R.id.irUrl);
        titleSets = findViewById(R.id.titleSets);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        txtATK = findViewById(R.id.txtAkt);
        txtDEF = findViewById(R.id.txtDef);
        type = findViewById(R.id.type);
        typing = findViewById(R.id.typing);
        archetype = findViewById(R.id.archetype);
        amazon_price = findViewById(R.id.amazon_price);
        cardmarket_price = findViewById(R.id.cardmarket_price);
        coolstuffinc_price = findViewById(R.id.coolstuffinc_price);
        ebay_price = findViewById(R.id.ebay_price);
        tcgplayer_price = findViewById(R.id.tcgplayer_price);
        sets = findViewById(R.id.sets);

        LinearLayoutManager Vertical = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        sets.setLayoutManager(Vertical);
        sets.setHasFixedSize(true);

        ratingBar = findViewById(R.id.simpleRatingBar);
        ratingBar.setIsIndicator(true);

        if (Utils.isConnectInternet(context)) {
            setDataServer();
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
            setDataServer();
        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    void setDataServer(){
        showLoading();
        list.clear();
        String url = getString(R.string.url) + "cardinfo.php?name=" + name;
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

            @SuppressLint("SetTextI18n")
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
                                    type.setText("Type: "+card.getType());
                                    typing.setText("typing: "+card.getFrameType());
                                    title.setText(card.getName());
                                    if (row.has("archetype")){
                                        card.setArchetype(row.getString("archetype"));
                                        archetype.setText("Archetype: "+card.getArchetype());
                                    } else{
                                        archetype.setVisibility(View.GONE);
                                    }
                                    card.setDesc(row.getString("desc"));
                                    desc.setText(card.getDesc());
                                    card.setYgoprodeck_url(row.getString("ygoprodeck_url"));
                                    irUrl.setOnClickListener(v -> {
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(card.getYgoprodeck_url()));
                                        startActivity(browserIntent);
                                    });

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
                                        sets.setAdapter(new SetsAdapter(context,card.getCard_sets()));
                                    } else {
                                        sets.setVisibility(View.GONE);
                                        titleSets.setVisibility(View.GONE);
                                    }



                                    JSONArray imagesArray = row.getJSONArray("card_images");
                                    imagesList = new ArrayList<>();
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
                                    ImagesAdapter adapter = new ImagesAdapter(context,imagesList);
                                    slideImages.setAdapter(adapter);
                                    setAutoScroll();

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

                                        amazon_price.setText("$ "+prices.getAmazon_price());
                                        cardmarket_price.setText("$ "+prices.getCardmarket_price());
                                        coolstuffinc_price.setText("$ "+prices.getCoolstuffinc_price());
                                        ebay_price.setText("$ "+prices.getEbay_price());
                                        tcgplayer_price.setText("$ "+prices.getTcgplayer_price());

                                    }
                                    card.setCard_prices(pricesList);
                                    if (row.has("atk")) {
                                        card.setAtk(row.getInt("atk"));
                                        txtATK.setText("ATK "+card.getAtk());
                                    } else {
                                        txtATK.setVisibility(View.GONE);
                                    }
                                    if (row.has("def")) {
                                        card.setDef(row.getInt("def"));
                                        txtDEF.setText("DEF "+card.getDef());
                                    } else {
                                        txtDEF.setVisibility(View.GONE);
                                    }
                                    if (row.has("level")) {
                                        card.setLevel(row.getInt("level"));
                                        ratingBar.setRating((float) card.getLevel());
                                    } else  {
                                        ratingBar.setVisibility(View.GONE);
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


                            } catch (JSONException e) {
                                AppExecutors.getInstance().mainThread().execute(() -> {
                                    hideLoading();
                                    Utils.alertInWorking(context);
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

    public void setAutoScroll(){
        final Handler handler = new Handler();
        final Runnable Update = () -> {

            if (currentPage == imagesList.size()) {
                currentPage = 0;
            }
            slideImages.setCurrentItem(currentPage++, true);
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void hideLoading(){
        progressBar.setVisibility(View.GONE);
    }
}