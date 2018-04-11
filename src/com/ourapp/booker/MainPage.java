package com.ourapp.booker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

//Starting page of the App

public class MainPage extends Activity{
	AutoCompleteTextView ISBN;
	public Button b1;
	public RadioButton radioISBN,rtitle,rauthor;
	public Context testvar;
	public String feedback_string=""; 
	public String[] history;
	private static final int DLG_EXAMPLE1 = 0;
	public ProgressDialog progress;
	public feedback_sender task;
	public ArrayAdapter<String> adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        testvar=this;
        radioISBN = (RadioButton)findViewById(R.id.radioISBN);
        rtitle = (RadioButton)findViewById(R.id.rtitle);
        rauthor = (RadioButton)findViewById(R.id.rauthor);
        ISBN= (AutoCompleteTextView) findViewById(R.id.EditText01);
        history=null;
        
        try{
        	  history= loadArray("history", this);  
        }	  
        catch(Exception e){}
        if (history!=null){
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,history);
        ISBN.setThreshold(1);
        ISBN.setAdapter(adapter);}
        
        progress = new ProgressDialog(this);
        progress.setIndeterminate(true);
        progress.setCancelable(true);
        progress.setTitle("Submitting feedback");
        progress.setMessage("Please wait while we record your feedback");
        progress.setButton(DialogInterface.BUTTON_NEGATIVE,"Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	task.cancel(true);
                dialog.dismiss();
            }
        }	);
        
        progress.setOnCancelListener(new OnCancelListener() {
            
            public void onCancel(DialogInterface dialog) {
            	task.cancel(true);
                dialog.dismiss();
            }
        });
              
        b1= (Button) findViewById(R.id.Button01);
        Button b2= (Button) findViewById(R.id.Button02);
        Button b3= (Button) findViewById(R.id.Button03);
        b1.setOnClickListener(onClickListener1);
        b2.setOnClickListener(onClickListener2);        
        b3.setOnClickListener(onClickListener3);     
        
        
		}            
    
    String[] addElement(String[] org, String added) {
        String[] result=null;
    	try{
    	result =  new String[org.length+1];}
        catch (Exception e) {return org;}
        System.arraycopy(org, 0, result, 0, org.length);
        result[org.length] = added;
        return result;
    }
    
    @Override
    protected void onResume(){
        super.onPause();
        history=null;
        try{
        history= loadArray("history", testvar);}
        catch(Exception e){}
        
        if (history!=null){
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,history);
        ISBN.setAdapter(adapter);}
    }
    
    @Override
    protected void onPause(){
        super.onPause();
        saveArray(history, "history", testvar);  
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    
    public boolean saveArray(String[] array, String arrayName, Context mContext) {   
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);  
        SharedPreferences.Editor editor = prefs.edit();
        int length;
        if (array == null) length=0;
        else length = array.length;
        editor.putInt(arrayName +"_size", length);  
        for(int i=0;i<length;i++)  
            editor.putString(arrayName + "_" + i, array[i]);  
        return editor.commit();  
    } 
    public String[] loadArray(String arrayName, Context mContext) {  
        SharedPreferences prefs = mContext.getSharedPreferences("preferencename", 0);  
        int size = prefs.getInt(arrayName + "_size", 0);  
        String array[] = new String[size];  
        for(int i=0;i<size;i++)  
            array[i] = prefs.getString(arrayName + "_" + i, null);  
        return array;  
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.about:
        	AlertDialog.Builder builder = new AlertDialog.Builder(testvar);
        	builder.setMessage("App made by Vaishnavi Paladugu, Ravali Nimmagadda and Vangmai Gadde.\n\nWe support Flipkart, Infibeam, BookAdda, BuyBooksIndia, uRead, Landmark and HomeShop18. To add more sites, use the Feedback option to send in your request.").setTitle("About");
        	builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                	dialog.dismiss();
                }
            });
        	builder.setCancelable(true);
        	AlertDialog dialog = builder.create();
        	dialog.show();
            return true;
        case R.id.feedback:
        	//Toast.makeText(this, "Feedback", Toast.LENGTH_LONG).show();
        	 showDialog(DLG_EXAMPLE1);
            return true;
        case R.id.history:
        	saveArray(null, "history", testvar);
        	history=new String[0];
        	adapter = new ArrayAdapter<String>(testvar,android.R.layout.simple_dropdown_item_1line,history);
            ISBN.setAdapter(adapter);
        	Toast.makeText(this, "Cleared all history!", Toast.LENGTH_LONG).show();
            return true;
        
        default:
            return super.onOptionsItemSelected(item);
        }
	
    }
    
    @Override
    protected Dialog onCreateDialog(int id) {
 
        switch (id) {
            case DLG_EXAMPLE1:
                return createExampleDialog();
            default:
                return null;
        }
    }
 
    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
 
        switch (id) {
            case DLG_EXAMPLE1:
                // Clear the input box.
                EditText text = (EditText) dialog.findViewById(43);
                text.setText("");
                break;
        }
    }
    
    private Dialog createExampleDialog() {
    	 
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Feedback");
        builder.setMessage("Type in your valuable feedback!");
        builder.setCancelable(true);
 
         // Use an EditText view to get user input.
         final EditText input = new EditText(this);
         input.setLines(3);
         input.setId(43);
         builder.setView(input);
 
        builder.setPositiveButton("Send", new DialogInterface.OnClickListener() {
 
            
            public void onClick(DialogInterface dialog, int whichButton) {
            	feedback_string = URLEncoder.encode(input.getText().toString());
            	if (feedback_string.length()>0){
            		final ConnectivityManager conMgr =  (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                    final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
                    if (activeNetwork != null && activeNetwork.isConnected()) {
            		
            		task = new feedback_sender();
            		task.execute(new String[] {"http://vaish.22web.org/app/feedback.php?feedback="+feedback_string+"&make="+URLEncoder.encode("Brand:"+android.os.Build.BRAND+", Manufacturer:"+android.os.Build.MANUFACTURER+", Model:"+android.os.Build.MODEL+", SDK: "+android.os.Build.VERSION.SDK_INT+", OS: "+android.os.Build.VERSION.RELEASE)});
            		progress.show();
                    }
                    else
                    	Toast.makeText(testvar, "You don't have an internet connection. Please enable it and try again!!", Toast.LENGTH_LONG).show();
                    
                return;
                }
                else 
                	Toast.makeText(testvar, "Enter some text to send!", Toast.LENGTH_LONG).show();
                
            }
        });
 
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
 
            
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
 
        return builder.create();
    }
    
    private OnClickListener onClickListener1 = new OnClickListener() {
        public void onClick(View arg0) {
			// TODO Auto-generated method stub
        	
        	if (radioISBN.isChecked())
        	{	
        	if (ISBN.length()==0){
				Toast.makeText(getApplicationContext(), "Please enter an ISBN number", Toast.LENGTH_SHORT).show();
			}
			else if(ISBN.length()!=10 && ISBN.length()!=13){
				Toast.makeText(getApplicationContext(), "A valid ISBN has 10 or 13 numbers only!", Toast.LENGTH_SHORT).show();
			}
			else if (!ISBN.getText().toString().matches("^[0-9]+$"))
				Toast.makeText(getApplicationContext(), "A valid ISBN has 10 or 13 numbers only!", Toast.LENGTH_SHORT).show();
			else{
				InputMethodManager imm = (InputMethodManager)getSystemService(
	        		      Context.INPUT_METHOD_SERVICE);
	        		imm.hideSoftInputFromWindow(ISBN.getWindowToken(), 0);
	        	
				Intent myIntent1 = new Intent(MainPage.this, Details.class);
				myIntent1.putExtra("ISBN", ISBN.getText().toString());
				history = addElement(history, ISBN.getText().toString());
				
				MainPage.this.startActivity(myIntent1);
			}
        }
        	
        	else if(rtitle.isChecked()){
        		if (ISBN.length()==0)
    				Toast.makeText(getApplicationContext(), "Please enter a title to search for", Toast.LENGTH_SHORT).show();
        		else{
        			
        			Intent myIntent1 = new Intent(MainPage.this, ResultsPage.class);
    				myIntent1.putExtra("query", "+intitle:" + URLEncoder.encode(ISBN.getText().toString()));
    				history = addElement(history, ISBN.getText().toString());
    				MainPage.this.startActivity(myIntent1);
        		}
        	}
        	else {
        		
        		if (ISBN.length()==0)
    				Toast.makeText(getApplicationContext(), "Please enter a title to search for", Toast.LENGTH_SHORT).show();
        		else{
        			
        			Intent myIntent1 = new Intent(MainPage.this, ResultsPage.class);
    				myIntent1.putExtra("query", "+inauthor:" + URLEncoder.encode(ISBN.getText().toString()));
    				history = addElement(history, ISBN.getText().toString());
    				MainPage.this.startActivity(myIntent1);
        		}
        	}
        	
		}
    };
    private OnClickListener onClickListener2 = new OnClickListener() {
        public void onClick(View arg0) {
			// TODO Auto-generated method stub
        	try{
            	Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "ONE_D_MODE");//for Qr code, its "QR_CODE_MODE" instead of "PRODUCT_MODE"
                intent.putExtra("SAVE_HISTORY", false);//this stops saving ur barcode in barcode scanner app's history
                startActivityForResult(intent, 0);
                }catch(Exception e){
                	
        			
                	AlertDialog.Builder builder = new AlertDialog.Builder(testvar);
                	builder.setCancelable(true);
                	builder.setTitle("Barcode Scanning");
                	builder.setInverseBackgroundForced(true);
                	builder.setMessage("This feature requires ZXing App to be installed on your phone. Click Yes to go to the marketplace to install this App or No to return and enter ISBN manually.");
                	builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                	  
                	  public void onClick(DialogInterface dialog, int which) {
                		  Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                		  Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                		  startActivity(marketIntent);
                	    dialog.dismiss();
                	  }
                	});
                	builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                	  
                	  public void onClick(DialogInterface dialog, int which) {
                	    dialog.dismiss();
                	  }
                	});
                	AlertDialog alert = builder.create();
                	alert.show();

                	
                }
		}
    };
    
    private OnClickListener onClickListener3 = new OnClickListener() {
        public void onClick(View arg0) {
			// TODO Auto-generated method stub
        	
        	
        	Intent myIntent3 = new Intent(MainPage.this, SearchPage.class);
			MainPage.this.startActivity(myIntent3);
		
		}
    };
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			finish();
        }
        return super.onKeyDown(keyCode, event);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                    String contents = data.getStringExtra("SCAN_RESULT"); //this is the result
                    if ( (contents.length()!=10 && contents.length()!=13) || !contents.matches("^[0-9]+$") )
                    	Toast.makeText(getApplicationContext(), "A valid ISBN contains 10 or 13 numbers only.", Toast.LENGTH_LONG).show();
                    else
                    {
                    	Intent myIntent1 = new Intent(MainPage.this, Details.class);
        				myIntent1.putExtra("ISBN", contents);
        				history = addElement(history, contents);
        				saveArray(history, "history", testvar);  
        				MainPage.this.startActivity(myIntent1);
        				
                    }
        			
            } else 
            if (resultCode == RESULT_CANCELED) {
              // Handle cancel
            }
        }
    }
    
    private class feedback_sender extends AsyncTask<String, Void, String>{

		@Override
		protected String doInBackground(String... arg0) {
			String response = "";
		
			try{
				HttpClient client = new DefaultHttpClient();  
                HttpGet get = new HttpGet(arg0[0]);
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
       	        return response;
       	        }
			catch(Exception e){}
			return null;
		}
    	
		@Override
        protected void onPostExecute(String result) {
			if (result!=null && result.indexOf("OK")!=-1)
					Toast.makeText(testvar, "Submitted feedback successfully. Thank you!", Toast.LENGTH_LONG).show();
			else
					Toast.makeText(testvar, "Error in submitting feedback. Try again!", Toast.LENGTH_LONG).show();
			        	
			progress.dismiss();
		}
    	
    }
}