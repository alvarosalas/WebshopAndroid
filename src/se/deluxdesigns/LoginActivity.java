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
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		new LogInTask().execute();
	}
	
	class LogInTask extends AsyncTask<Void, Void, String> {
		private String username;
		private String password;
		
		@Override
		protected String doInBackground(Void... params) {
			try {
				HttpPost post = new HttpPost("http://" + ip + ":9000/api/login");

				DefaultHttpClient client = new DefaultHttpClient();

				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs
						.add(new BasicNameValuePair("username", username));
				nameValuePairs
						.add(new BasicNameValuePair("password", password));

				post.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				String response = client.execute(post,
						new BasicResponseHandler());
				cookies = client.getCookieStore().getCookies();

				return response;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if (cookies.isEmpty()) {
				Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG)
						.show();
			} else {
				startActivity(new Intent(LoginActivity.this, LogInTask.class));
				Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG)
						.show();
			}
		}

	}
}
