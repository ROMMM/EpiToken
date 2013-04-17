package com.kokaz.epitoken;

import org.apache.http.Header;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

public class HttpClientManager {
	private static final String BASE_URL = "https://intra.epitech.eu";
	private static PersistentCookieStore cookies = null;
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
    	client.setCookieStore(cookies);
		client.get(getAbsoluteUrl(url), responseHandler);
	}

	public static void post(Context context, String url, Header[] header,
			RequestParams params, String contenttype,
			AsyncHttpResponseHandler responseHandler) {
		client.setCookieStore(cookies);
		client.post(context, getAbsoluteUrl(url), header, params, contenttype, responseHandler);
	}
	
	private static String getAbsoluteUrl(String relativeUrl) {
		return BASE_URL + relativeUrl;
	}

	public static void cookieClear() {
		cookies.clear();
		client.setCookieStore(cookies);
	}

	public static void setCookies(Context context) {
		if (cookies == null)
			cookies = new PersistentCookieStore(context);
	}
}