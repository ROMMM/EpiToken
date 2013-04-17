package com.kokaz.epitoken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SettingsActivity extends Activity {
	EditText m_login;
	EditText m_passwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
		HttpClientManager.setCookies(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void connectionIntra(final View v) {
    	RequestParams params = new RequestParams();
 
    	params.put("login", m_login.getText().toString());
    	params.put("password", m_passwd.getText().toString());
    	params.put("remind", "on");
    	if (m_login.getText().length() == 0 || m_passwd.getText().length() == 0) {
    		Toast.makeText(this, "Champs vide !", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	HttpClientManager.post(getApplicationContext(), "", null, params, 
    				"application/x-www-form-urlencoded", new IntraStatusHandler());
    }

    public void loginClick(View v) {
    	m_login = (EditText) findViewById(R.id.loginText);
    	m_passwd = (EditText) findViewById(R.id.passwdText);

    	if (m_login.getText().length() == 0 || m_passwd.getText().length() == 0) {
    		Toast.makeText(getApplicationContext(), "Champs vide !", Toast.LENGTH_SHORT).show();
    		return;
    	}
    	SharedPreferences prefs = this.getSharedPreferences(
  		      "epitoken", Context.MODE_PRIVATE);
    	prefs.edit().putString("epitoken.login", m_login.getText().toString()).commit();
    	prefs.edit().putString("epitoken.password", m_passwd.getText().toString()).commit();
    	Toast.makeText(getApplicationContext(), "Connexion en cours...", Toast.LENGTH_SHORT).show();
    	this.connectionIntra(v);
    }
    
    public static String rot13(String s) {
		StringBuilder out = new StringBuilder(s.length());
		char[] ca = s.toCharArray();
		
		for (char c : ca) {
			if (c >= 'a' && c <= 'm') c += 13;
			else if (c >= 'n' && c <= 'z') c -= 13;
			else if (c >= 'A' && c <= 'M') c += 13;
			else if (c >= 'N' && c <= 'Z') c -= 13;
			out.append(c);
		}
		return out.toString();
    }
    public static String derot13(String s) {
		StringBuilder out = new StringBuilder(s.length());
		char[] ca = s.toCharArray();
		
		for (char c : ca) {
			if (c >= 'a' && c <= 'm') c += 13;
			else if (c >= 'n' && c <= 'z') c -= 13;
			else if (c >= 'A' && c <= 'M') c += 13;
			else if (c >= 'N' && c <= 'Z') c -= 13;
			out.append(c);
		}
		return out.toString();
    }
    class IntraStatusHandler extends AsyncHttpResponseHandler {
        @Override
        public void onSuccess(String response) {
       	 	startActivity(new Intent(SettingsActivity.this, TokenActivity.class));
        }

        @Override
        public void onFailure(Throwable e, String response) {
	    	Log.e("EpiTokenError", "Fail SettingsActivity");
       	 	Toast.makeText(getApplicationContext(), "Connexion impossible...", Toast.LENGTH_SHORT).show();
        }
    }
}
