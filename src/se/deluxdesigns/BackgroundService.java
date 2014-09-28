package se.deluxdesigns;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;

import se.deluxdesigns.testappview.R;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class BackgroundService extends IntentService{
	private int numberOfCategories;
	private int numberOfProducts;
	String ip = "192.168.0.14";

	public BackgroundService() {

		super("BackgroundService");

	}

	@Override
	protected void onHandleIntent(Intent workIntent) {

		try {
			while (true) {

				new GetCategory().execute();
				new GetProduct().execute();
				Thread.sleep(100 * 5);
			}

		} catch (Exception e) {

			Log.e("Exception", e.getMessage());
		}

	}

	private void vibrateLight() {
		NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification n = new Notification();
		n.vibrate = new long[] { 100, 200, 100, 500 };
		n.defaults = 0;
		n.ledARGB = 0xFF00FFFF;
		n.flags = Notification.FLAG_SHOW_LIGHTS;
		n.ledOnMS = 1000;
		n.ledOffMS = 100;
		nManager.notify(0, n);
	}

	private void makeNotificationCategory() {
		

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(

		this).setSmallIcon(R.drawable.dd_launcher)

		.setContentTitle("Category Update!")

		.setContentText("Check it out!");

		int mNotificationId = (int) System.currentTimeMillis();
		
		Intent resultIntent = new Intent(this, CategoryList.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(CategoryList.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	}

	private void makeNotificationProduct() {
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(

		this).setSmallIcon(R.drawable.dd_launcher)

		.setContentTitle("Product Update!")

		.setContentText("Check it out!");

		int mNotificationId = (int) System.currentTimeMillis();
		
		Intent resultIntent = new Intent(this, ProductList.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(ProductList.class);

		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		mNotifyMgr.notify(mNotificationId, mBuilder.build());
	}

	class GetCategory extends AsyncTask<Void, Void, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... params) {
			try {
				String myResponse = new DefaultHttpClient().execute(
						new HttpGet("http://" + ip + ":9000/api/categories"),
						new BasicResponseHandler());
//				for (Cookie cookie : cookies) {
//					client.getCookieStore().addCookie(cookie);
//				}

				return new JSONArray(myResponse);

			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		protected void onPostExecute(JSONArray result) {

			if (result.length() > numberOfCategories) {
				if (numberOfCategories != 0) {
					makeNotificationCategory();
					vibrateLight();
					try {
						Uri notification = RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						Ringtone r = RingtoneManager.getRingtone(
								getApplicationContext(), notification);
						r.play();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				numberOfCategories = result.length();
			}
		}
	}

	class GetProduct extends AsyncTask<Void, Void, JSONArray> {

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

			if (result.length() > numberOfProducts) {
				if (numberOfProducts != 0) {
					makeNotificationProduct();
					vibrateLight();
					try {
						Uri notification = RingtoneManager
								.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
						Ringtone r = RingtoneManager.getRingtone(
								getApplicationContext(), notification);
						r.play();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				numberOfProducts = result.length();
			}
		}
	}
}