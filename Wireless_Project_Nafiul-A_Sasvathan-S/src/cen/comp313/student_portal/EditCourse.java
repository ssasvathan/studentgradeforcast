package cen.comp313.student_portal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class EditCourse extends Activity {

	 // Progress Dialog
    private ProgressDialog pDialog;
    
    String studentID;
    String courseID;
    String assesID;
    Boolean isNew = false;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    ArrayList<HashMap<String, String>> assessmentList;
 
    // url to get all products list
    private static String url_all_products = "http://teampineapple.host22.com/andriod_connect/get_assessments_by_course.php";
    private static String url_delete_course = "http://teampineapple.host22.com/andriod_connect/delete_course.php";
 
    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "assessments";
    private static final String TAG_CID = "assessmentID";
    private static final String TAG_NAME = "name";
    private static final String TAG_WEIGHT = "weight";
    private static final String TAG_GRADE = "achiveGrade";
 
    // products JSONArray
    JSONArray assessments = null;
    
    ListView lv;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_home);
 
        // Hashmap for ListView
        assessmentList = new ArrayList<HashMap<String, String>>();
 
        // Loading products in Background Thread
        new LoadAllProducts().execute();
 
        // Get listview
       lv = (ListView) findViewById(R.id.assesList);
 
        // on seleting single product
        // launching Edit Product Screen
        lv.setOnItemClickListener(new OnItemClickListener() {
 
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
          
 
            String asid = ((TextView) view.findViewById(R.id.aid)).getText().toString();
                // Starting new intent
             Intent in = new Intent(getApplicationContext(), EditAsses.class);
             //sending pid to next activity
             in.putExtra("assessmentID", asid);
             in.putExtra("courseID", courseID);
             in.putExtra("studentID", studentID);
 
                // starting new activity and expecting some response back
             startActivityForResult(in, 100);
            }
        });
         
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.course_menu, menu);
		return true;
	}
	
    @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
	      switch (item.getItemId()) 
	    {
	        
	      	case R.id.addAsses: //Add Assessment
	      		Intent i = new Intent(EditCourse.this, EditAsses.class);
	      		isNew = true;
	      		i.putExtra("courseID", courseID);
	            i.putExtra("studentID", studentID);
	            i.putExtra("isNew", isNew);
	            startActivity(i);
	      		return true;
	    	case R.id.deleteCourse:
	        	new DeleteCourse().execute();
	        	return true;
	    	case R.id.gradeForcast:
	    		Intent forcast= new Intent(this, Grade_ForeCast.class);
	    		forcast.putExtra("courseID", courseID);
	    		forcast.putExtra(Student_Home.STUDENTID, studentID);
	    		startActivity(forcast);
	    		return true;
	    	case R.id.assessmentlogOut:
	        	if(Login.currentStudent != null)
	        	{
	        		Login.currentStudent = null;
	        		Intent loginIntent= new Intent(this, Login.class);
	        		startActivity(loginIntent);
	        	}
	        		return true;
	    	case R.id.cHomeBtn:
	    		 Intent in = new Intent(EditCourse.this, Student_Home.class);
		         in.putExtra("studentID", studentID);
		         startActivity(in);
	    		return true;
	        default:
	        	return super.onOptionsItemSelected(item);
	     } // end switch
	   }
    
  
    
    
    
    
    class DeleteCourse extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditCourse.this);
			pDialog.setMessage("Deleting Course. Please wait...");
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
		
			json = jParser.makeHttpRequest(url_delete_course, "POST",paramms);
			
	        // check log cat for response
	        Log.d("Create Response", json.toString());

	        // check for success tag
	        try {
	            int success = json.getInt("success");

	            if (success == 1) { 
	           
	            	
	            } else {
	                // failed to create product
	            	Toast.makeText(getApplicationContext(), "Unable to delete!", Toast.LENGTH_SHORT).show();
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
					return null;
		}
		
	    protected void onPostExecute(String file_url) {
            Intent i = new Intent(EditCourse.this, AllProductsActivity.class);
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
            pDialog = new ProgressDialog(EditCourse.this);
            pDialog.setMessage("Fetching Assessments. Please wait...");
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
          courseID = extras.getString("courseID");
            
           params.add(new BasicNameValuePair("studentID", studentID));
           params.add(new BasicNameValuePair("courseID", courseID));
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
                    assessments = json.getJSONArray(TAG_PRODUCTS);
 
                    // looping through All Products
                    for (int i = 0; i < assessments.length(); i++) {
                        JSONObject c = assessments.getJSONObject(i);
 
                        // Storing each json item in variable
                        assesID = c.getString(TAG_CID);
                        String name = c.getString(TAG_NAME);
                        String weight = c.getString(TAG_WEIGHT);
                        String grade = c.getString(TAG_GRADE);
                        
                        Double g = Double.parseDouble(grade);
                        Double w = Double.parseDouble(weight);
                        Double r = RoundTo2Decimals((g/100)*w);
                        
                        String secondLine = "Grade: " + grade + " -- " + r +" Out of " + weight;
 
                        // creating new HashMap
                        HashMap<String, String> map = new HashMap<String, String>();
 
                        // adding each child node to HashMap key => value
                        map.put("name", name);
                        map.put("desc", secondLine);
                        map.put("id", assesID);
 
                        // adding HashList to ArrayList
                        assessmentList.add(map);
                    }
                } else {
                	isNew = true;
                    // no products found
                    // Launch Add New product Activity
            		Intent i = new Intent(EditCourse.this, EditAsses.class);
    	      		i.putExtra("courseID", courseID);
    	            i.putExtra("studentID", studentID);
    	            i.putExtra("isNew", isNew);
    	            startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
        
    	public double RoundTo2Decimals(double val) {
            DecimalFormat df2 = new DecimalFormat("###.##");
            return Double.valueOf(df2.format(val));
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
                            EditCourse.this, assessmentList,
                            R.layout.list_assessment, new String[] { "name",
                                    "desc", "id"},
                            new int[] { R.id.firstLine, R.id.secondLine, R.id.aid });
                    // updating listview
                    lv.setAdapter(adapter);
                }
            });
 
        }
 
    }
	
	
	
	
}
