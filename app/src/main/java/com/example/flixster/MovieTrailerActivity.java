package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public static final String VIDEOS_URL_1 = "https://api.themoviedb.org/3/movie/";
    public static final String VIDEOS_URL_2 = "/videos?api_key=";
    public static final String TAG = "MovieTrailerActivity";
    Movie movie;
    String videoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        // temporary test video id -- TODO replace with movie trailer video id
//        String videoId;

        // intent with parceler to GET info about movie from MovieDetailsActivity
        // unwrap the movie passed in via intent using its simple name
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieTrailerActivity", String.format("Showing movie/YouTube for '%s'", movie.getTitle()));
        int movieId = movie.getId();

        // API call to /movie/{movie_id}/videos endpoint
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(VIDEOS_URL_1 + movieId + VIDEOS_URL_2 + getString(R.string.movie_api_key), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonObject = json.jsonObject;
                try {
                    // getting the resulting list of videos for this movie
                    JSONArray results = jsonObject.getJSONArray("results");
                    Log.i(TAG, "Results: " + results.toString());

                    // getting the video from this JSON array
                    JSONObject first = results.getJSONObject(0);

                    // getting the String key from the first video
                    videoId = first.getString("key");

                    Log.i(TAG, "Movie key for YouTube URL: " + videoId);

                    // resolve the player view from the layout
                    YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

                    // initialize with API key stored in secrets.xml
                    playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
                        @Override
                        public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                            YouTubePlayer youTubePlayer, boolean b) {
                            // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(videoId);
                        }

                        @Override
                        public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                            YouTubeInitializationResult youTubeInitializationResult) {
                            // log the error
                            Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                        }
                    });

                } catch (JSONException e) {
                    Log.e(TAG, "Hit JSON exception" + e);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.d(TAG, "onFailure");
            }
        });

    }
}