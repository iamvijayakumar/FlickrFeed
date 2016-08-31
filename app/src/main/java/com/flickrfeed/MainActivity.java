package com.flickrfeed;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.flickrfeed.adapter.GridAdapter;
import com.flickrfeed.utils.FeedStructure;
import com.flickrfeed.utils.UrlUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    RelativeLayout mProgressLayout;
    FetchImages fetchImageTask;
    ArrayList<FeedStructure> imageList = new ArrayList<>();
    GridAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView)findViewById(R.id.gridView);
        mProgressLayout = (RelativeLayout) findViewById(R.id.progress_layout);

        fetchImageTask = new FetchImages();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            fetchImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        else
            fetchImageTask.execute();

    }

    // Fetching flickr images
    private class FetchImages extends AsyncTask<String, String, String> {

        private String resp;
        @Override
        protected void onPreExecute() {
            mProgressLayout.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String... params) {

            try {

                if(isNetworkConnected()) {
                    URL url = new URL(UrlUtils.FEED_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                    StringBuilder builder = new StringBuilder();
                    String inputString;
                    while ((inputString = bufferedReader.readLine()) != null) {
                        builder.append(inputString);
                    }
                    JSONObject feedObject = new JSONObject(builder.toString());
                    parseJsonData(feedObject);
                    urlConnection.disconnect();
                    resp ="success";
                }else{
                    resp ="failed";
                }
            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            if(resp.equalsIgnoreCase("success")) {
                if (imageList != null && imageList.size() != 0) {
                    gridAdapter = new GridAdapter(MainActivity.this, R.layout.grid_item, imageList);
                    mGridView.setAdapter(gridAdapter);
                    mGridView.setVisibility(View.VISIBLE);
                    mProgressLayout.setVisibility(View.GONE);
                }
            }else{
                mProgressLayout.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,getResources().getString(R.string.check_network_connection),Toast.LENGTH_SHORT).show();
            }
        }

    }

// Parse Flickr json data
    public void parseJsonData(JSONObject feedObject) {
        try {
            JSONArray itemArray = feedObject.getJSONArray("items");
            for(int i=0; i<itemArray.length(); i++){
                FeedStructure feedStr = new FeedStructure();
                feedStr.setmImageName(itemArray.getJSONObject(i).getString("title"));
                feedStr.setmImageUrl(itemArray.getJSONObject(i).getJSONObject("media").getString("m"));
                imageList.add(feedStr);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

// Check network availibility
    protected boolean isNetworkConnected() {
        try {
            ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            return (mNetworkInfo == null) ? false : true;

        }catch (NullPointerException e){
            return false;

        }
    }
}
