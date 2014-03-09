package com.example.databaseandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Utils 
{
	private static String loginInformation = "";
	private String username = null, password = null;

	public String Login(String username, String password)
	{
		loginInformation = "";
		this.username = username;
		this.password = password;
		
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.burrowsapps.com/test/user.php");
		try 
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));


			HttpResponse response = client.execute(post);
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) 
			{
				//				System.out.println(line);
				loginInformation += line;
			}
			rd.close();
		} catch (Exception e) { } 
		return loginInformation;
	}
	
	public void Update(String username, String password, String points)
	{
		this.username = username;
		this.password = password;
		
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost("http://www.burrowsapps.com/test/update.php");
		try 
		{
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("points", points));
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));


			HttpResponse response = client.execute(post);
//			response.getEntity().getContent();
		} catch (Exception e) { } 
	}

	public String getLogin()
	{
		return loginInformation;
	}

	public void setUserName(String username)
	{
		this.username = username;
	}

	public String getUserName()
	{
		return username;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getPassword()
	{
		return password;
	}
	
	/**
	 * Remove the data based on the key
	 * @param c Context
	 * @param key Key
	 */
	public static void removeData(Context c, String key)
	{
		SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(c).edit();
		if (prefEditor != null)
		{
			prefEditor.remove(key);
			prefEditor.commit();
		}
	}
	
	public static void setStringPref(Context c, String key, String value)
	{
		SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(c).edit();
		if (prefEditor != null)
		{
			prefEditor.putString(key, value);
			prefEditor.commit();
		}
	}
	
	public static String getStringPref(Context c, String key)
	{
		return PreferenceManager.getDefaultSharedPreferences(c).getString(key, "first");
	}
	
	/**
	 * 
	 * @param c
	 * @param key
	 * @param values
	 */
	public static void setStringArrayPref(Context c, String key, List<String> values) 
	{
		SharedPreferences.Editor prefEditor = PreferenceManager.getDefaultSharedPreferences(c).edit();
	    JSONArray a = new JSONArray();
	    
	    for (int i = 0; i < values.size(); i++) 
	    {
	        a.put(values.get(i));
	    }
	    
	    if (! values.isEmpty()) 
	    {
	    	prefEditor.putString(key, a.toString());
	    } 
	    else 
	    {
	    	prefEditor.putString(key, null);
	    }
	    prefEditor.commit();
	}

	/**
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static ArrayList<String> getStringArrayPref(Context c, String key) 
	{
		SharedPreferences prefEditor = PreferenceManager.getDefaultSharedPreferences(c);
	    String json = prefEditor.getString(key, null);
	    ArrayList<String> urls = new ArrayList<String>();
	    
	    if (json != null) 
	    {
	        try 
	        {
	            JSONArray a = new JSONArray(json);
	            
	            for (int i = 0; i < a.length(); i++) 
	            {
	                urls.add(a.optString(i));
	            }
	        } catch (JSONException e) { }
	    }
	    return urls;
	}
}