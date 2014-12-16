package com.bde.lpsmin.bdemmi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lheido on 15/12/14.
 */
public class GalleryListViewAdapter extends BaseAdapter{
    private final ArrayList<Gallerie> items;
    private final Context context;

    public GalleryListViewAdapter(Context applicationContext, ArrayList<Gallerie> items) {
        this.context = applicationContext;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Gallerie getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int r, View convertView, ViewGroup parent) {
        Gallerie item = this.getItem(r);
        ViewHolder holder;
        holder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        holder.title = (TextView) convertView.findViewById(R.id.item_title);
        holder.image = (ImageView) convertView.findViewById(R.id.item_image);
        convertView.setTag(holder);

        holder.title.setText(item.getTitle());

        if(!item.getImages().isEmpty()){
            Picasso.with(context)
                    .load(item.getImages().get(0).getUrl())
                    .centerCrop()
                    .fit()
                    .into(holder.image);
        }
        return convertView;
    }

    private static class ViewHolder {
        public TextView title;
        public ImageView image;
    }

}
