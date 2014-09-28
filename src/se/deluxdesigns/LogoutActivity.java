package se.deluxdesigns;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

public class LogoutActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		new LogOutTask().execute();
	}

	class LogOutTask extends AsyncTask<Void, Void, String> {
		@Override
		protected String doInBackground(Void... params) {
			finish();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(LogoutActivity.this, result, Toast.LENGTH_LONG)
					.show();
			
		}
	}
}
