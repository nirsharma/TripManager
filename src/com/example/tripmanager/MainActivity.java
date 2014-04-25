package com.example.tripmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {

private static List<String> names;
private static HashMap<String, Integer> details;
private static String detailing;
	
	static {
		names = new ArrayList<String>();
		details = new HashMap<String, Integer>();
		detailing = new String();
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fetchNames();
        if(names.size() == 0)
        setContentView(R.layout.activity_main);
        else
        	addDetails();
        

        
    }
    
    private void addDetails() {
    	setContentView(R.layout.details);
    	// drop down list updating
    	Spinner dropdown = (Spinner)findViewById(R.id.drop_down);
    	int size = names.size();
    	String[] items = new String[size];
    	
    	for(int i = 0; i< size; i++) {
    		items[i] = names.get(i);
    	}
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
    	dropdown.setAdapter(adapter);
    	
    	updateHashMap();
    	
    	
		
	}
    
    // update hash map at the start up.
    private void updateHashMap() {
		// TODO Auto-generated method stub
    	try {
        	InputStream in = openFileInput("details.data");
        	 
        	if (in != null) {
        	InputStreamReader tmp=new InputStreamReader(in);
        	BufferedReader reader=new BufferedReader(tmp);
        	String str;
        	StringBuilder buf=new StringBuilder();
        	while ((str = reader.readLine()) != null) {
        	detailing = detailing + str + "\n";
        	 String[]parts = str.split(" ");
        	 String name = parts[0];
        	 int moneyValue = Integer.parseInt(parts[1]);
        	 if(details.containsKey(name)) {
         		int value = details.get(name);
         		details.remove(name);
         		details.put(name, value + moneyValue);
         	}
         	else {
         		details.put(name, moneyValue);
         	}
        	}
        	TextView dikhao = (TextView) findViewById(R.id.details_view);
        	dikhao.setText(detailing);
        	in.close();
        	 
        	} 
        	}
        	catch (java.io.FileNotFoundException e) {
        	// that's OK, we probably haven't created it yet
        	}
        	catch (Throwable t) {
        	Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
        	}
		
	}

	// on click submit on details page
    public void onClickAddMoney(View v) {
    	
    	EditText money = (EditText)findViewById(R.id.money);
    	Spinner option = (Spinner)findViewById(R.id.drop_down);
    	
    	String moneyValue_str = money.getText().toString();
    	int moneyValue = Integer.parseInt(moneyValue_str);
    	
    	int index = option.getSelectedItemPosition();
    	
    	 
    	String name = names.get(index);
    	money.setText("");
    	
    	detailing = detailing + name + " " + moneyValue + "\n";
    	TextView dikhao = (TextView) findViewById(R.id.details_view);
    	dikhao.setText(detailing);
    	
    	if(details.containsKey(name)) {
    		int value = details.get(name);
    		details.remove(name);
    		details.put(name, value + moneyValue);
    	}
    	else {
    		details.put(name, moneyValue);
    	}
    	
    	try { 
    		OutputStreamWriter out = new OutputStreamWriter(openFileOutput("details.data", MODE_APPEND));
    		out.write(name + " " + moneyValue + "\n");
    		 
    		out.close();
    		}
        catch(IOException e)
        {
          
        }
    	
    }
    
    public void onClickGenerate(View v) {
    	
    	setContentView(R.layout.result);
    	String final_str = "";
    	int total = 0;
    	Integer[] paise = new Integer[names.size()];
    	
    	for(int i = 0; i < names.size(); i++) {
    		
    		if(details.containsKey(names.get(i)))
    		paise[i] = details.get(names.get(i));
    		else
    			paise[i] = 0;
    		
    		total = total + paise[i];
    	}
    	int total_per_head = total / names.size();
    	
    	for(int i = 0; i < names.size(); i++) {
    		int temp = total_per_head - paise[i];
    		final_str = final_str + names.get(i) + " " + temp + "\n";
    	}
    	
    	TextView toshow = (TextView)findViewById(R.id.result_view_change);
    	toshow.setText(final_str);
    	
    }

	// to fetch all the names already stored
    private void fetchNames() {
    	try {
    	InputStream in = openFileInput("namesFile.data");
    	 
    	if (in != null) {
    	InputStreamReader tmp=new InputStreamReader(in);
    	BufferedReader reader=new BufferedReader(tmp);
    	String str;
    	StringBuilder buf=new StringBuilder();
    	while ((str = reader.readLine()) != null) {
    	 names.add(str);
    	}
    	in.close();
    	 
    	} 
    	}
    	catch (java.io.FileNotFoundException e) {
    	// that's OK, we probably haven't created it yet
    	}
    	catch (Throwable t) {
    	Toast.makeText(this, "Exception: "+t.toString(), Toast.LENGTH_LONG).show();
    	}
	}
    
 public void onClickAddMember(View v) {
   		
	 	StringBuilder stringBuilder = new StringBuilder();
    	EditText et = (EditText) findViewById(R.id.edit_message);
    	String nvalue = et.getText().toString();
    	et.setText("");
    	names.add(nvalue);
    	for(int i = 0; i < names.size(); i++) {
    		stringBuilder.append(i+1 +". " + names.get(i) + "\n" );
    	}
    	
    	String toshow = stringBuilder.toString();
    	
    	TextView show = (TextView) findViewById(R.id.text_view);
    	show.setText(toshow);
   
    	
    }
 
 public void onClickSubmit(View v) {
	 writeToFile();
	 addDetails();
 }

 // To write content to file
private void writeToFile() {
	// TODO Auto-generated method stub
	
		try { 
		OutputStreamWriter out = new OutputStreamWriter(openFileOutput("namesFile.data", 0));
		 for(int i = 0; i < names.size(); i++)
		out.write(names.get(i) + "\n");
		 
		out.close();
		}
		catch (Exception e) {
			// TODO: handle exception
		}
	
}

public void onClickDeleteTrip(View v) {
	setContentView(R.layout.activity_main);
	// clear all data
	names.clear();
	details.clear();
	detailing = "";
	File file1 = new File(getFilesDir(), "namesFile.data");
	File file2 = new File(getFilesDir(), "details.data");
	if(file1.delete() && file2.delete())
		Toast.makeText(this, "Trip deleted.", Toast.LENGTH_LONG).show();
	else
		Toast.makeText(this, "Trip couldn't deleted.", Toast.LENGTH_LONG).show();
}


    

}
