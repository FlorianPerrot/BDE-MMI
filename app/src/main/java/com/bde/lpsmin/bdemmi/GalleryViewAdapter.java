package com.bde.lpsmin.bdemmi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lheido on 16/12/14.
 */
public class GalleryViewAdapter extends BaseAdapter{
    private ArrayList<Gallerie.Image> items;
    private Context context;

    public GalleryViewAdapter(Context applicationContext, ArrayList<Gallerie.Image> items) {
        this.context = applicationContext;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Gallerie.Image getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public View getView(int r, View convertView, ViewGroup parent) {
        Gallerie.Image item = this.getItem(r);
        ViewHolder holder;
        holder = new ViewHolder();
        convertView = LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false);
        holder.title = (TextView) convertView.findViewById(R.id.item_title);
        holder.image = (ImageView) convertView.findViewById(R.id.item_image);
        convertView.setTag(holder);

        holder.title.setText(item.getTitle());

            Picasso.with(context)
                    .load(item.getUrl())
                    .centerCrop()
                    .fit()
                    .into(holder.image);
        return convertView;
    }

    private static class ViewHolder {
        public TextView title;
        public ImageView image;
    }
}
