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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StartActivity extends BaseActivity {
	String ip = "192.168.0.14";
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		loggedIn = false;
		startService(new Intent(this, BackgroundService.class));
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		Button login = (Button) findViewById(R.id.userLogin);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogInTask logintask = new LogInTask(ip);

				EditText username = (EditText) findViewById(R.id.login_username);
				EditText password = (EditText) findViewById(R.id.login_pasword);

				logintask.setUsername(username.getText().toString());
				logintask.setPassword(password.getText().toString());
				logintask.execute();

			}
		});

	}
	
	@Override
	protected void onRestart() {
		startActivity(new Intent(this, ProductList.class));
		super.onRestart();
	}
	
	class LogInTask extends AsyncTask<Void, Void, String> {
		String ip = "192.168.0.14";
		private String username;
		private String password;

		LogInTask(String ip) {
			this.ip = ip;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

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
				

				return response;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(String result) {
			if (cookies.size() > 0) {
				
				Toast.makeText(getApplicationContext(),
						"Success!", Toast.LENGTH_LONG)
						.show();
				loggedIn = true;
				invalidateOptionsMenu();
				setContentView(R.layout.main_layout);
				onRestart();
			} else {
				Toast.makeText(getApplicationContext(), "Wrong Email or Password",
						Toast.LENGTH_LONG).show();
				loggedIn = false;
			}
		}

	}
}