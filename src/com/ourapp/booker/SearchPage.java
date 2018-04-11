package com.ourapp.booker;

import java.net.URLEncoder;

import com.ourapp.booker.R;

import android.app.Activity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Class to display page to search for books.

public class SearchPage extends Activity {
       EditText title ,author,publisher,subject;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Button b1= (Button) findViewById(R.id.Button01);
        b1.setOnClickListener(onClickListener1);
        title = (EditText) findViewById(R.id.title);
        author = (EditText) findViewById(R.id.author);
        publisher = (EditText) findViewById(R.id.publisher);
        subject = (EditText) findViewById(R.id.subject);

    }
    
    //If back key is pressed, return to main activity
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) finish();
        return super.onKeyDown(keyCode, event);
    }
    
    //Get results on hitting search key
    private OnClickListener onClickListener1 = new OnClickListener() {
        public void onClick(View arg0) {
        	ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnected()) {
            	
            
        	String obj="";
			
			 if (title.length() != 0)
	                obj += "+intitle:" + URLEncoder.encode(title.getText().toString());

	            if (author.length() != 0)
	                obj += "+inauthor:" + URLEncoder.encode(author.getText().toString());

	            if (publisher.length() != 0)
	                obj += "+inpublisher:" + URLEncoder.encode(publisher.getText().toString());

	            if (subject.length() != 0)
	                obj += "+subject:" + URLEncoder.encode(subject.getText().toString());


	            if (obj.length() == 0)
	            	Toast.makeText(getApplicationContext(), "Please enter atleast one parameter", Toast.LENGTH_SHORT).show();
	    		
			else{
				Intent myIntent1 = new Intent(SearchPage.this, ResultsPage.class);
				myIntent1.putExtra("query", obj);
				SearchPage.this.startActivity(myIntent1);
			}}
            else{
            	Toast.makeText(getApplicationContext(), "You don't have a working internet connection. Please try again!", Toast.LENGTH_SHORT).show();
	    		
            }
            
		}
    };
}