package com.bde.lpsmin.bdemmi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by lheido on 26/09/14.
 */
public class ListViewAdapter extends BaseAdapter {

    private ArrayList<Actu> items;
    private Context context;

    public ListViewAdapter(Context context, ArrayList<Actu> items) {
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

    @Override
    public Actu getItem(int i) {
        return this.items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int r, View convertView, ViewGroup parent) {
        Actu item = this.getItem(r);
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.item_title);
            holder.date = (TextView) convertView.findViewById(R.id.item_date);
            holder.lieu = (TextView) convertView.findViewById(R.id.item_lieu);
            holder.description = (TextView) convertView.findViewById(R.id.item_description);
            holder.image = (ImageView) convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder) convertView.getTag();

        holder.title.setText(item.getTitle());
        if(item instanceof Event){
            holder.lieu.setBackgroundResource(R.drawable.place_background_selector);
            final String lieu = ((Event) item).getLieu();
//            final String lieu = "Parc Paul Mistral Grenoble";
            holder.lieu.setText(lieu);
            if(lieu != null && !lieu.equals("")) {
                holder.lieu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String uri = String.format(Locale.FRANCE, "geo:0,0?q=" + lieu);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }catch (Exception e){e.printStackTrace();}
                    }
                });
            }else{
                holder.lieu.setBackgroundResource(R.drawable.without_place_background_selector);
            }
        }else{
            holder.lieu.setText("");
            holder.lieu.setBackgroundResource(R.drawable.without_place_background_selector);
        }

        holder.date.setText(item.getDateFormated());

        holder.description.setText(item.getContent());
        try {
            if (item.getImgUri() != null && !item.getImgUri().equals("")) {
                Picasso.with(context).load(item.getImgUri()).fit().centerCrop().into(holder.image);
            }else {
                Picasso.with(context).load(R.drawable.img_placeholder).fit().centerCrop().into(holder.image);
            }
        }catch (Exception e){e.printStackTrace();}

        return convertView;
    }

    private static class ViewHolder {
        public TextView title;
        public TextView description;
        public TextView lieu;
        public TextView date;
        public ImageView image;
    }
}
