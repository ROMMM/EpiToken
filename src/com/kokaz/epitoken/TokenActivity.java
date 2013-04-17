package com.kokaz.epitoken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

public class TokenActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.single_expandable_list);

		HttpClientManager.setCookies(this);
		getTokenJson();
	}

	private void postToken() {
	}

	public void getTokenJson() {
		IntraResponseHandler response = new IntraResponseHandler();
		Time days_ago = new Time();
		days_ago.set(System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000));
		String daDate = days_ago.format("%Y-%m-%d");
		Time cur_date = new Time();
		cur_date.set(System.currentTimeMillis());
		String cDate = cur_date.format("%Y-%m-%d");
		// HttpClientManager
		// .get("/planning/load?format=json&start=2013-04-13&end=2013-04-14",
		// response);
		Log.e("EpiTokenError", "/planning/load?format=json&start=" + daDate
				+ "&end=" + cDate + "&onlymypromo=true&onlymymodule=true");
		HttpClientManager.get("/planning/load?format=json&start=" + daDate
				+ "&end=" + cDate /*+ "&onlymypromo=true&onlymymodule=true"*/, response);
	}

	public boolean logoutClick(MenuItem item) {
		HttpClientManager.cookieClear();
		Toast.makeText(getApplicationContext(), "Deconnecté",
				Toast.LENGTH_SHORT).show();
		startActivity(new Intent(TokenActivity.this, SettingsActivity.class));
		finish();
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.logout_button:
			logoutClick(item);
			return true;
		case R.id.settings_button:
			startActivity(new Intent(TokenActivity.this, SettingsActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	class IntraResponseHandler extends AsyncHttpResponseHandler {

		@Override
		public void onSuccess(String response) {
			response = response.substring(31);
			try {
				fillListView(new JSONArray(response));
			} catch (JSONException e) {
				Log.e("EpiTokenError", "JSONSException" + e.getMessage());
			}
		}

		@Override
		public void onFailure(Throwable e, String response) {
			Toast.makeText(getApplicationContext(), response,
					Toast.LENGTH_LONG).show();
		}
	}

	public ArrayAdapter<String> ListViewAdapter(JSONArray courses) throws JSONException {
		ArrayAdapter<String> values = new ArrayAdapter<String>(this, R.layout.expandable_list_item, R.id.text);

		if (courses == null) {
			Toast.makeText(getApplicationContext(), "Courses empty",
					Toast.LENGTH_SHORT).show();
		} else {
			for (int i = 0; i < courses.length(); i++) {
				try {
					JSONObject oneObject = courses.getJSONObject(i);
					String value = oneObject.getString("acti_title");
					if (oneObject.getString("event_registered").equals("registered") && oneObject.getString("allow_token").equals("true"))
						values.add(value);
				} catch (JSONException e) {
				}
			}
		}

		return values;
	}

	public void fillListView(JSONArray courses) {
		try {
			ActionSlideExpandableListView list = (ActionSlideExpandableListView) this
					.findViewById(R.id.list);
			list.setAdapter(ListViewAdapter(courses));
			list.setItemActionListener(
					new ActionSlideExpandableListView.OnActionClickListener() {
						@Override
						public void onClick(View listView, View buttonview,
								int position) {
							if (buttonview.getId() == R.id.validate_token) {
//								String toto = (String) listView.getTag();
								postToken();
							}
						}
					}, R.id.token, R.id.validate_token);
		} catch (JSONException e) {
		}
	}
}
