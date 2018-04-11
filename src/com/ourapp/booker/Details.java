package com.ourapp.booker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class Details extends Activity {
    /** Called when the activity is first created. */
	 public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
	 public String texttosend;
	 public TextView title,author,publisher,year ;
	 public RatingBar rating;
	 public LinearLayout rater,root;
	 public StoreData[] stockArr;
	 public ProgressDialog progress;
	 public Context localContext;
	 public jsonupdater task;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.details);

        root = (LinearLayout) findViewById(R.id.rootlayout);
        root.setVisibility(View.INVISIBLE);
        Button b1= (Button) findViewById(R.id.Button01);
        b1.setOnClickListener(onClickListener1);
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.setTitle("Fetching Prices");
        progress.setMessage("Hold on while we get the best prices for your book....");
        progress.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	task.cancel(true);
                dialog.dismiss();
                finish();
            }
        }	);
        
        progress.setOnCancelListener(new OnCancelListener() {
            
            public void onCancel(DialogInterface dialog) {
            	task.cancel(true);
                dialog.dismiss();
                finish();
            }
        });
      
        
        progress.show();
    	localContext=this;
        Button b2= (Button) findViewById(R.id.Button02);
        b2.setOnClickListener(onClickListener2);
        
        title= (TextView) findViewById(R.id.title);
        author= (TextView) findViewById(R.id.author);
        publisher= (TextView) findViewById(R.id.publisher);
        year= (TextView) findViewById(R.id.year);
        
        final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
        	
        	Intent intent = getIntent();
        	String url = "http://vaish.22web.org/app/?isbn="+intent.getStringExtra("ISBN");
        	task = new jsonupdater();
            task.execute(new String[] {url});
        } 
        else {
        	Toast.makeText(getApplicationContext(), "You don't have a working internet connection. Please enable it and try again", Toast.LENGTH_LONG).show();
        	finish();
        } 
          
           
    }
   
    private OnClickListener onClickListener1 = new OnClickListener() {
        public void onClick(View arg0) {
			// TODO Auto-generated method stub
        	Calendar cal = Calendar.getInstance();              
        	Intent intent = new Intent(Intent.ACTION_EDIT);
        	intent.setType("vnd.android.cursor.item/event");
        	intent.putExtra("beginTime", cal.getTimeInMillis()+24*60*60*1000);
        	intent.putExtra("allDay", true);
        	intent.putExtra("endTime", cal.getTimeInMillis()+24*60*60*1000);
        	intent.putExtra("title", "Buy "+title.getText().toString()+" by "+author.getText().toString());
        	startActivity(intent);
		
		}
    };
    
    private OnClickListener onClickListener2 = new OnClickListener() {
        public void onClick(View arg0) {
			// TODO Auto-generated method stub
        	Intent sendIntent = new Intent();
        	sendIntent.setAction(Intent.ACTION_SEND);
        	sendIntent.putExtra(Intent.EXTRA_TEXT, texttosend);
        	sendIntent.setType("text/plain");
        	startActivity(Intent.createChooser(sendIntent, "check"));
		
		}
    };
    
    
    
    private class jsonupdater extends AsyncTask<String, Void, String>{

	
    	
    	@Override
		protected String doInBackground(String... urls) {
    		  String response = "";
              for (String url : urls) {
                try {
                    HttpClient client = new DefaultHttpClient();  
                    HttpGet get = new HttpGet(url);
           	        HttpResponse responseGet = client.execute(get);  
           	        HttpEntity resEntityGet = responseGet.getEntity();  
           	        
           	        if (resEntityGet != null) {
           				InputStream instream = resEntityGet.getContent();
           				BufferedReader str = new BufferedReader(new InputStreamReader(
           						instream));
           	
           				String ans = new String("");
           				while ((ans = str.readLine()) != null) 
           					response = response + ans;
           				
           	        }

                } catch (Exception e) {
                }
              }
              Log.d("Response",response);
              return response;
		}

    	
    	 @Override
         protected void onPostExecute(String result) {
    		 
    		 try{
    				if (result.indexOf("Invalid ISBN")!=-1)
    	   				 throw new Exception("Invalid ISBN");
    	   					
    	   				JSONObject total = new JSONObject(result);
    	   				
    	   				if (total.isNull("Title"))
       	   				 throw new Exception("No Data");
    	   				title.setText(total.getString("Title"));
    	   				
    	   				if (!total.isNull("Author"))
    	   					author.setText("Author: "+total.getString("Author"));
    	   				
    	   				if (!total.isNull("Publisher"))
    	   					publisher.setText("Publisher: "+total.getString("Publisher"));
    	   				
    	   				if (!total.isNull("Year"))
        	   				year.setText("Year: "+total.getString("Year"));
    	   				
    	   				if (!total.isNull("rating")){
    	   					rating.setRating((float)total.getDouble("rating"));
    	   					rater.setVisibility(View.VISIBLE);}
    	   				
    	   				JSONArray Prices = total.getJSONArray("PriceList");
    	   				ArrayList<StoreData> newArray = new ArrayList<StoreData>();
    	   				
    	   				for(int i=0;i<Prices.length();i++)
    	   				{
    	   					JSONObject price = null;
    	   					price = Prices.getJSONObject(i);
    	   					StoreData name = new StoreData (price.getString("StoreName"),price.getString("price"),price.getString("Link"));
    	   					newArray.add(name);
    	   				}
    	   				stockArr = new StoreData[newArray.size()];
    	   			    stockArr = newArray.toArray(stockArr);
    	   			    View header = (View)getLayoutInflater().inflate(R.layout.listview_header, null);
    	   	        	ListView listView = (ListView) findViewById(R.id.mylist);
    	   	        	ListAdapter adapter = new ListAdapter(localContext,R.layout.listview_item_row, stockArr);
    	   	        	listView.addHeaderView(header);
    	   				listView.setAdapter(adapter); 
    	   				listView.setOnItemClickListener(new OnItemClickListener() {
    	   					  public void onItemClick(AdapterView<?> parent, View view,
    	   					    int position, long id) {
    	   						  
    	   						  String url = stockArr[position-1].url;
    	   						Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    	   						startActivity(browserIntent);
    	   					    }
    	   					});
    	   					
	   					imager task = new imager();
	   					task.execute(total.getString("ImageUrl"));
    	   					
    	   					
    	   				texttosend = ""+title.getText().toString()+" by "+author.getText().toString().replace("Author: ", "")+" is available for Rs."+stockArr[0].price+" at "+stockArr[0].store;

    	   				progress.dismiss();
    	   				root.setVisibility(View.VISIBLE);
    		 }
    		 catch(Exception e){
    			 
    				//Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
            			
            		if (e.toString().indexOf("Invalid ISBN")!=-1)
                  		Toast.makeText(getApplicationContext(),"This not a valid ISBN", Toast.LENGTH_LONG).show();
            		else if (e.toString().indexOf("No Data")!=-1)
            	  		Toast.makeText(getApplicationContext(),"No data for this book. Try another one.", Toast.LENGTH_LONG).show();
                	else
                		Toast.makeText(getApplicationContext(),"Your net connection is not working properly. Please try again", Toast.LENGTH_LONG).show();
            		finish();

    		 }
    	 }
    
    }
    
    
    private class imager extends AsyncTask<String, Void, Drawable>{

		@Override
		protected Drawable doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			Drawable thumb_d=null;
			try{
					URL thumb_u = new URL(arg0[0]);
					thumb_d = Drawable.createFromStream(thumb_u.openStream(), "src");
					}
					catch(Exception e)
					{
						thumb_d=null;
					}
			return thumb_d;
		}
    	
		 @Override
         protected void onPostExecute(Drawable result) {
			 ImageView cover = (ImageView)findViewById(R.id.bookcover);
			 if (result!=null)
			 cover.setImageDrawable(result);
			 else
				cover.setImageResource(R.drawable.book);
			 
		 }
    }
    
    
 }
    
    
    