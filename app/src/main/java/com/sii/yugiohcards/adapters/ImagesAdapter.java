package com.sii.yugiohcards.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.sii.yugiohcards.R;
import com.sii.yugiohcards.objects.Card;
import com.sii.yugiohcards.objects.CardImages;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImagesAdapter extends PagerAdapter {

    private static final String TAG = "ImageViewPage";
    Context mContext;
    LayoutInflater mLayoutInflater;

    List<CardImages> imagelist;

    public ImagesAdapter(Context context, List<CardImages> imagelist) {
        mContext = context;
        this.imagelist = imagelist;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imagelist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        CardImages urlimage = imagelist.get(position);
        View itemView = mLayoutInflater.inflate(R.layout.item_images, container, false);
        ImageView imageView =  itemView.findViewById(R.id.iv_photo);
        if (!urlimage.getImage_url().isEmpty() ) {
            Picasso.get().load(urlimage.getImage_url()).into(imageView);
        }
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d(TAG, "destroyItem() called with: " + "container = [" + container + "], position = [" + position + "], object = [" + object + "]");
        // container.removeView((LinearLayout) object);
    }
}