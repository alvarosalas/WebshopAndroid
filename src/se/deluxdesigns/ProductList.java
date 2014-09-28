package se.deluxdesigns;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.deluxdesigns.testappview.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ProductList extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);

		new GetProducts().execute();

	}

	class GetProducts extends AsyncTask<Void, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String myResponse = new DefaultHttpClient().execute(
						new HttpGet("http://" + ip + ":9000/api/products"),
						new BasicResponseHandler());

				return new JSONArray(myResponse);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {

			ListView listView = (ListView) findViewById(R.id.list_view);

			listView.setAdapter(new ProductAdapter(result));
		}
	}

	class ProductAdapter extends BaseAdapter {

		private JSONArray products;

		public ProductAdapter(JSONArray products) {
			this.products = products;
		}

		@Override
		public int getCount() {
			return products.length();
		}

		@Override
		public Object getItem(int index) {
			try {
				return products.getJSONObject(index);
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
			String setCategoryName = "";
			View productListItem = getLayoutInflater().inflate(R.layout.products_list_item, parent, false);
			TextView productName = (TextView) productListItem.findViewById(R.id.product_name);
			TextView productDescription = (TextView) productListItem.findViewById(R.id.description);
			TextView productCost = (TextView) productListItem.findViewById(R.id.cost);
			TextView productId = (TextView) productListItem.findViewById(R.id.prod_list_id);
			TextView category = (TextView) productListItem.findViewById(R.id.categoryName);

			Button add = (Button) productListItem.findViewById(R.id.add);

			try {
				final JSONObject product = products.getJSONObject(index);
				
				JSONArray getCategory = new JSONArray(product.getString("categories"));
				for(int i = 0; i < getCategory.length(); i++)
				{
				    JSONObject object = getCategory.getJSONObject(i);
				    String name = object.getString("name");
				    setCategoryName += name + " ";
				}

				final JSONObject product1 = products.getJSONObject(index);
				productName.setText("Name:" + " "+ product1.getString("name"));
				productDescription.setText("Desc:" + " " + product1.getString("description"));
				productCost.setText("Cost:" + " " + product1.getString("cost"));
				productId.setText(" Id:" + " " + product1.getString("id"));
				category.setText(setCategoryName);
				add.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						try {
							new PlaceProductOrder().execute(product1
									.getInt("id"));
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}

			return productListItem;
		}

		private class PlaceProductOrder extends
				AsyncTask<Integer, Void, String> {
			@Override
			protected String doInBackground(Integer... params) {
				String response = "";
				try {
					HttpPost post = new HttpPost("http://" + ip
							+ ":9000/api/orders/create");
					DefaultHttpClient client = new DefaultHttpClient();
					for (Cookie cookie : cookies) {
						client.getCookieStore().addCookie(cookie);
					}
					List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
					nameValuePair.add(new BasicNameValuePair("product-id",
							params[0].toString()));
					post.setEntity(new UrlEncodedFormEntity(nameValuePair));
					response = client.execute(post, new BasicResponseHandler());
					return response;

				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

}
