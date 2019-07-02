package com.movie.movie;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivityMovie extends AppCompatActivity implements OnItemClick {


    private static final String API_KEY = "4dfd9cebf53bdac99fad6fabb6f27620";
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private final String KEY_LIST_STATE = "list_state";
    private RecyclerView mRecyclerView;
    private List<DataModelMovie> mDataMovieList;
    private AdapterMovie mAdapterMovie;
    private RecyclerView.LayoutManager mLayoutManager;

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns >= 2 ? noOfColumns : 2;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_movie);


        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mLayoutManager = new GridLayoutManager(this, calculateNoOfColumns(getBaseContext()));
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (NetUtils(getBaseContext())) {
            new AsyncTaskMovie().execute(UrlNowPlayingMovie());
            MainActivityMovie.this.setTitle(R.string.nowPlay);
        }
    }

    @Override
    public void OnItemClicked(View view, int pos) {
        Intent detail = new Intent(this,DetailActivity.class);
        detail.putExtra("MovieDetail",mDataMovieList.get(pos));
        startActivity(detail);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (NetUtils(getApplicationContext())== false){
            menu.findItem(R.id.most_popular).setEnabled(false);
            menu.findItem(R.id.top_rated).setEnabled(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                //TODO MOST POPULAR
                if (NetUtils(getApplicationContext())) {
                    MainActivityMovie.this.setTitle(R.string.mostPopular);
                    mDataMovieList.clear();
                    mAdapterMovie.notifyDataSetChanged();
                    new AsyncTaskMovie().execute(URL_POPULAR_MOVIE());
                }
                break;
            case R.id.top_rated:
                //TODO MOST RATED
                if (NetUtils(getApplicationContext())) {
                    MainActivityMovie.this.setTitle(R.string.topRated);
                    mDataMovieList.clear();
                    mAdapterMovie.notifyDataSetChanged();
                    new AsyncTaskMovie().execute(URL_TOP_RATED_MOVIE());
                }
                break;
            case R.id.favourite:
                Intent favourite = new Intent(MainActivityMovie.this, FavouriteActivity.class);
                startActivity(favourite);
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    public String URL_POPULAR_MOVIE() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.themoviedb.org");
        builder.appendPath("3");
        builder.appendPath("movie");
        builder.appendPath("popular");
        builder.appendQueryParameter("api_key", API_KEY);
        //builder.appendQueryParameter("language", "it");
        builder.appendQueryParameter("append_to_response", "videos");
        String URL = builder.build().toString();
        return URL;
    }

    public String UrlNowPlayingMovie() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.themoviedb.org");
        builder.appendPath("3");
        builder.appendPath("movie");
        builder.appendPath("now_playing");
        builder.appendQueryParameter("api_key", API_KEY);
        //builder.appendQueryParameter("language", "it");
        String URL = builder.build().toString();
        return URL;
    }

    public String URL_TOP_RATED_MOVIE() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.themoviedb.org");
        builder.appendPath("3");
        builder.appendPath("movie");
        builder.appendPath("top_rated");
        builder.appendQueryParameter("api_key", API_KEY);
        //builder.appendQueryParameter("language", "it");
        String URL = builder.build().toString();
        return URL;
    }

    //TODO URL METOD IMAGE
    public String IMG_URL(String img_path) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("image.tmdb.org");
        builder.appendPath("t");
        builder.appendPath("p");
        builder.appendPath("w342");
        return builder + img_path;
    }


    //TODO PARSING JSON
    public void Json_Data(String json) {
        try {
            JSONObject jsObj = new JSONObject(json);
            JSONArray jsArray = jsObj.getJSONArray("results");
            for (int i = 0; i < jsArray.length(); i++) {
                JSONObject jsObjResult = jsArray.getJSONObject(i);
                String title = jsObjResult.getString("title");
                String path_img = jsObjResult.getString("poster_path");
                String release_date = jsObjResult.getString("release_date");
                String vote_average = jsObjResult.getString("vote_average");
                String plot = jsObjResult.getString("overview");
                String id_trailer = jsObjResult.getString("id");
                IMG_URL(path_img);

                mDataMovieList.add(new DataModelMovie(IMG_URL(path_img), title, plot, release_date, vote_average, id_trailer, 0));

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean NetUtils(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null && info.isConnected()){
            return true;
        }else {
            Toast.makeText(this, R.string.check_conn, Toast.LENGTH_SHORT).show();
            return false;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable saveIn = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putSerializable(KEY_LIST_STATE, (ArrayList<? extends Serializable>) mDataMovieList);
        outState.putParcelable(KEY_RECYCLER_STATE, saveIn);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable restore = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            savedInstanceState.getSerializable(KEY_LIST_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(restore);
        }
    }


    //TODO ASYNC
    public class AsyncTaskMovie extends AsyncTask<String, Object, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivityMovie.this);
            progressDialog.setMessage("Download Movie Data");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            mDataMovieList = new ArrayList<>();
        }

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder stringBuilder = new StringBuilder();
            String resultConn = null;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                        resultConn = stringBuilder.toString();
                    }
                    return resultConn;
                } else {
                    urlConnection.disconnect();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        //TODO POST EXECUTE
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            Json_Data(result);
            mAdapterMovie = new AdapterMovie(mDataMovieList, getApplicationContext());
            mRecyclerView.setAdapter(mAdapterMovie);
            mAdapterMovie.setClickListener(MainActivityMovie.this);

        }
    }

}