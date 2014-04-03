package cen.comp313.student_portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AddCourse extends ListActivity{

	 // Progress Dialog
    private ProgressDialog pDialog;
    
    String studentID, courseID, cName;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> coursesList;
 
    // url to get all products list
    private static String url_all_courses = "http://teampineapple.host22.com/andriod_connect/get_all_course.php";
    private static String url_add_course = "http://teampineapple.host22.com/andriod_connect/add_course.php";
    private static String url_create_course = "http://teampineapple.host22.com/andriod_connect/create_course.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "courses";
    private static final String TAG_CID = "courseID";
    private static final String TAG_NAME = "courseName";
 
    // products JSONArray
    JSONArray courses = null;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_courses);
        
        studentID = getIntent().getExtras().getString("studentID");
 
        // Hashmap for ListView
        coursesList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
 
        // Get listview
        ListView lv = getListView();
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
               //  getting values from selected ListItem
               courseID = ((TextView) view.findViewById(R.id.cid)).getText().toString();
                 
               new PlusCourse().execute();
            }
        });
 
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.course_createmenu, menu);
		return true;
	}
	
    @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
	      switch (item.getItemId()) 
	    {
	    
	        case R.id.createCourse:
	        	
	        	AlertDialog.Builder alert = new AlertDialog.Builder(this);

	        	alert.setTitle("Title");
	        	alert.setMessage("Message");

	        	// Set an EditText view to get user input 
	        	final EditText input = new EditText(this);
	        	alert.setView(input);

	        	alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dialog, int whichButton) {
	        	  cName = input.getText().toString();
	        	  new CCourse().execute();
	        	  }
	        	});

	        	alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	        	  public void onClick(DialogInterface dialog, int whichButton) {
	        	    // Canceled.
	        	  }
	        	});

	        	alert.show();
	            return true;
	        case R.id.ccMenuBtn:
	        	  Intent i = new Intent(AddCourse.this, Student_Home.class);
		            i.putExtra("studentID", studentID);
		            startActivity(i);
	        	return true;
	        case R.id.cclogOut:
	           	if(Login.currentStudent != null)
	        	{
	        		Login.currentStudent = null;
	        		Intent loginIntent= new Intent(this, Login.class);
	        		startActivity(loginIntent);
	        	}
	        	return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	     } // end switch
	   }
    
    
    
    class CCourse extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddCourse.this);
			pDialog.setMessage("Creating Course. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
    	
    	
    	@Override
		protected String doInBackground(String... arg0) {
    		List<NameValuePair> paramms = new ArrayList<NameValuePair>();
  	        paramms.add(new BasicNameValuePair("courseName", cName));
  	        
  			JSONObject json;
  			
			json = jParser.makeHttpRequest(url_create_course, "POST", paramms);
			
	        // check log cat for response
	        Log.d("Create Response", json.toString());

	        // check for success tag
	        try {
	            int success = json.getInt("success");

	            if (success == 1) { 
	           
	            	
	            } else {
	                // failed to create product
	            	Toast.makeText(getApplicationContext(), "Unable to Add!", Toast.LENGTH_SHORT).show();
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
			return null;
		}
    	
        protected void onPostExecute(String file_url) {
        	Intent i = new Intent(AddCourse.this, AddCourse.class);
            i.putExtra("studentID", studentID);
            pDialog.dismiss();
            startActivity(i);
	    }
    	
    }
    
    
    class PlusCourse extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AddCourse.this);
			pDialog.setMessage("Adding Course. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
    	
		@Override
		protected String doInBackground(String... arg0) {
	        // Building Parameters
	        List<NameValuePair> paramms = new ArrayList<NameValuePair>();
	        paramms.add(new BasicNameValuePair("courseID", courseID));
	        paramms.add(new BasicNameValuePair("studentID", studentID));
	     
			JSONObject json;
		
			json = jParser.makeHttpRequest(url_add_course, "POST", paramms);
			
	        // check log cat for response
	        Log.d("Create Response", json.toString());

	        // check for success tag
	        try {
	            int success = json.getInt("success");

	            if (success == 1) { 
	           
	            	
	            } else {
	                // failed to create product
	            	Toast.makeText(getApplicationContext(), "Unable to Add!", Toast.LENGTH_SHORT).show();
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
					return null;
		}
		
	    protected void onPostExecute(String file_url) {
            Intent i = new Intent(AddCourse.this, AllProductsActivity.class);
             // Closing all previous activities
            i.putExtra("studentID", studentID);
            pDialog.dismiss();
            startActivity(i);
	    }
    	
    }
 
    // Response from Edit Product Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // if result code 100
        if (resultCode == 100) {
            // if result code 100 is received
            // means user edited/deleted product
            // reload this screen again
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
 
    }
 
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllProducts extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddCourse.this);
            pDialog.setMessage("Fetching Courses. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
 
        /**
         * getting All products from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            
           // params.add(new BasicNameValuePair("studentID", "1"));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_courses, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Courses: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    courses = json.getJSONArray(TAG_PRODUCTS);
 
                    // looping through All Products
                    for (int i = 0; i < courses.length(); i++) {
                        JSONObject c = courses.getJSONObject(i);
 
                        // Storing each json item in variable
                        String id = c.getString(TAG_CID);
                        String name = c.getString(TAG_NAME);
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put(TAG_CID, id);
                        map.put(TAG_NAME, name);
 
                        // adding HashList to ArrayList
                        coursesList.add(map);
                    }
                } else {
                    // no products found
                    // Launch Add New product Activity
                  Intent i = new Intent(getApplicationContext(), test.class);
                    // Closing all previous activities
                   i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                   startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            AddCourse.this, coursesList,
                            R.layout.list_item, new String[] { TAG_CID,
                                    TAG_NAME},
                            new int[] { R.id.cid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });
 
        }
 
    }
	
	
}
