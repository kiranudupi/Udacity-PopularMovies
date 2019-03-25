package in.kirankumard.popularmovies_udacity.Activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.Constants.Constants;
import in.kirankumard.popularmovies_udacity.Model.Movie;
import in.kirankumard.popularmovies_udacity.R;
import in.kirankumard.popularmovies_udacity.Utils.Utils;

public class MovieDetailActivity extends AppCompatActivity {

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
        Movie movie = (Movie) getIntent().getParcelableExtra(Constants.MOVIE_INTENT_KEY);
        getSupportActionBar().setTitle(movie.getmOriginalTitle());
        Picasso.with(this).load(getString(R.string.movie_backdrop_url) + movie.getmBackdropPath())
                .placeholder(R.drawable.picasso_placeholder)
                .error(R.drawable.picasso_error)
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

    }

    void showTrailers(ArrayList<String> trailersArray) {
        if(trailersArray != null)
        {
            for (int i = 0; i < 3; i++) {
                TextView trailer = new TextView(MovieDetailActivity.this);
                LinearLayout.LayoutParams trailerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                trailer.setLayoutParams(trailerLayoutParams);
                trailer.setPadding(8, 16, 8, 16);
                trailer.setText("Trailer " + (i+1));
                trailer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_play_arrow_black_18dp, 0, 0, 0);
                //String url = getResources().getString(R.string.trailers_url) + trailersArray.get(i);
                String url = "https://www.youtube.com/watch?v=K_tLp7T6U1c";
                trailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                llTrailersParent.addView(trailer);
            }
        }



    }

    void showReviews(ArrayList<String> reviewsArray) {
        if(reviewsArray == null)
        {
            for (int i = 0; i < 3; i++) {
                TextView trailer = new TextView(MovieDetailActivity.this);
                LinearLayout.LayoutParams trailerLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                trailerLayoutParams.setMargins(0,0,0,16);
                trailer.setLayoutParams(trailerLayoutParams);
                trailer.setPadding(8,8,8,8);
                trailer.setBackgroundColor(getResources().getColor(R.color.white));
                trailer.setText("dasdasd aaksd ajd ad akd akd ad askd akd ajskd akjsd ask daskjdaksjdjasdkasdjkad askd askj dadaskdakjsdjkasdkasdkasdkas");
                //trailer.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_play_arrow_black_18dp, 0, 0, 0);
                //String url = getResources().getString(R.string.trailers_url) + trailersArray.get(i);
                String url = "https://www.youtube.com/watch?v=K_tLp7T6U1c";
                trailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                llReviewsParent.addView(trailer);
            }
        }



    }

    public class GetTrailersAsyncTask extends AsyncTask<Void, Void, ArrayList<String>> {

        int movieID;
        private String mResponse;

        public GetTrailersAsyncTask(int id) {
            this.movieID = id;
        }

        @Override
        protected ArrayList<String> doInBackground(Void... voids) {
            try {
                URL url = new URL(getResources().getString(R.string.movies_trailer_url_primary) + this.movieID + getResources().getString(R.string.movies_trailer_url_secondary) + getResources().getString(R.string.api_key));
                Log.v("responseabc", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");

                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    mResponse = Utils.convertInputStreamToString(inputStream);

                    Log.v("responseabc", mResponse);
                    Log.v("responseabc", "");
                    return Utils.parseTrailerJson(mResponse);
                } else {
                    Log.v("responseabc", "2 " + statusCode);
                }
            } catch (
                    IOException e) {
                Log.v("responseabc", "3");
            }
            finally {

            }
            return null;
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
                Log.v("responseabc", url.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestMethod("POST");

                int statusCode = urlConnection.getResponseCode();

                if (statusCode == 200) {

                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    mResponse = Utils.convertInputStreamToString(inputStream);

                    Log.v("responseabc", mResponse);
                    Log.v("responseabc", "4");
                    return Utils.parseReviewsJson(mResponse);
                } else {
                    Log.v("responseabc", "5 " + statusCode);
                }
            } catch (
                    IOException e) {
                Log.v("responseabc", "6");
            }
            finally {

            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            showReviews(strings);
        }
    }

}
