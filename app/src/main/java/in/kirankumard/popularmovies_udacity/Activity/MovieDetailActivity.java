package in.kirankumard.popularmovies_udacity.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.Constants.Constants;
import in.kirankumard.popularmovies_udacity.Model.Movie;
import in.kirankumard.popularmovies_udacity.R;

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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupUi();
    }

    private void setupUi()
    {
        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Movie movie = (Movie)getIntent().getParcelableExtra(Constants.MOVIE_INTENT_KEY);
        getSupportActionBar().setTitle(movie.getmOriginalTitle());
        Picasso.with(this).load(getString(R.string.movie_backdrop_url) + movie.getmBackdropPath())
                .placeholder(R.drawable.picasso_placeholder)
                .error(R.drawable.picasso_error)
                .into(ivMovieBackdrop);
        tvTitle.setText(movie.getmOriginalTitle());
        tvRatingValue.setText(String.format(Locale.US,"%.2f",movie.getmVoteAverage()));
        tvRatingCount.setText(getString(R.string.movie_vote, movie.getmVoteCount()));
        tvReleaseDate.setText(getString(R.string.movie_release_date,movie.getmReleaseDate()));
        tvOverview.setText(movie.getmOverview());

    }
}
