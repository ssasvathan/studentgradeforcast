package cen.comp313.student_portal;

import java.util.ArrayList;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class EditAsses extends Activity {

	TextView nameText;
	TextView gradeText;
	TextView weightText;
	Button updateButton;
	Assesment a;

	String name, grade, weight, aid, cid, sid;
	
	Boolean isNew = false;

	// Progress Dialog
	private ProgressDialog pDialog;
	private String validationMsg="";

	// JSON parser class
	JSONParser jsonParser;

	public static String url_get_assessment = "http://teampineapple.host22.com/andriod_connect/get_assessment_by_id.php";
	public static String url_update_assessment = "http://teampineapple.host22.com/andriod_connect/update_assessment.php";
	public static String url_delete_assessment = "http://teampineapple.host22.com/andriod_connect/delete_assessment.php";
	public static String url_add_assessment = "http://teampineapple.host22.com/andriod_connect/add_assessment.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_asses_edit);
		nameText = (TextView) findViewById(R.id.assesName);
		gradeText = (TextView) findViewById(R.id.gradeText);
		weightText = (TextView) findViewById(R.id.weightText);
		updateButton = (Button) findViewById(R.id.updateAssesBtn);
		
		Bundle extras = getIntent().getExtras();
          
	    aid = extras.getString("assessmentID");
	    cid = extras.getString("courseID");
	    sid = extras.getString("studentID");
	    isNew = extras.getBoolean("isNew");
	    
	    
	    if (!isNew){
	    new GetAssessment().execute();
	    }
	   
		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = nameText.getText().toString();
				grade = gradeText.getText().toString();
				weight = weightText.getText().toString();
				if (validateForm()) {
					if (!isNew) {
						new UpdateAssessment().execute();
					} else {
						new CreateAssessment().execute();
					}
				}else
				{
					Toast.makeText(getApplicationContext(), validationMsg, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
	    getMenuInflater().inflate(R.menu.assessment_menu, menu);
		return true;
	}
	
    @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
	      switch (item.getItemId()) // switch based on selected MenuItem's ID
	      {
	         case R.id.deleteAsses: 
	         new DeleteAssessment().execute();
	         return true;
	         case R.id.asHomeBtn:
	         Intent i = new Intent(EditAsses.this, Student_Home.class);
		     i.putExtra("studentID", sid);
		     startActivity(i);	 
	         return true;
	         default:
	            return super.onOptionsItemSelected(item);
	      } // end switch
	   }

    
	class CreateAssessment extends AsyncTask<String, String, String> {

		JSONObject json;
		JSONParser jp = new JSONParser();
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditAsses.this);
			pDialog.setMessage("Creating Assessment. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

	        // Building Parameters
	        List<NameValuePair> myParam = new ArrayList<NameValuePair>();
	        myParam.add(new BasicNameValuePair("name", name));
	        myParam.add(new BasicNameValuePair("achiveGrade", grade));
	        myParam.add(new BasicNameValuePair("weight", weight));
	        myParam.add(new BasicNameValuePair("studentID", sid));
	        myParam.add(new BasicNameValuePair("courseID", cid));

		
			
			Log.d("Params", myParam.toString());
		
		
			
			json = jp.makeHttpRequest(url_add_assessment, "POST", myParam);
			
	        // check log cat for response
	        Log.d("Create Response", json.toString());

	        // check for success tag
	        try {
	            int success = json.getInt("success");

	            if (success == 1) { 
	           
	            	
	            } else {
	                // failed to create product
	            	Toast.makeText(getApplicationContext(), "Unable to create!", Toast.LENGTH_SHORT).show();
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
	    
            Intent i = new Intent(EditAsses.this, EditCourse.class);
             // Closing all previous activities
            i.putExtra("courseID", cid);
            i.putExtra("studentID", sid);
            pDialog.dismiss();
            startActivity(i);
            
	        
	    }
	}
    
    
    
    class DeleteAssessment extends AsyncTask<String, String, String>{

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditAsses.this);
			pDialog.setMessage("Deleting Assessment. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
    	
		@Override
		protected String doInBackground(String... arg0) {
	        // Building Parameters
	        List<NameValuePair> paramms = new ArrayList<NameValuePair>();
	        paramms.add(new BasicNameValuePair("assessmentID", aid));
	     
			JSONObject json;
		
			json = jsonParser.makeHttpRequest(url_delete_assessment, "POST",paramms);
			
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
            Intent i = new Intent(EditAsses.this, EditCourse.class);
             // Closing all previous activities
            i.putExtra("courseID", cid);
            i.putExtra("studentID", sid);
            pDialog.dismiss();
            startActivity(i);
	    }
    	
    }
    
	/**
	 * Background Async Task to Get complete product details
	 * */
	class UpdateAssessment extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditAsses.this);
			pDialog.setMessage("Updating Assessment. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

	        // Building Parameters
	        List<NameValuePair> paramms = new ArrayList<NameValuePair>();
	        paramms.add(new BasicNameValuePair("assessmentID", aid));
	        paramms.add(new BasicNameValuePair("name", name));
	        paramms.add(new BasicNameValuePair("achiveGrade", grade));
	        paramms.add(new BasicNameValuePair("weight", weight));
	        
	
			JSONObject json;
		
			json = jsonParser.makeHttpRequest(url_update_assessment, "POST",paramms);
			
	        // check log cat for response
	        Log.d("Create Response", json.toString());

	        // check for success tag
	        try {
	            int success = json.getInt("success");

	            if (success == 1) { 
	           
	            	
	            } else {
	                // failed to create product
	            	Toast.makeText(getApplicationContext(), "Unable to update!", Toast.LENGTH_SHORT).show();
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
	    
            Intent i = new Intent(EditAsses.this, EditCourse.class);
             // Closing all previous activities
            i.putExtra("courseID", cid);
            i.putExtra("studentID", sid);
            pDialog.dismiss();
            startActivity(i);
            
	        
	    }
	}


	/**
	 * Background Async Task to Get complete product details
	 * */
	class GetAssessment extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(EditAsses.this);
			pDialog.setMessage("Fetching Assessment. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

					// Check for success tag
					int success;
					try {
						// Building Parameters
						List<NameValuePair> paramms = new ArrayList<NameValuePair>();
						paramms.add(new BasicNameValuePair("assessmentID", aid));
					
						// getting product details by making HTTP request
						// Note that product details url will use GET request
						jsonParser= new JSONParser();
						JSONObject json = jsonParser.makeHttpRequest(url_get_assessment, "GET", paramms);

						// check your log for json response
						Log.d("Single Product Details", json.toString());

						// json success tag
						success = json.getInt("success");
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json.getJSONArray("assessment"); // JSON
																					// Array

							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);
							
							a = new Assesment();

							a.setAssesmentid(product.getInt("assessmentID"));
							a.setName(product.getString("name"));
							a.setAchievedgrade(product.getString("achiveGrade"));
							a.setWeight(product.getString("weight"));
						    

						} else {
							// product with pid not found
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
	        // dismiss the dialog once done
	    	//Toast.makeText(getApplicationContext(), loginStatus, Toast.LENGTH_SHORT).show();
	    	nameText.setText(a.getName());
	    	gradeText.setText(a.getAchievedgrade());
	    	weightText.setText(a.getWeight());
	        pDialog.dismiss();
	    }
	}
	
    private boolean validateForm()
    {
    	if(name.isEmpty())
    	{
    		validationMsg="User Name Cannot be empty!";
    		return false;
    	}
    	if(grade.isEmpty())
    	{
    		validationMsg="Grade Cannot be empty!";
    		return false;
    	}
    	if(weight.isEmpty())
    	{
    		validationMsg="Weight Cannot be empty!";
    		return false;
    	}

    	return true;
    }
}
