package se.deluxdesigns;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import se.deluxdesigns.testappview.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CategoryForm extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_category);

		Button button = (Button) findViewById(R.id.create);

		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				CreateCategoryOnServer createCategoryOnServer = new CreateCategoryOnServer();
				EditText name = (EditText) findViewById(R.id.category_name);

				createCategoryOnServer
						.CreateCategory(name.getText().toString());
				createCategoryOnServer.execute();

			}
		});

	}

	class CreateCategoryOnServer extends AsyncTask<Void, Void, Boolean> {

		private String name;

		public void CreateCategory(String name) {
			this.name = name;

		}

		protected Boolean doInBackground(Void... params) {
			try {
				DefaultHttpClient client = new DefaultHttpClient();
				HttpPost put = new HttpPost("http://" + ip
						+ ":9000/api/categories/create");

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("name", name));
				put.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				
						client.execute(put, new BasicResponseHandler());
				return true;
			} catch (Exception e) {
				Log.e("Error creating", e.getMessage());
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean success) {
			if (success == true) {
				Toast.makeText(getApplicationContext(), "success",
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(), "Sucker",
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}