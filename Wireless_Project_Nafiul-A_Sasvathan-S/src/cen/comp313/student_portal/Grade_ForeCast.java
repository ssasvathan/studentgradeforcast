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

import android.widget.ProgressBar;
import android.widget.TextView;

public class Grade_ForeCast extends Activity {
	
	private TextView toA1Txt;
	private TextView toATxt;
	private TextView toB1Txt;
	private TextView toBTxt;
	private TextView toC1Txt;
	private TextView toCTxt;
	private TextView toD1Txt;
	private TextView toDTxt;
	private TextView total;
	
	private ProgressBar bar;
	
	String studentID, courseID;
	
	// Progress Dialog
    private ProgressDialog pDialog;
 
    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();
 
    List<Assesment> assessmentList= null;
 
    // url to get all products list
    private static String url_all_products = "http://teampineapple.host22.com/andriod_connect/get_assessments_by_course.php";
    
 // products JSONArray
    JSONArray assessments = null;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_forecast);
        
        
        assessmentList = new ArrayList<Assesment>();
        
        toA1Txt= (TextView) findViewById(R.id.toA1);
        toATxt= (TextView) findViewById(R.id.toA);
        toB1Txt= (TextView) findViewById(R.id.gradetoB1);
        toBTxt= (TextView) findViewById(R.id.toB);
        toC1Txt= (TextView) findViewById(R.id.toC1);
        toCTxt= (TextView) findViewById(R.id.toC);
        toD1Txt= (TextView) findViewById(R.id.toD1);
        toDTxt= (TextView) findViewById(R.id.toD);
        bar = (ProgressBar) findViewById(R.id.progressBar1);
        total = (TextView) findViewById(R.id.total);
        
       Bundle extras = getIntent().getExtras();
       
       if(extras != null)
       {
    	   studentID = extras.getString(Student_Home.STUDENTID);
    	   courseID = extras.getString("courseID");
       }
        
     // Loading Assessments in Background Thread
        new LoadAllAssessments().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	  getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }
       
    @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
        switch (item.getItemId()) 
  	    {
  	    
  	        case R.id.homeMenuBtn:
  	          Intent i = new Intent(Grade_ForeCast.this, Student_Home.class);
	            i.putExtra("studentID", studentID);
	            startActivity(i);
	            return true;
	        case R.id.forecastlogOut:
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
	   } // end method onOptionsItemSelected
    
    /**
     * Background Async Task to Load all product by making HTTP Request
     * */
    class LoadAllAssessments extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Grade_ForeCast.this);
            pDialog.setMessage("Calculating Grade Forecast. Please wait...");
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
            params.add(new BasicNameValuePair("studentID", studentID));
            params.add(new BasicNameValuePair("courseID", courseID));
            // getting JSON string from URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
 
            // Check your log cat for JSON reponse
            Log.d("All Products: ", json.toString());
 
            try {
                // Checking for SUCCESS TAG
                int success = json.getInt("success");
 
                if (success == 1) {
                    // products found
                    // Getting Array of Products
                    assessments = json.getJSONArray("assessments");
 
                    // looping through All Products
                    for (int i = 0; i < assessments.length(); i++) {
                        JSONObject c = assessments.getJSONObject(i);
 
                        // Storing each json item in variable
                        Assesment assessment = new Assesment();
                        assessment.setAssesmentid(c.getInt("assessmentID"));
                        assessment.setName(c.getString("name"));
                        assessment.setWeight(c.getString("weight"));
                        assessment.setAchievedgrade(c.getString("achiveGrade"));
 
                        // adding HashList to ArrayList
                        assessmentList.add(assessment);
                    }
                } else {
                   Log.d("Get Assesments", "No Assessments Found");
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
        	Grades g= new Grades(assessmentList);
    		g.calculateGrades();
    		toA1Txt.setText(String.valueOf(g.getGradeToAPlus()));
    		toATxt.setText(String.valueOf(g.getGradeToA()));
    		toB1Txt.setText(String.valueOf(g.getGradeToBPlus()));
    		toBTxt.setText(String.valueOf(g.getGradeToB()));
    		toC1Txt.setText(String.valueOf(g.getGradeToCPlus()));
    		toCTxt.setText(String.valueOf(g.getGradeToC()));
    		toD1Txt.setText(String.valueOf(g.getGradeToDPlus()));
    		toDTxt.setText(String.valueOf(g.getGradeToD()));
    		bar.setProgress((int) g.getWeightedMark());
    		total.setText(g.getWeightedMark()+"/"+bar.getMax());
    		
            pDialog.dismiss();
        }
    }
}
