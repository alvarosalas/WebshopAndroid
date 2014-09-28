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


public class OrderActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.order_layout);
		super.onCreate(savedInstanceState);
		
		new GetOrders();
	}
	
	class GetOrders extends AsyncTask<Void, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String myResponse = new DefaultHttpClient().execute(
						new HttpGet("http://" + ip + ":9000/api/orders"),
						new BasicResponseHandler());

				return new JSONArray(myResponse);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {

			ListView listView = (ListView) findViewById(R.id.list_view);

			listView.setAdapter(new OrderAdapter(result));
		}
	}
	
	class OrderAdapter extends BaseAdapter {

		private JSONArray order;

		public OrderAdapter(JSONArray orders) {
			this.order = orders;
		}

		@Override
		public int getCount() {
			return order.length();
		}

		@Override
		public Object getItem(int index) {
			try {
				return order.getJSONObject(index);
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
			View orderListItems = getLayoutInflater().inflate(R.layout.order_layout, parent, false);
			TextView orderDate = (TextView) orderListItems.findViewById(R.id.order_date);
			TextView orderUser = (TextView) orderListItems.findViewById(R.id.order_user);
			TextView orderProduct = (TextView) orderListItems.findViewById(R.id.order_product);
			TextView orderQuantity = (TextView) orderListItems.findViewById(R.id.order_quantity);
			TextView orderCost = (TextView) orderListItems.findViewById(R.id.order_cost);
			
			try {
				JSONObject orders = order.getJSONObject(index);
				orderDate.setText("Date:" + " "+ orders.getInt("date"));
				orderUser.setText("Date:" + " "+ orders.getInt("user"));
				
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}

			return orderListItems;
		}

	}
	
}
