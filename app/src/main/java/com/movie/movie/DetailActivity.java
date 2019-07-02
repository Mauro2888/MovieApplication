package com.movie.movie;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.movie.movie.data.MovieContract;
import com.movie.movie.data.MovieHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnItemClick {
    private static final String API_KEY = "4dfd9cebf53bdac99fad6fabb6f27620";
    public MovieHelper mMovieHelper;
    public SQLiteDatabase mSQLiteDatabase;
    DataModelMovie dataModelMovie;
    String keyTrailer;
    TextView mTextNoReview;
    private TextView mPlot;
    private ImageView mImage;
    private TextView mVote;
    private TextView mTitle;
    private TextView mDateRelease;
    private Button mBtnAddPref;
    private Button mBtnReview;
    private RecyclerView mRecyclerTrailer;
    private RecyclerView.LayoutManager mLayoutRecTrailer;
    private List<DataModelTrailer> mListData;
    private AdaperTrailer mAdapterTrailer;
    private RecyclerView mRecyclerReview;
    private RecyclerView.LayoutManager mLayoutReview;
    private AdaperReview mAdapterReview;
    private List<DataModelReview> mListReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



        mBtnAddPref = (Button) findViewById(R.id.btnPreference);
        mPlot = (TextView)findViewById(R.id.plotMovieTxt);
        mImage = (ImageView)findViewById(R.id.imageViewDetail);
        mVote = (TextView)findViewById(R.id.voteMovieTxt);
        mTitle = (TextView)findViewById(R.id.titleMovieTxt);
        mDateRelease = (TextView)findViewById(R.id.dateReleaseTxt);
        mRecyclerTrailer = (RecyclerView) findViewById(R.id.recycler_trailer);
        mLayoutRecTrailer = new LinearLayoutManager(this);
        mRecyclerTrailer.setHasFixedSize(true);
        mRecyclerTrailer.setLayoutManager(mLayoutRecTrailer);

        mRecyclerReview = (RecyclerView) findViewById(R.id.recycler_review);
        mLayoutReview = new LinearLayoutManager(this);
        mRecyclerReview.setLayoutManager(mLayoutReview);
        mRecyclerReview.setHasFixedSize(true);
        mTextNoReview = (TextView) findViewById(R.id.textNoReview);



        //TODO GET DATA FROM MAIN ACTIVITY
        Intent intent = getIntent();
        dataModelMovie = (DataModelMovie) intent.getSerializableExtra("MovieDetail");
        mTitle.setText(dataModelMovie.getmTitle());
        Glide.with(this).load(dataModelMovie.getImgMovie()).centerCrop().into(mImage);
        mVote.setText(dataModelMovie.getmVote() + "/10");
        mTitle.setText(dataModelMovie.getmTitle());
        mPlot.setText(dataModelMovie.getmPlot());
        mDateRelease.setText(dataModelMovie.getmDate());
        setTitle(dataModelMovie.getmTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mBtnAddPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InsertData();
                Toast.makeText(DetailActivity.this, mTitle.getText() + " added to Favourite", Toast.LENGTH_SHORT).show();

            }
        });
        new TrailerTask().execute(Video_Url());
        new AsyncTaskReview().execute(Review_url());
    }

    @Override
    public void OnItemClicked(View view, int pos) {
        String Url = mListData.get(pos).getmTrailerUri();
        Intent play = new Intent(Intent.ACTION_VIEW);
        play.setData(Uri.parse(Url));
        startActivity(play);

    }

    public void InsertData() {
        mMovieHelper = new MovieHelper(this);
        mSQLiteDatabase = mMovieHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", dataModelMovie.getmTitle());
        contentValues.put("poster", dataModelMovie.getImgMovie());
        contentValues.put("date", dataModelMovie.getmDate());
        contentValues.put("rate", dataModelMovie.getmVote());
        getContentResolver().insert(MovieContract.MovieEntry.URI_CONTENT, contentValues);

    }

    public String Review_url() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.themoviedb.org");
        builder.appendPath("3");
        builder.appendPath("movie");
        builder.appendPath(dataModelMovie.getmTrailer());
        builder.appendPath("reviews");
        builder.appendQueryParameter("api_key", API_KEY);
        String review = builder.build().toString();
        return review;
    }

    public String Video_Url() {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("api.themoviedb.org");
        builder.appendPath("3");
        builder.appendPath("movie");
        //TODO Add ID TRAILER from Data model and create uri
        builder.appendPath(dataModelMovie.getmTrailer());
        builder.appendQueryParameter("api_key", API_KEY);
        builder.appendQueryParameter("append_to_response", "videos");
        String URLID = builder.build().toString();
        return URLID;
    }

    public String Youtube_url(String key) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https");
        builder.authority("www.youtube.com");
        builder.appendPath("watch");
        builder.appendQueryParameter("v", key);
        String TrailerLink = builder.build().toString();
        return TrailerLink;
    }

    public void JsonReview(String jsonString) {
        try {
            JSONObject jsonObj = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObj.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonReview = jsonArray.getJSONObject(i);
                String author = jsonReview.getString("author");
                String content = jsonReview.getString("content");
                String urlReview = jsonReview.getString("url");
                mListReview.add(new DataModelReview(author, content, urlReview));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void JsonTrailer(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONObject objectVideos = jsonObject.getJSONObject("videos");
            JSONArray jsonArray = objectVideos.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objects = jsonArray.getJSONObject(i);
                String titleTrailer = objects.getString("name");
                keyTrailer = objects.getString("key");
                //TODO ADD to LIST
                mListData.add(new DataModelTrailer(titleTrailer, Youtube_url(keyTrailer)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, Youtube_url(keyTrailer));
                share.setType("text/plain");
                startActivity(Intent.createChooser(share, dataModelMovie.getmTitle() + " Trailer"));
        }
        return super.onOptionsItemSelected(item);
    }

    public class TrailerTask extends AsyncTask<String, Object, String> {
        @Override
        protected void onPreExecute() {
            //TODO ARRAYLIST
            mListData = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... voids) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(voids[0]);
                StringBuilder stringBuilder = new StringBuilder();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputS = urlConnection.getInputStream();
                    BufferedReader bf = new BufferedReader(new InputStreamReader(inputS));
                    String line;
                    while ((line = bf.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return stringBuilder.toString();
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
        protected void onPostExecute(String result) {
            JsonTrailer(result);
            mAdapterTrailer = new AdaperTrailer(getApplicationContext(), mListData);
            mRecyclerTrailer.setAdapter(mAdapterTrailer);
            mAdapterTrailer.setmClickListener(DetailActivity.this);
            super.onPostExecute(result);
        }
    }

    public class AsyncTaskReview extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            mListReview = new ArrayList<>();
            Log.i("array", mListReview.toString());
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... voids) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(voids[0]);

                StringBuilder stringBuilder = new StringBuilder();
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(15000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputS = urlConnection.getInputStream();
                    BufferedReader bf = new BufferedReader(new InputStreamReader(inputS));
                    String line;
                    while ((line = bf.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    return stringBuilder.toString();
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
        protected void onPostExecute(String s) {
            JsonReview(s);

            mAdapterReview = new AdaperReview(getApplicationContext(), mListReview);
            mRecyclerReview.setAdapter(mAdapterReview);
            if (mListReview.size() <= 0) {
                Toast.makeText(getApplicationContext(), R.string.no_review, Toast.LENGTH_LONG).show();
                mTextNoReview.setVisibility(View.VISIBLE);

            }
            super.onPostExecute(s);
        }
    }
}
