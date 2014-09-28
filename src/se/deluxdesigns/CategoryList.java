package se.deluxdesigns;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.deluxdesigns.testappview.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CategoryList extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);

		new GetCategory().execute();

	}

	class GetCategory extends AsyncTask<Void, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String myResponse = new DefaultHttpClient().execute(
						new HttpGet("http://" + ip + ":9000/api/categories"),
						new BasicResponseHandler());

				return new JSONArray(myResponse);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {

			ListView listView = (ListView) findViewById(R.id.list_view);

			listView.setAdapter(new CategoryAdapter(result));
		}
	}

	class CategoryAdapter extends BaseAdapter {

		private JSONArray categorys;

		public CategoryAdapter(JSONArray categorys) {
			this.categorys = categorys;
		}

		@Override
		public int getCount() {
			return categorys.length();
		}

		@Override
		public Object getItem(int index) {
			try {
				return categorys.getJSONObject(index);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}

		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			View categoryListItem = getLayoutInflater().inflate(R.layout.category_list_item, parent, false);
			TextView categoryName = (TextView) categoryListItem.findViewById(R.id.product_name);
			
			try {
				JSONObject category = categorys.getJSONObject(index);
				categoryName.setText("category name:" + " "+ category.getString("name"));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}

			return categoryListItem;
		}

	}

}
