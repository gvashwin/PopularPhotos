package com.codepath.ashwin.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;


public class PhotosActivity extends Activity {
	
	
	private String API_PREFIX = "https://api.instagram.com/v1/media/";
	private String COMMENTS_TAG = "comments";
	private String POPULAR_TAG = "popular";
	private String CLIENT_ID_TAG = "?client_id=";
	private String CLIENT_ID_VAL = "ada7a51e376048afa6e44a8f38259717";
	
	//private String API = "https://api.instagram.com/v1/media/popular?client_id=";
	//https://api.instagram.com/v1/media/810238465619840335_323462098/comments?client_id=ada7a51e376048afa6e44a8f38259717
	//https://api.instagram.com/v1/media/popular?client_id=ada7a51e376048afa6e44a8f38259717
			
	private ArrayList<InstagramPhoto> photoList;
	private InstagramPhotosAdapter aPhotos;
	private PullToRefreshListView lvPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        
        // Create a  data  source
        photoList = new ArrayList<InstagramPhoto>();
        // Create adapter for data source
        aPhotos = new InstagramPhotosAdapter(this,photoList);
        // Get reference to the list view 
        lvPhotos = (PullToRefreshListView) findViewById(R.id.lvPhotos);
        // Set the adapter to the list view
        lvPhotos.setAdapter(aPhotos);
        lvPhotos.setOnRefreshListener(new OnRefreshListener(){
        	@Override
        	public void onRefresh(){
        		fetchPopularPhotos();
        	}
        });
        fetchPopularPhotos();
    }


    private void fetchPopularPhotos() {
		// rest api https://api.instagram.com/v1/media/popular?client_id=CLIENT-ID
    	//String url = API+CLIENT_ID;
    	StringBuffer url = new StringBuffer();
    	
    	url.append(API_PREFIX).append(POPULAR_TAG).append(CLIENT_ID_TAG).append(CLIENT_ID_VAL);
    	AsyncHttpClient client = new AsyncHttpClient();
		client.get(url.toString(), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				JSONArray photosJSON = null;
				photoList.clear();
				try {
					photosJSON = response.getJSONArray("data");
					for(int i = 0; i < photosJSON.length(); i++) {
						JSONObject photoObject = photosJSON.getJSONObject(i);
						
						InstagramPhoto instgmPhoto = new InstagramPhoto();
						instgmPhoto.usrName = photoObject.getJSONObject("user").getString("username");
						instgmPhoto.imgCaption = photoObject.getJSONObject("caption").getString("text");
						instgmPhoto.imgUrl = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
						instgmPhoto.imgHeight = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
						instgmPhoto.imgWidth = photoObject.getJSONObject("images").getJSONObject("standard_resolution").getInt("width");
						instgmPhoto.likesCount = photoObject.getJSONObject("likes").getInt("count");
						instgmPhoto.id = photoObject.getString("id");
						JSONArray comments = photoObject.getJSONObject("comments").getJSONArray("data");
						instgmPhoto.lastCmntUser = comments.getJSONObject(comments.length()-1).getJSONObject("from").getString("username");
						instgmPhoto.secondLastCmntUser = comments.getJSONObject(comments.length()-2).getJSONObject("from").getString("username");
						instgmPhoto.lastComment = comments.getJSONObject(comments.length()-1).getString("text");
						instgmPhoto.secondLastComment = comments.getJSONObject(comments.length()-2).getString("text");
						instgmPhoto.userPhoto = photoObject.getJSONObject("user").getString("profile_picture");
						instgmPhoto.time = photoObject.getString("created_time");
						photoList.add(instgmPhoto);
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				aPhotos.notifyDataSetChanged();
				lvPhotos.onRefreshComplete();
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					Throwable throwable, JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, throwable, errorResponse);
			}
			
		});
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
