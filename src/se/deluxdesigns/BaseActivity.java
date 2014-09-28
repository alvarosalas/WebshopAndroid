package se.deluxdesigns;

import java.util.List;

import org.apache.http.cookie.Cookie;

import se.deluxdesigns.testappview.R;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
	
	
	
	static protected final String ip = "192.168.0.14";
	public static List<Cookie> cookies;
	static protected boolean loggedIn = false;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return loggedIn;
	}
	
	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}

	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.products:
			startActivity(new Intent(this, ProductList.class));
			Toast.makeText(this, "Products", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.category:
			startActivity(new Intent(this, CategoryList.class));
			Toast.makeText(this, "Category", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.create_product:
			startActivity(new Intent(this, ProductForm.class));
			Toast.makeText(this, "Create a product", Toast.LENGTH_SHORT).show();
			return true;
		case R.id.create_category:
			startActivity(new Intent(this, CategoryForm.class));
			return true;
		case R.id.orders:
			startActivity(new Intent(this, OrderActivity.class));
			return true;
		case R.id.logout:
			onStop();
			startActivity(new Intent(this, StartActivity.class));
			Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
			return true;
		}
		
		return false;
	}
}
