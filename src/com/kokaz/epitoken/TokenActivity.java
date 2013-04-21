package com.kokaz.epitoken;

import java.util.ArrayList;

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
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.tjerkw.slideexpandable.library.ActionSlideExpandableListView;

public class TokenActivity extends Activity {
	private ActionSlideExpandableListView	list;
	private TokenAdapter 					adapter = null;
	private ArrayList<String>				m_alcourses;
	private ArrayList<String>				m_codeevents;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.single_expandable_list);

		list = (ActionSlideExpandableListView)findViewById(R.id.list);
		
		m_alcourses = new ArrayList<String>();
		m_codeevents = new ArrayList<String>();
		
		HttpClientManager.setCookies(this);
		getTokenJson();
	}

	private void postToken(String token, String eventId) {
	}

	public void getTokenJson() {
		IntraResponseHandler response = new IntraResponseHandler();
		Time days_ago = new Time();
		days_ago.set(System.currentTimeMillis() + (3 * 24 * 60 * 60 * 1000));
		String daDate = days_ago.format("%Y-%m-%d");
		Time cur_date = new Time();
		cur_date.set(System.currentTimeMillis());
		String cDate = cur_date.format("%Y-%m-%d");
		Log.e("EpiTokenError", "/planning/load?format=json&start=" + cDate
				+ "&end=" + daDate + "&onlymypromo=true&onlymymodule=true");
		HttpClientManager.get("/planning/load?format=json&start=" + cDate
				+ "&end=" + daDate + "&onlymypromo=true&onlymymodule=true", response);
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
	private void create_list_courses(JSONArray courses)
	{
		for (int i = 0; i < courses.length(); i++) 
			{
				try {
					JSONObject oneObject = courses.getJSONObject(i);
					//if (oneObject.getString("event_registered").equals("registered") && oneObject.getString("allow_token").equals("true"))
					{
						m_codeevents.add(oneObject.getString("codeevent"));
						m_alcourses.add(oneObject.getString("acti_title"));
					}
				} catch (JSONException e) {
			}
		}
	}

	public void fillListView(JSONArray courses) {
		create_list_courses(courses);
		if (adapter == null) {
			adapter = new TokenAdapter(this, m_alcourses, m_codeevents, R.layout.expandable_list_item);
			list.setAdapter(adapter);
		}
		else
			adapter.notifyDataSetChanged();
		list.setItemActionListener(
				new ActionSlideExpandableListView.OnActionClickListener() 
				{
					@Override
					public void onClick(View listView, View buttonview, int position) {
						if (buttonview.getId() == R.id.validate_token) {
							EditText token = (EditText)listView.findViewById(R.id.token);
							String eventId = (String) buttonview.getTag();
							postToken(token.toString(), eventId);
						}
					}
				}, R.id.token, R.id.validate_token);
	}
}
