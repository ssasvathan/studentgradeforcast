package cen.comp313.student_portal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
 

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class AllProductsActivity extends ListActivity {

	 // Progress Dialog
    private ProgressDialog pDialog;
    
    String studentID;

 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> coursesList;
 
    // url to get all products list
    private static String url_all_products = "http://teampineapple.host22.com/andriod_connect/get_courses_by_student.php";
 
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
                 String cid = ((TextView) view.findViewById(R.id.cid)).getText().toString();
 
                // Starting new intent
             Intent in = new Intent(getApplicationContext(), EditCourse.class);
             //sending pid to next activity
             in.putExtra(TAG_CID, cid);
             in.putExtra("studentID", studentID);
 
                // starting new activity and expecting some response back
             startActivityForResult(in, 100);
            }
        });
 
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
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.course_addmenu, menu);
		return true;
	}
	
    @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
	      switch (item.getItemId()) 
	    {
	    
	        case R.id.addCourse:
	            Intent i = new Intent(AllProductsActivity.this, AddCourse.class);
	            i.putExtra("studentID", studentID);
	            startActivity(i);
	            return true;
	        case R.id.courselogOut:
	        	if(Login.currentStudent != null)
	        	{
	        		Login.currentStudent = null;
	        		Intent loginIntent= new Intent(this, Login.class);
	        		startActivity(loginIntent);
	        	}
	        		return true;
	        case R.id.caHomeBtn:
//	        	  Intent in = new Intent(AllProductsActivity.this, Student_Home.class);
//		          in.putExtra("studentID", studentID);
//		          startActivity(in);
	        	startActivity(new Intent(AllProductsActivity.this, Tab_Activity.class));
	        default:
	        	return super.onOptionsItemSelected(item);
	     } // end switch
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
            pDialog = new ProgressDialog(AllProductsActivity.this);
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
            
          Bundle extras = getIntent().getExtras();
            
          studentID = extras.getString("studentID");
            
           params.add(new BasicNameValuePair("studentID", studentID));
           // params.add(new BasicNameValuePair("studentID", "1"));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
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
                  Intent i = new Intent(getApplicationContext(), AddCourse.class);
                  i.putExtra("studentID", studentID);
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
                            AllProductsActivity.this, coursesList,
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
