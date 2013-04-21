package com.kokaz.epitoken;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TokenAdapter extends ArrayAdapter<String>
{
	private LayoutInflater		m_inflater;
	private int	 		 		m_layout;
	private ArrayList<String>	m_alcourses;
	private ViewHolder			holder;
	private String				m_currentToken;
	private ArrayList<String>	m_codeevents;
	
	public TokenAdapter(Activity activity, ArrayList<String> courses, ArrayList<String> codeevents, int p_layout) 
	{
		super(activity, p_layout, courses);
		m_inflater = LayoutInflater.from(activity);
		m_layout = p_layout;
		m_alcourses = courses;
		m_codeevents = codeevents;
	}
		
	private class ViewHolder 
	{
		TextView 		m_tvCourseName;
		EditText		m_etToken;	
		Button			m_bMore;
		Button			m_bValidateToken;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		m_currentToken = m_alcourses.get(position);
		if(convertView == null) 
		{
			holder = new ViewHolder();
			convertView = m_inflater.inflate(m_layout, null);
			holder.m_tvCourseName = (TextView)convertView.findViewById(com.kokaz.epitoken.R.id.text);
			holder.m_etToken = (EditText)convertView.findViewById(com.kokaz.epitoken.R.id.token);
			holder.m_bMore = (Button)convertView.findViewById(com.kokaz.epitoken.R.id.expandable_toggle_button);
			holder.m_bValidateToken = (Button)convertView.findViewById(com.kokaz.epitoken.R.id.validate_token);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		if (m_currentToken != null)
			holder.m_tvCourseName.setText(m_currentToken);

		holder.m_bValidateToken.setTag(m_codeevents.get(position));
		return convertView;
	}	
}