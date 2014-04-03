package cen.comp313.student_portal;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;

public class Student_Home extends Activity {

	final static String STUDENTID = "studentID";
	final static String FIRST_NAME = "firstName";
	final static String LAST_NAME = "lastName";
	final static String USER_NAME = "userName";
	final static String PASSWORD = "password";
	
	private TextView userNameTxt;
	private TextView firstNameTxt;
	private TextView lastNameTxt;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student__home);
        
        userNameTxt = (TextView) findViewById(R.id.home_uName);
        firstNameTxt = (TextView) findViewById(R.id.home_fName);
        lastNameTxt = (TextView) findViewById(R.id.home_lName);
        
        userNameTxt.setText(Login.currentStudent.getUserName());
        firstNameTxt.setText(Login.currentStudent.getFirstName());
        lastNameTxt.setText(Login.currentStudent.getLastName());
        
  

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return true;
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Normal case behavior follows
        userNameTxt.setText(Login.currentStudent.getUserName());
        firstNameTxt.setText(Login.currentStudent.getFirstName());
        lastNameTxt.setText(Login.currentStudent.getLastName());
    }
    
    @Override
	   public boolean onOptionsItemSelected(MenuItem item) 
	   {
	      switch (item.getItemId()) // switch based on selected MenuItem's ID
	      {
	         case R.id.editStudent:
	            // create an Intent to launch the AddEditContact Activity
	            Intent addEditStudent = new Intent(this, SignUp.class);
	            
	            // pass the selected contact's data as extras with the Intent
	            addEditStudent.putExtra(STUDENTID, Login.currentStudent.getID());
	            addEditStudent.putExtra(USER_NAME, Login.currentStudent.getUserName());
	            addEditStudent.putExtra(FIRST_NAME, Login.currentStudent.getFirstName());
	            addEditStudent.putExtra(LAST_NAME, Login.currentStudent.getLastName());
	            addEditStudent.putExtra(PASSWORD, Login.currentStudent.getPassword());
	            startActivity(addEditStudent); // start the Activity
	            return true;
	         case R.id.viewCourse:
	        	 	Intent courseIntent= new Intent(this, AllProductsActivity.class);
	        	    courseIntent.putExtra("studentID", Integer.toString(Login.currentStudent.getID()));
	        	 	startActivity(courseIntent);
	        	 	return true;
	        case R.id.logout:
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
    
}
