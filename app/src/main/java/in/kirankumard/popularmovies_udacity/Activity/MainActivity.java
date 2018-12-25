package in.kirankumard.popularmovies_udacity.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.Adapters.MoviesAdapter;
import in.kirankumard.popularmovies_udacity.Asynctasks.GetMovieDataAsyncTask;
import in.kirankumard.popularmovies_udacity.Constants.Constants;
import in.kirankumard.popularmovies_udacity.Interfaces.GetMovieDataInterface;
import in.kirankumard.popularmovies_udacity.Interfaces.MovieClickListerner;
import in.kirankumard.popularmovies_udacity.Model.Movie;
import in.kirankumard.popularmovies_udacity.R;
import in.kirankumard.popularmovies_udacity.Utils.SpacesItemDecoration;
import in.kirankumard.popularmovies_udacity.Utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetMovieDataInterface, MovieClickListerner {

    @BindView(R.id.pb_loading_movies)
    ProgressBar pbLoadingMovies;
    @BindView(R.id.ll_error_message_parent)
    LinearLayout llErrorMessageParent;
    @BindView(R.id.tv_error_message)
    TextView tvErrorMessage;
    @BindView(R.id.rv_movies_recyclerview)
    RecyclerView rvMoviesRecyclerView;
    @BindView(R.id.tv_retry_movies)
    TextView tvRetryMovies;

    ArrayList<Movie> moviesArrayList;

    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();
        loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_movies:
                PopupMenu sortMenu = new PopupMenu(MainActivity.this, findViewById(R.id.sort_movies));
                sortMenu.getMenuInflater().inflate(R.menu.sort_menu, sortMenu.getMenu());
                sortMenu.setOnMenuItemClickListener((MenuItem menuItem) -> {
                    switch (menuItem.getItemId()) {
                        case R.id.popularity:
                            try {
                                Collections.sort(moviesArrayList, (Movie o1, Movie o2) -> Double.compare(o2.getmPopularity(), o1.getmPopularity()));
                                mAdapter.notifyDataSetChanged();
                            } catch (Exception ignored) {

                            }

                            return true;
                        case R.id.rating:
                            try {
                                Collections.sort(moviesArrayList, (Movie o1, Movie o2) -> Double.compare(o2.getmVoteAverage(), o1.getmVoteAverage()));
                                mAdapter.notifyDataSetChanged();
                            } catch (Exception e) {

                            }
                            return true;
                    }
                    return true;

                });
                sortMenu.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void setupUi() {
        ButterKnife.bind(this);
        llErrorMessageParent.setOnClickListener(this);
        rvMoviesRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvMoviesRecyclerView.setLayoutManager(mLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.movie_poster_spacing);
        rvMoviesRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        tvRetryMovies.setOnClickListener(this);
    }

    private void loadMovies() {
        if (Utils.isConnectedToInternet(this)) {
            llErrorMessageParent.setVisibility(View.GONE);
            pbLoadingMovies.setVisibility(View.VISIBLE);
            new GetMovieDataAsyncTask(getString(R.string.movied_db_url) + getString(R.string.api_key), this).execute();
        } else {
            showErrorMessage(getString(R.string.no_internet_connection));
        }


    }

    private void showErrorMessage(String errorMessage) {
        tvErrorMessage.setText(errorMessage);
        llErrorMessageParent.setVisibility(View.VISIBLE);
        pbLoadingMovies.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_retry_movies:
                loadMovies();
                break;
        }
    }


    @Override
    public void getMovieDataCompletionHandler(Boolean success, String response) {
        if (success) {
            moviesArrayList = Utils.parseMovieJson(response);
            mAdapter = new MoviesAdapter(MainActivity.this, moviesArrayList, this);
            runOnUiThread(() -> {
                llErrorMessageParent.setVisibility(View.GONE);
                rvMoviesRecyclerView.setAdapter(mAdapter);
                pbLoadingMovies.setVisibility(View.GONE);
            });


        } else {
            runOnUiThread(() -> showErrorMessage(getString(R.string.failed_to_retrieve)));
        }
    }

    @Override
    public void onMovieClick(int clickedMovieIndex) {
        Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        movieDetailIntent.putExtra(Constants.MOVIE_INTENT_KEY,moviesArrayList.get(clickedMovieIndex));
        startActivity(movieDetailIntent);
    }
}
