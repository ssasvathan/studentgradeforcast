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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SignUp extends Activity {
	
	// Progress Dialog
    private ProgressDialog pDialog;
    private String pDialogMsg= "Creating Student..";
    private String validationMsg="";
    
    JSONParser jsonParser = new JSONParser();
    
	TextView uNameTxt;
	TextView password;
	TextView firstName;
	TextView lastName;
	Button signUpBtn;
	
	int studentID=0;
	
	 // url to create new student
    //private static String url_create_product = "http://10.0.2.2/andriod_connect/create_student.php";
    private static String url_create_product = "http://teampineapple.host22.com/andriod_connect/create_student.php";
    private static String url_update_student = "http://teampineapple.host22.com/andriod_connect/update_student.php";
    
    
 // JSON Node names
    private static final String TAG_SUCCESS = "success";
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        
        uNameTxt = (TextView) findViewById(R.id.addUNameTxt);
        password = (TextView) findViewById(R.id.addPasswordTxt);
        firstName = (TextView) findViewById(R.id.addFNameTxt);
        lastName = (TextView) findViewById(R.id.addLNameTxt);
        signUpBtn = (Button) findViewById(R.id.addStudentBtn);
        
        Bundle extras = getIntent().getExtras(); // get Bundle of extras
        
     // if there are extras, use them to populate the EditTexts
        if (extras != null)
        {
        	pDialogMsg = "Updating Student...";
           studentID = extras.getInt(Student_Home.STUDENTID);
           uNameTxt.setText(extras.getString(Student_Home.USER_NAME));  
           lastName.setText(extras.getString(Student_Home.LAST_NAME));  
           firstName.setText(extras.getString(Student_Home.FIRST_NAME));  
           password.setText(extras.getString(Student_Home.PASSWORD));   
        } // end if
        
        signUpBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (validateForm()) {
					// TODO Auto-generated method stub
					new CreateNewProduct().execute();
				} else {
					Toast.makeText(getApplicationContext(), validationMsg, Toast.LENGTH_SHORT).show();
				}
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    
    /**
     * Background Async Task to Create new product
     * */
    class CreateNewProduct extends AsyncTask<String, String, String> {
 
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignUp.this);
            pDialog.setMessage(pDialogMsg);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
 
        /**
         * Creating Student
         * */
        protected String doInBackground(String... args) {
        	saveStudent();
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            //pDialog.dismiss();
        }
 
    }
    
    private void saveStudent()
    {
    	String uName = uNameTxt.getText().toString();
        String fName = firstName.getText().toString();
        String lName = lastName.getText().toString();
        String pwd = password.getText().toString();

        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("studentID", String.valueOf(studentID)));
        params.add(new BasicNameValuePair("userName", uName));
        params.add(new BasicNameValuePair("firstName", fName));
        params.add(new BasicNameValuePair("lastName", lName));
        params.add(new BasicNameValuePair("password", pwd));
        
		JSONObject json;
		if (getIntent().getExtras() == null) {
			// getting JSON Object
			// Note that create product url accepts POST method
			json = jsonParser.makeHttpRequest(url_create_product, "POST",params);
			
			// check for success tag
	        try {
	            int success = json.getInt(TAG_SUCCESS);

	            if (success == 1) { 
	                					
	                finish();
	            } else {
	                // failed to create product
	            	Toast.makeText(getApplicationContext(), "Unable to create student!", Toast.LENGTH_SHORT).show();
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        
		} else {
			// getting JSON Object
			// Note that create product url accepts POST method
			json = jsonParser.makeHttpRequest(url_update_student, "POST",params);
			
			// check for success tag
	        try {
	            int success = json.getInt(TAG_SUCCESS);

	            if (success == 1) { 
	                // closing this screen and going to login screen
	            	// Building Parameters
					List<NameValuePair> paramms = new ArrayList<NameValuePair>();
					paramms.add(new BasicNameValuePair("studentID", String.valueOf(studentID)));

					// getting product details by making HTTP request
					// Note that product details url will use GET request
					jsonParser= new JSONParser();
					json = jsonParser.makeHttpRequest(
							Login.url_get_student, "GET", paramms);

					// check your log for json response
					Log.d("Get Studnet", json.toString());

					// json success tag
					success = json.getInt("success");
					if (success == 1) {
						// successfully received product details
						JSONArray productObj = json.getJSONArray("student"); // JSON
																				// Array

						// get first product object from JSON Array
						JSONObject product = productObj.getJSONObject(0);

						Student student = new Student();
						// display product data in EditText
						student.setID(product.getInt("studentID"));
						student.setFirstName(product.getString("firstName"));
						student.setLastName(product.getString("lastName"));
						student.setUserName(product.getString("userName"));
						student.setPassword(product.getString("password"));
						Login.currentStudent = student;
					}
						
	                finish();
	            } else {
	                // failed to create product
	            	Toast.makeText(getApplicationContext(), "Unable to update student!", Toast.LENGTH_SHORT).show();
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
		}        
    }
    
    private boolean validateForm()
    {
    	if(uNameTxt.getText().toString().isEmpty())
    	{
    		validationMsg="User Name Cannot be empty!";
    		return false;
    	}
    	if(password.getText().toString().isEmpty())
    	{
    		validationMsg="Password Cannot be empty!";
    		return false;
    	}
    	if(firstName.getText().toString().isEmpty())
    	{
    		validationMsg="First Name Cannot be empty!";
    		return false;
    	}
    	if(lastName.getText().toString().isEmpty())
    	{
    		validationMsg="Last Name Cannot be empty!";
    		return false;
    	}
    	return true;
    }
}
