package in.kirankumard.popularmovies_udacity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.Adapters.MoviesAdapter;
import in.kirankumard.popularmovies_udacity.Asynctasks.GetMovieDataAsyncTask;
import in.kirankumard.popularmovies_udacity.Interfaces.GetMovieDataInterface;
import in.kirankumard.popularmovies_udacity.Model.Movie;
import in.kirankumard.popularmovies_udacity.R;
import in.kirankumard.popularmovies_udacity.Utils.SpacesItemDecoration;
import in.kirankumard.popularmovies_udacity.Utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetMovieDataInterface {

    @BindView(R.id.pb_loading_movies)
    ProgressBar pbLoadingMovies;
    @BindView(R.id.ll_error_message_parent)
    LinearLayout llErrorMessageParent;
    @BindView(R.id.tv_error_message)
    TextView tvErrorMessage;
    @BindView(R.id.rv_movies_recyclerview)
    RecyclerView rvMoviesRecyclerView;

    ArrayList<Movie> moviesArrayList;

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

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
                sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.popularity:
                                Collections.sort(moviesArrayList, new Comparator<Movie>() {
                                    @Override
                                    public int compare(Movie o1, Movie o2) {
                                        return Double.compare(o1.getmPopularity(), o2.getmPopularity());
                                    }
                                });
                                mAdapter.notifyDataSetChanged();
                                return true;
                            case R.id.rating:
                                Collections.sort(moviesArrayList, new Comparator<Movie>() {
                                    @Override
                                    public int compare(Movie o1, Movie o2) {
                                        return Double.compare(o1.getmVoteAverage(), o2.getmVoteAverage());
                                    }
                                });
                                mAdapter.notifyDataSetChanged();
                                return true;
                        }
                        return true;
                    }
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
        mLayoutManager = new GridLayoutManager(this, 2);
        rvMoviesRecyclerView.setLayoutManager(mLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.movie_poster_spacing);
        rvMoviesRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_error_message_parent:
                loadMovies();
                break;
        }
    }


    @Override
    public void getMovieDataCompletionHandler(Boolean success, String response) {
        if (success) {
            moviesArrayList = Utils.parseMovieJson(response);
            mAdapter = new MoviesAdapter(MainActivity.this, moviesArrayList);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    llErrorMessageParent.setVisibility(View.GONE);
                    rvMoviesRecyclerView.setAdapter(mAdapter);
                    pbLoadingMovies.setVisibility(View.GONE);
                }
            });


        } else {
            showErrorMessage(getString(R.string.failed_to_retrieve));
        }
    }
}
