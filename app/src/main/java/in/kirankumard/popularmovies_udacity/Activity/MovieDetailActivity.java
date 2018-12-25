package in.kirankumard.popularmovies_udacity.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.R;

public class MovieDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_movie_backdrop)
    ImageView ivMovieBackdrop;

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
        getSupportActionBar().setTitle("Bumblebee");
        Picasso.with(this).load("http://image.tmdb.org/t/p/original//VuukZLgaCrho2Ar8Scl9HtV3yD.jpg").into(ivMovieBackdrop);
    }
}
