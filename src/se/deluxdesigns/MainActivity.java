package se.deluxdesigns;


import se.deluxdesigns.testappview.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;


public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		startService(new Intent(this, BackgroundService.class));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}
	
}
