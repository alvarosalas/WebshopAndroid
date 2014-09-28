package se.deluxdesigns;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import se.deluxdesigns.testappview.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ProductForm extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_product);
		GetCategoryFromServer getCategoryFromServer = new GetCategoryFromServer();
		getCategoryFromServer.execute();
		
		Button button = (Button) findViewById(R.id.create);
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				try {
					Spinner category = (Spinner) findViewById(R.id.spinner1);
					CreateProductOnServer createProductOnServer = new CreateProductOnServer();
					EditText name = (EditText) findViewById(R.id.product_name);
					EditText description = (EditText) findViewById(R.id.product_description);
					EditText price = (EditText) findViewById(R.id.product_cost);
					EditText rrp = (EditText) findViewById(R.id.product_rrp);
					createProductOnServer.setName(name.getText().toString());
					createProductOnServer.setDescription(description.getText().toString());
					createProductOnServer.setCost(price.getText().toString());
					createProductOnServer.setRrp(rrp.getText().toString());
					JSONObject jObject = new JSONObject(category
							.getSelectedItem().toString());
					createProductOnServer.setCategory(jObject.getString("id"));
					createProductOnServer.execute();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
	}
	class CreateProductOnServer extends AsyncTask<Void, Void, Boolean>{
		
		
		public String getName() {
			return name;
		}


		public void setName(String name) {
			this.name = name;
		}


		public String getDescription() {
			return description;
		}


		public void setDescription(String description) {
			this.description = description;
		}


		public String getCost() {
			return cost;
		}


		public void setCost(String cost) {
			this.cost = cost;
		}


		public String getRrp() {
			return rrp;
		}


		public void setRrp(String rrp) {
			this.rrp = rrp;
		}


		public String getStock() {
			return stock;
		}


		public void setStock(String stock) {
			this.stock = stock;
		}
		public String getCategory() {
			return categorys;
		}
		public void setCategory(String categorys) {
			this.categorys = categorys;
		}

		private String name;
		private String description;
		private String cost;
		private String rrp;
		private String stock;
		private String categorys;
		
		
		
		
		protected Boolean doInBackground(Void... params) {
			try{
				HttpPut put = new HttpPut("http://" + ip + ":9000/api/products/create");
				
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("name", name));
				nameValuePairs.add(new BasicNameValuePair("description", description));
				nameValuePairs.add(new BasicNameValuePair("cost", cost));
				nameValuePairs.add(new BasicNameValuePair("rrp", rrp));
				nameValuePairs.add(new BasicNameValuePair("stock", stock));
				nameValuePairs.add(new BasicNameValuePair("category", categorys));
				put.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				new DefaultHttpClient().execute(put, new BasicResponseHandler());
				return true;
			} catch(Exception e){
				Log.e("Error creating", e.getMessage());
				return false;
			}
		}
		@Override
		protected void onPostExecute(Boolean success) {
			if (success == true){
				Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(getApplicationContext(), "Sucker! Try agian", Toast.LENGTH_SHORT).show();
			}
			super.onPostExecute(success);
		}
	}
	
	class GetCategoryFromServer extends AsyncTask<Void, Void, JSONArray> {
		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String response = new DefaultHttpClient().execute(new HttpGet(
						"http://" + ip + ":9000/api/categories"),
						new BasicResponseHandler());
				return new JSONArray(response);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		@Override
		protected void onPostExecute(JSONArray result) {
			Spinner spinner = (Spinner) findViewById(R.id.spinner1);
			spinner.setAdapter(new CategoryAdapter(result));
		}
	}
	class CategoryAdapter extends BaseAdapter implements SpinnerAdapter {
		private JSONArray category;
		public CategoryAdapter(JSONArray category) {
			this.category = category;
		}
		@Override
		public int getCount() {
			return category.length();
		}
		@Override
		public Object getItem(int index) {
			try {
				return category.getJSONObject(index);
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
		@Override
		public long getItemId(int index) {
			try {
				return category.getJSONObject(index).getInt("id");
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
		}
		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			TextView categoryName = new TextView(getApplicationContext());
			try {
				JSONObject cat = category.getJSONObject(index);
				categoryName.setText(cat.getString("name"));
			} catch (JSONException e) {
				throw new RuntimeException(e);
			}
			categoryName.setTextSize(20);
			return categoryName;
		}
	}
}