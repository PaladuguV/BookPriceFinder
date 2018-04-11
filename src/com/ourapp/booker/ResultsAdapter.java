package com.ourapp.booker;

import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//Class to display the results returned by Google in a listview

public class ResultsAdapter extends ArrayAdapter<ResultsData>{

    Context context; 
    int layoutResourceId;    
    ResultsData dataarray[] = null;
    
    public ResultsAdapter(Context context, int layoutResourceId, ResultsData[] dataarray) {
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
            holder.title = (TextView)row.findViewById(R.id.title);
            holder.author = (TextView)row.findViewById(R.id.author);
            holder.publisher = (TextView)row.findViewById(R.id.publisher);
            holder.date = (TextView)row.findViewById(R.id.date);
            holder.pages = (TextView)row.findViewById(R.id.pages);
            holder.description = (TextView)row.findViewById(R.id.description);
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            
            row.setTag(holder);
        }
        else
        {
            holder = (WeatherHolder)row.getTag();
        }
        
        ResultsData dataitem = dataarray[position];
        holder.title.setText(dataitem.title);
        holder.author.setText("Author(s): "+dataitem.author);
        holder.publisher.setText("Publisher: "+dataitem.publisher);
        holder.date.setText("Publish Date: "+dataitem.date);
        holder.pages.setText("Page Count: "+dataitem.pages);
        holder.description.setText("Description: "+dataitem.description);
        
        if (dataitem.thumbnail != ""){
        try{
        	Ops passer = new Ops();
        	passer.holder = holder;
        	passer.url = dataitem.thumbnail;
        	imaggetter task = new imaggetter();
        	task.execute(passer);
				}
        catch(Exception e){}
        }
        
        
        return row;
    }
    
    
    static class WeatherHolder
    {
        ImageView imgIcon;
        TextView title;
        TextView author;
        TextView description;
        TextView pages;
        TextView publisher;
        TextView date;
    
    }
    
    static class Ops{
    	String url;
    	WeatherHolder holder;
    }
    
    
    private class imaggetter extends AsyncTask<Ops, Void, Drawable>{

		@Override
		protected Drawable doInBackground(Ops... arg0) {
			// TODO Auto-generated method stub
			Drawable thumb_d;
			try{
			URL thumb_u = new URL(arg0[0].url);
			thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
			arg0[0].holder.imgIcon.setImageDrawable(thumb_d);
			}catch(Exception e){return null;}
			
			return thumb_d;
		}
    	
		@Override
	     protected void onPostExecute(Drawable result) {
			
		}
    }
}
