package com.ourapp.booker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ResultsPage extends Activity {
    
	public ResultsData[] stockArr;
	 String response;
	 String build;
	 public Context localContext;
	 public ProgressDialog progress;
	 public searchupdates task;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);
        localContext=this;
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.setTitle("Getting Results");
        progress.setMessage("Hold on while we get the best books for your query....");
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
               
                 
               
        
        
        Intent intent = getIntent();
       	String url = "https://www.googleapis.com/books/v1/volumes?q="+intent.getStringExtra("query");
       	
       	task = new searchupdates();
        task.execute(new String[] {url});

    }
   
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            // start new Activity here
			finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
private class searchupdates extends AsyncTask<String, Void, String>{

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
            	//Toast.makeText(getApplicationContext(),"Unable to get data. Please check your data connection!", Toast.LENGTH_LONG).show();
            }
          }
          //Log.d("Response",response);
          return response;
	}

	
	@Override
     protected void onPostExecute(String result) {
		try
	    {
				JSONObject total = new JSONObject(result);
				if (total.getString("totalItems")!="0"){
					//start of results more than 0
					JSONArray books = total.getJSONArray("items");
					ArrayList<ResultsData> newArray = new ArrayList<ResultsData>();
					for(int i=0;i<books.length();i++)
	   				{	//start of loop of each book
						
						String title="", author="",publisher="",date="",description="",isbn="",pages="",thumbnail="",snippet="";
			    		
						JSONArray authors=null,ids = null;
						JSONObject volumeinfo=null, searchinfo=null, singlebook = null, booksisbn = null,images=null;
						singlebook = books.getJSONObject(i);
						if (!singlebook.isNull("volumeInfo"))
		   					volumeinfo = singlebook.getJSONObject("volumeInfo");
						
						if (!singlebook.isNull("searchInfo"))
		   					searchinfo = singlebook.getJSONObject("searchInfo");
						if (searchinfo!=null && !searchinfo.isNull("textSnippet"))
	   						snippet=searchinfo.getString("textSnippet");
	   					else snippet="";
	   					
						if (!volumeinfo.isNull("title"))
	   						title=volumeinfo.getString("title");
	   					else title="-NA-";
	   					
	   					if (!volumeinfo.isNull("publisher"))
	   						publisher=volumeinfo.getString("publisher");
	   					else publisher="-NA-";
	   					
	   					if (!volumeinfo.isNull("publishedDate"))
	   						date=volumeinfo.getString("publishedDate");
	   					else date="-NA-";
	   					
	   					if (!volumeinfo.isNull("description"))
	   						description=volumeinfo.getString("description");
	   					else if(snippet.length()!=0)
	   						description=snippet;
	   					else description="-NA-";
	   					
	   					if (description.length()>200) 
	   					{description=description.substring(0, 200);
	   					description+="...";}
	   					
	   					if (!volumeinfo.isNull("pageCount"))
	   						pages=volumeinfo.getString("pageCount");
	   					else pages="-NA-";
	   					
	   					
	   					
	   					
	   					if (!volumeinfo.isNull("industryIdentifiers"))
	   						
	   					{	ids=volumeinfo.getJSONArray("industryIdentifiers");
	   						
	   						for (int j=0;j<ids.length();j++){
	   						booksisbn = ids.getJSONObject(j);
	   						
	   						if (booksisbn.getString("type").indexOf("ISBN")!=-1)
	   						{
	   							isbn = booksisbn.getString("identifier");
	   							
	   						}
	   							
	   						}
	   							
	   					}
	   					
	   					
	   					if (!volumeinfo.isNull("authors"))
	   					{	authors=volumeinfo.getJSONArray("authors");
	   						for (int k=0;k<authors.length();k++)
	   							author += authors.getString(k)+", ";
	   						StringBuilder b = new StringBuilder(author);
	   						b.replace(author.lastIndexOf(", "), author.lastIndexOf(", ") + 1, "" );
	   						author = b.toString();
	   					}
	   					else author="-NA-";
	   					
	   					if (!volumeinfo.isNull("imageLinks")){
	   						images=volumeinfo.getJSONObject("imageLinks");
	   						if (!images.isNull("thumbnail"))
	   							thumbnail=images.getString("thumbnail");
	   					}
	   					
	   					ResultsData name = new ResultsData (title, author,publisher,	date,description,isbn,pages,thumbnail);
	   					
	   					newArray.add(name);
	   						//end of loop for each element
	   				}
	   				stockArr = new ResultsData[newArray.size()];
	   			    stockArr = newArray.toArray(stockArr);
	   			    View header = (View)getLayoutInflater().inflate(R.layout.results_listview_header, null);
	   			    ListView listView = (ListView) findViewById(R.id.mylist);
	   			    ResultsAdapter adapter = new ResultsAdapter(localContext,R.layout.results_listview_item_row, stockArr);
	   			    listView.addHeaderView(header);
	   			    listView.setAdapter(adapter); 
	   				listView.setOnItemClickListener(new OnItemClickListener() {
	   					  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	   						  String isbn = stockArr[position-1].isbn;
	   						
	   						  if (isbn=="") 
	   							Toast.makeText(getApplicationContext(),"This book does not have an ISBN. Try again.", Toast.LENGTH_LONG).show();
	   		 	        	 else{
	   		 	        		 	 
	   							Intent myIntent1 = new Intent(ResultsPage.this, Details.class);
	   							myIntent1.putExtra("ISBN", isbn);
	   							ResultsPage.this.startActivity(myIntent1);
	   		 	        	  }
	   					  }
	   					});
	   				progress.dismiss();
	   				//end of results more than 0
				}
					
	        	
				 else {Toast.makeText(getApplicationContext(),"Your search returned 0 results. Please try again", Toast.LENGTH_LONG).show();
				 	finish();
				 }
			    
	        
	        	//end of results block
	        }
	        
	 	catch(Exception e){
	 		//Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_LONG).show();
	    	
	      	Toast.makeText(getApplicationContext(),"Your net connection is not working properly. Please try again", Toast.LENGTH_LONG).show();
	      	finish();
	 	}	
	
	}
	
}

 }
    
    
    