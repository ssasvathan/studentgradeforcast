package cen.comp313.student_portal;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class test extends Activity {
	

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity); 
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

}
