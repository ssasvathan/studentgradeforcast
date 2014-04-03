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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	TextView uNameTxt;
	TextView passwordTxt;
	Button loginBtn;
	Button signUpBtn;
	public static Student currentStudent;
	String password, userName, loginStatus;

	// Progress Dialog
	private ProgressDialog pDialog;

	// JSON parser class
	JSONParser jsonParser;

	public static String url_get_student = "http://teampineapple.host22.com/andriod_connect/get_studnet_by_name.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		uNameTxt = (TextView) findViewById(R.id.uNameTxt);
		passwordTxt = (TextView) findViewById(R.id.passwordTxt);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		signUpBtn = (Button) findViewById(R.id.signUpBtn);

		signUpBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent signupIntent = new Intent(Login.this, SignUp.class);
				startActivity(signupIntent);
			}
		});

		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userName = uNameTxt.getText().toString();
				password = passwordTxt.getText().toString();
				if (!userName.isEmpty()  || !password.isEmpty()) 
				{
					new GetStudent().execute();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"UserName/Password cannot be empty!", 
							Toast.LENGTH_SHORT)
							.show();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.student_menu, menu);
		return true;
	}

	boolean isAuthenticate(String uName, String password) {
		

		return false;
	}

	/**
	 * Background Async Task to Get complete product details
	 * */
	class GetStudent extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Login.this);
			pDialog.setMessage("Logging in to student portal. Please wait...");
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
					loginStatus="";
					try {
						// Building Parameters
						List<NameValuePair> paramms = new ArrayList<NameValuePair>();
						paramms.add(new BasicNameValuePair("userName", uNameTxt
								.getText().toString()));

						// getting product details by making HTTP request
						// Note that product details url will use GET request
						jsonParser= new JSONParser();
						JSONObject json = jsonParser.makeHttpRequest(
								url_get_student, "GET", paramms);

						// check your log for json response
						Log.d("Single Product Details", json.toString());

						// json success tag
						success = json.getInt("success");
						if (success == 1) {
							// successfully received product details
							JSONArray productObj = json.getJSONArray("student"); // JSON
																					// Array

							// get first product object from JSON Array
							JSONObject product = productObj.getJSONObject(0);

							currentStudent = new Student();
							// display product data in EditText
							currentStudent.setID(product.getInt("studentID"));
							currentStudent.setFirstName(product.getString("firstName"));
							currentStudent.setLastName(product.getString("lastName"));
							currentStudent.setUserName(product.getString("userName"));
							currentStudent.setPassword(product.getString("password"));
							
							if (currentStudent.getUserName() != "") 
							{
								if (currentStudent.getPassword().equals(password)) 
								{
									Log.i("Loggin Status", "Success");
									startActivity(new Intent(Login.this, Tab_Activity.class));
								}
								else
								{
									currentStudent = null;
									loginStatus ="Invalid UserName/Password!";
								}
							}else
							{
								loginStatus="Unable to login at the moment!"; 
							}

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
	    	if(loginStatus != "")
	    	Toast.makeText(getApplicationContext(), loginStatus, Toast.LENGTH_SHORT).show();
	        pDialog.dismiss();
	    }
	}
	
}
