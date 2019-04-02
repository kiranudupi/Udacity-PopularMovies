package in.kirankumard.popularmovies_udacity.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.Constants.Constants;
import in.kirankumard.popularmovies_udacity.Database.MovieDatabase;
import in.kirankumard.popularmovies_udacity.Model.Movie;
import in.kirankumard.popularmovies_udacity.R;
import in.kirankumard.popularmovies_udacity.Utils.Utils;

public class MovieDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_movie_backdrop)
    ImageView ivMovieBackdrop;

    @BindView(R.id.tv_rating_value)
    TextView tvRatingValue;

    @BindView(R.id.tv_rating_count)
    TextView tvRatingCount;

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_release_date)
    TextView tvReleaseDate;

    @BindView(R.id.tv_overview)
    TextView tvOverview;

    @BindView(R.id.ll_trailers_parent)
    LinearLayout llTrailersParent;

    @BindView(R.id.ll_reviews_parent)
    LinearLayout llReviewsParent;

    @BindView(R.id.iv_favourite)
    ImageView ivFavourite;

    private Movie movie;

    private boolean isMovieFavourite = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupUi();
    }

    private void setupUi() {
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        movie = getIntent().getParcelableExtra(Constants.MOVIE_INTENT_KEY);
        getSupportActionBar().setTitle(movie.getmOriginalTitle());
        Picasso.with(this).load(getString(R.string.movie_backdrop_url) + movie.getmBackdropPath())
                .placeholder(R.drawable.picasso_placeholder)
                .error(R.drawable.picasso_error)
                .fit()
                .into(ivMovieBackdrop);
        tvTitle.setText(movie.getmOriginalTitle());
        tvRatingValue.setText(String.format(Locale.US, "%.2f", movie.getmVoteAverage()));
        tvRatingCount.setText(getString(R.string.movie_vote, movie.getmVoteCount()));
        tvReleaseDate.setText(getString(R.string.movie_release_date, movie.getmReleaseDate()));
        tvOverview.setText(movie.getmOverview());

        GetTrailersAsyncTask getTrailersAsyncTask = new GetTrailersAsyncTask(movie.getmId());
        getTrailersAsyncTask.execute();

        GetReviewsAsyncTask getReviewsAsyncTask = new GetReviewsAsyncTask(movie.getmId());
        getReviewsAsyncTask.execute();

        new GetFavouriteInformation().execute(movie.mId);
    }

    private void setUpFavouriteButton(Boolean movieCount) {
        if (!movieCount) {
            ivFavourite.setImageResource(R.drawable.baseline_favorite_border_white_24dp);
            ivFavourite.setColorFilter(ContextCompat.getColor(MovieDetailActivity.this, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY);
            isMovieFavourite = false;
        } else {
            ivFavourite.setImageResource(R.drawable.baseline_favorite_white_24dp);
            ivFavourite.setColorFilter(ContextCompat.getColor(MovieDetailActivity.this, R.color.favourite), android.graphics.PorterDuff.Mode.MULTIPLY);
            isMovieFavourite = true;
        }
        ivFavourite.setVisibility(ImageView.VISIBLE);
        ivFavourite.setOnClickListener(this);
    }

    private class GetFavouriteInformation extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Integer... ints) {
            MovieDatabase movieDatabase = MovieDatabase.getInstance(MovieDetailActivity.this);
            List<Movie> movies = movieDatabase.dao().getMovieById(ints[0]);
            return movies.size() != 0;
        }

        @Override
        protected void onPostExecute(Boolean movieCount) {
            super.onPostExecute(movieCount);
            setUpFavouriteButton(movieCount);
        }
    }


    private void showTrailers(ArrayList<String> trailersArray) {
        if (trailersArray != null) {
            for (int i = 0; i < trailersArray.size(); i++) {
                TextView trailer = new TextView(MovieDetailActivity.this);
                LinearLayout.LayoutParams trailerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                trailer.setLayoutParams(trailerLayoutParams);
                trailer.setPadding(8, 16, 8, 16);
                trailer.setText(getResources().getString(R.string.trailer_prefix, (i + 1)));
                trailer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_play_arrow_black_18dp, 0, 0, 0);
                String url = getResources().getString(R.string.trailers_url) + trailersArray.get(i);
                trailer.setOnClickListener(v -> {
                    Intent trailerIntent = new Intent(Intent.ACTION_VIEW);
                    trailerIntent.setData(Uri.parse(url));
                    if(trailerIntent.resolveActivity(getPackageManager()) != null)
                        startActivity(trailerIntent);
                });
                llTrailersParent.addView(trailer);
            }
        }
    }

    private void showReviews(ArrayList<String> reviewsArray) {
        if (reviewsArray != null) {
            for (int i = 0; i < reviewsArray.size(); i++) {
                TextView review = new TextView(MovieDetailActivity.this);
                LinearLayout.LayoutParams trailerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                trailerLayoutParams.setMargins(0, 0, 0, 16);
                review.setLayoutParams(trailerLayoutParams);
                review.setPadding(32, 32, 32, 32);
                review.setBackgroundColor(getResources().getColor(R.color.white));
                review.setText(reviewsArray.get(i));
                llReviewsParent.addView(review);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_favourite:
                new AddRemoveFavourite().execute(isMovieFavourite);
                break;
        }
    }

    private class AddRemoveFavourite extends AsyncTask<Boolean, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Boolean... isFavourited) {
            MovieDatabase movieDatabase = MovieDatabase.getInstance(MovieDetailActivity.this);
            if (isFavourited[0]) {
                movieDatabase.dao().deleteMovie(movie);
                return false;
            } else {
                movieDatabase.dao().insertMovie(movie);
                return true;
            }
        }

        @Override
        protected void onPostExecute(Boolean setFavourite) {
            super.onPostExecute(setFavourite);
            setUpFavouriteButton(setFavourite);
        }
    }

    private class GetTrailersAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        int movieID;
        private String mResponse;

        public GetTrailersAsyncTask(int id) {
            this.movieID = id;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            try {
                URL url = new URL(getResources().getString(R.string.movies_trailer_url_primary) + this.movieID + getResources().getString(R.string.movies_trailer_url_secondary) + getResources().getString(R.string.api_key));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    mResponse = Utils.convertInputStreamToString(inputStream);
                    return Utils.parseTrailerJson(mResponse);
                } else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            showTrailers(strings);
        }
    }

    public class GetReviewsAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        int movieID;
        private String mResponse;

        public GetReviewsAsyncTask(int id) {
            this.movieID = id;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            try {
                URL url = new URL(getResources().getString(R.string.movies_reviews_url_primary) + this.movieID + getResources().getString(R.string.movies_reviews_url_secondary) + getResources().getString(R.string.api_key));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    mResponse = Utils.convertInputStreamToString(inputStream);
                    return Utils.parseReviewsJson(mResponse);
                } else {
                    return null;
                }
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            showReviews(strings);
        }
    }
}
