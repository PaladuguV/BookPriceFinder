package com.ourapp.booker;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//Class to display store details of each book

public class ListAdapter extends ArrayAdapter<StoreData>{

    Context context; 
    int layoutResourceId;    
    StoreData dataarray[] = null;
    
    public ListAdapter(Context context, int layoutResourceId, StoreData[] dataarray) {
        super(context, layoutResourceId, dataarray);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.dataarray = dataarray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        WeatherHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new WeatherHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            holder.price = (TextView)row.findViewById(R.id.price);
            
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }
        
        StoreData dataitem = dataarray[position];
        holder.txtTitle.setText(dataitem.store);
        holder.price.setText("Rs. "+dataitem.price);
        
        if (dataitem.store.indexOf("flipkart")!=-1)
        holder.imgIcon.setImageResource(R.drawable.flipkart);
        else if (dataitem.store.indexOf("bookadda")!=-1)
        	holder.imgIcon.setImageResource(R.drawable.bookadda);
        else if (dataitem.store.indexOf("infibeam")!=-1)
        	holder.imgIcon.setImageResource(R.drawable.infibeam);
        else if (dataitem.store.indexOf("homeshop18")!=-1)
        	holder.imgIcon.setImageResource(R.drawable.homeshop18);
        else if (dataitem.store.indexOf("buybooksindia")!=-1)
        	holder.imgIcon.setImageResource(R.drawable.buybooksindia);
        else if (dataitem.store.indexOf("landmark")!=-1)
        	holder.imgIcon.setImageResource(R.drawable.landmark);
        else if (dataitem.store.indexOf("uread")!=-1)
        	holder.imgIcon.setImageResource(R.drawable.uread);
        
        else
        	holder.imgIcon.setImageResource(R.drawable.icon);
        	
        
        
        
        return row;
    }
    
    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
        TextView price;
    }
}
