package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.flixster.databinding.ActivityMovieDetailsBinding;
import com.example.flixster.models.Movie;

import org.parceler.Parcels;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    // declaring views before viewbinding
//    TextView tvTitleDetail;
//    TextView tvOverviewDetail;
//    RatingBar rbVoteAverage;
//    Button backBtn;

    // view binding stuff
    ActivityMovieDetailsBinding activityDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_movie_details);

        activityDetailsBinding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());

        // layout of activity is stored in a special property called root
        View view = activityDetailsBinding.getRoot();
        setContentView(view);

        // back button functionality
        // backBtn = (Button) findViewById(R.id.backBtn);
        activityDetailsBinding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewActivity();
            }
        });

        // unwrap the movie passed in via intent using its simple name
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // resolving the view objects, before viewbinding
//        tvTitleDetail = (TextView) findViewById(R.id.tvTitleDetail);
//        tvOverviewDetail = (TextView) findViewById(R.id.tvOverviewDetail);
//        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);

        // set the title and overview, before viewbinding
//        tvTitleDetail.setText(movie.getTitle());
//        tvOverviewDetail.setText(movie.getOverview());

        // WITH VIEW BINDING
        activityDetailsBinding.tvTitleDetail.setText(movie.getTitle());
        activityDetailsBinding.tvOverviewDetail.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        activityDetailsBinding.rbVoteAverage.setRating(voteAverage / 2.0f);

        // rbVoteAverage.setRating(voteAverage / 2.0f); // before viewbinding
    }

    public void openNewActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}