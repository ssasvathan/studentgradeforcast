package cen.comp313.student_portal;

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class Tab_Activity  extends  TabActivity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);

            // create the TabHost that will contain the Tabs
            TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


            TabSpec tab1 = tabHost.newTabSpec("home Tab");
            TabSpec tab2 = tabHost.newTabSpec("View Courses");
            TabSpec tab3 = tabHost.newTabSpec("Help Menu");
         

           // Set the Tab name and Activity
           // that will be opened when particular Tab will be selected
            tab1.setIndicator("Home");
            tab1.setContent(new Intent(this,Student_Home.class));
            
            tab2.setIndicator("View Courses");
            Intent courseIntent= new Intent(this, AllProductsActivity.class);
    	    courseIntent.putExtra("studentID", Integer.toString(Login.currentStudent.getID()));
            tab2.setContent(courseIntent);
            
            tab3.setIndicator("Help Menu");
            tab3.setContent(new Intent(this,Help.class));
            
       

            /** Add the tabs  to the TabHost to display. */
            tabHost.addTab(tab1);
            tabHost.addTab(tab2);
            tabHost.addTab(tab3);
       
    }
}
