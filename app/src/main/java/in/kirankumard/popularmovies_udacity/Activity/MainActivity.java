package in.kirankumard.popularmovies_udacity.Activity;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.Adapters.MoviesAdapter;
import in.kirankumard.popularmovies_udacity.Asynctasks.GetMovieDataAsyncTask;
import in.kirankumard.popularmovies_udacity.Constants.Constants;
import in.kirankumard.popularmovies_udacity.Database.MovieDatabase;
import in.kirankumard.popularmovies_udacity.Interfaces.GetMovieDataInterface;
import in.kirankumard.popularmovies_udacity.Interfaces.MovieClickListerner;
import in.kirankumard.popularmovies_udacity.Model.Movie;
import in.kirankumard.popularmovies_udacity.R;
import in.kirankumard.popularmovies_udacity.Utils.SpacesItemDecoration;
import in.kirankumard.popularmovies_udacity.Utils.Utils;
import in.kirankumard.popularmovies_udacity.Viewmodel.FavouriteViewModel;

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
    List<Movie> favouriteMoviesArrayList = null;

    private MoviesAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    private boolean isFavourite = false;

    FavouriteViewModel favouriteViewModel;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(Constants.IS_FAVOURITE_SELECTED_KEY, isFavourite);
//        if(favouriteMoviesArrayList != null)IS_FAVOURITE_SELECTED_KEY
//            outState.putParcelableArrayList(Constants.BUNDLE_FAVOURITE_MOVIES_ARRAYLIST_KEY, favouriteMoviesArrayList);
        if (isFavourite)
            outState.putBoolean(Constants.BUNDLE_IS_FAVOURITE_ACTIVE, true);
        if (moviesArrayList != null)
            outState.putParcelableArrayList(Constants.BUNDLE_MOVIES_ARRAYLIST_KEY, moviesArrayList);
        if (mLayoutManager != null)
            outState.putParcelable(Constants.BUNDLE_RECYCLERVIEW_POSITION, mLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupUi();

        if(savedInstanceState == null)
        {
            loadMovies(R.string.movied_db_url_popularity);
        }
        else
        {
            if(savedInstanceState.containsKey(Constants.BUNDLE_IS_FAVOURITE_ACTIVE) && savedInstanceState.getBoolean(Constants.BUNDLE_IS_FAVOURITE_ACTIVE))
            {
                isFavourite = true;
                setUpFavouriteViewModel();
                //new GetFavouriteMovies().execute();
            }
            else if (!savedInstanceState.containsKey(Constants.BUNDLE_MOVIES_ARRAYLIST_KEY))
                loadMovies(R.string.movied_db_url_popularity);
            else {
                moviesArrayList = savedInstanceState.getParcelableArrayList(Constants.BUNDLE_MOVIES_ARRAYLIST_KEY);
                mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(Constants.BUNDLE_RECYCLERVIEW_POSITION));
                showMovies();
            }
        }
//        if(savedInstanceState != null)
//        {
//            if(savedInstanceState.containsKey(Constants.BUNDLE_IS_FAVOURITE_ACTIVE) && savedInstanceState.getBoolean(Constants.BUNDLE_IS_FAVOURITE_ACTIVE))
//            {
//                isFavourite = true;
//                new GetFavouriteMovies().execute();
//            }
//            else
//            {
//                if (savedInstanceState == null || !savedInstanceState.containsKey(Constants.BUNDLE_MOVIES_ARRAYLIST_KEY))
//                    loadMovies(R.string.movied_db_url_popularity);
//                else {
//                    moviesArrayList = savedInstanceState.getParcelableArrayList(Constants.BUNDLE_MOVIES_ARRAYLIST_KEY);
//                    mLayoutManager.onRestoreInstanceState(savedInstanceState.getParcelable(Constants.BUNDLE_RECYCLERVIEW_POSITION));
//                    showMovies();
//                }
//            }
//        }


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
                            getSupportActionBar().setTitle(getResources().getString(R.string.app_title_popular));
                            loadMovies(R.string.movied_db_url_popularity);
                            isFavourite = false;
                            return true;
                        case R.id.rating:
                            getSupportActionBar().setTitle(getResources().getString(R.string.app_title_top_rated));
                            loadMovies(R.string.movied_db_url_rating);
                            isFavourite = false;
                            return true;
                        case R.id.favourites:
                            getSupportActionBar().setTitle(getResources().getString(R.string.app_title_favourites));
                            //showFavouriteMovies(favouriteMoviesArrayList);
                            //loadFavourites();
                            setUpFavouriteViewModel();
                            isFavourite = true;
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

    @Override
    protected void onResume() {
        super.onResume();
//        if(isFavourite)
//        {
//            mAdapter.moviesArrayList = favouriteMoviesArrayList;
//            mAdapter.notifyDataSetChanged();
//        }
    }

    private void setupUi() {
        //loadFavourites();
        getSupportActionBar().setTitle(getResources().getString(R.string.app_title_popular));
        ButterKnife.bind(this);
        rvMoviesRecyclerView.setVisibility(View.VISIBLE);
        rvMoviesRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this, 2);
        rvMoviesRecyclerView.setLayoutManager(mLayoutManager);
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.movie_poster_spacing);
//        rvMoviesRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        tvRetryMovies.setOnClickListener(this);
    }

    private void loadMovies(int sortOrder) {
        if (Utils.isConnectedToInternet(this)) {
            llErrorMessageParent.setVisibility(View.GONE);
            pbLoadingMovies.setVisibility(View.VISIBLE);
            new GetMovieDataAsyncTask(getString(sortOrder) + getString(R.string.api_key), this).execute();
        } else {
            showErrorMessage(getString(R.string.no_internet_connection));
        }


    }

    private void loadFavourites() {
        setUpFavouriteViewModel();
        // new GetFavouriteMovies().execute();
    }

    private void showErrorMessage(String errorMessage) {
        tvErrorMessage.setText(errorMessage);
        llErrorMessageParent.setVisibility(View.VISIBLE);
        pbLoadingMovies.setVisibility(View.GONE);
        rvMoviesRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_retry_movies:
                loadMovies(R.string.movied_db_url_popularity);
                break;
        }
    }


    @Override
    public void getMovieDataCompletionHandler(Boolean success, String response) {
        if (success) {
            moviesArrayList = Utils.parseMovieJson(response);
            Collections.sort(moviesArrayList, (Movie o1, Movie o2) -> Double.compare(o2.getmPopularity(), o1.getmPopularity()));
            showMovies();
        } else {
            runOnUiThread(() -> showErrorMessage(getString(R.string.failed_to_retrieve)));
        }
    }

    private void showMovies() {
        mAdapter = new MoviesAdapter(MainActivity.this, moviesArrayList, this);
        runOnUiThread(() -> {
            llErrorMessageParent.setVisibility(View.GONE);
            rvMoviesRecyclerView.setVisibility(View.VISIBLE);
            rvMoviesRecyclerView.setAdapter(mAdapter);
            pbLoadingMovies.setVisibility(View.GONE);
        });

    }

    private void showFavouriteMovies(List<Movie> movies) {
        mAdapter = new MoviesAdapter(MainActivity.this, movies, this);
        runOnUiThread(() -> {
            llErrorMessageParent.setVisibility(View.GONE);
            rvMoviesRecyclerView.setVisibility(View.VISIBLE);
            rvMoviesRecyclerView.setAdapter(mAdapter);
            pbLoadingMovies.setVisibility(View.GONE);
        });

    }

    @Override
    public void onMovieClick(int clickedMovieIndex) {
        Movie movie = isFavourite ? favouriteMoviesArrayList.get(clickedMovieIndex) : moviesArrayList.get(clickedMovieIndex);
        Intent movieDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        movieDetailIntent.putExtra(Constants.MOVIE_INTENT_KEY, movie);
        startActivity(movieDetailIntent);
    }

    private void setUpFavouriteViewModel() {
        favouriteViewModel = ViewModelProviders.of(MainActivity.this).get(FavouriteViewModel.class);
        favouriteViewModel.getFavouriteMovies().observe(MainActivity.this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                //showFavouriteMovies(movies);
Log.i("responseabc", "loaded from viewmodel");
                mAdapter = new MoviesAdapter(MainActivity.this, movies, MainActivity.this);
                rvMoviesRecyclerView.setAdapter(mAdapter);
            }
        });
    }

//    private void setupFavouriteViewModel()
//    {
//        favouriteViewModel = ViewModelProviders.of(MainActivity.this).get(FavouriteViewModel.class);
//        favouriteViewModel.getFavouriteMovies().observe(MainActivity.this, new Observer<List<Movie>>() {
//            @Override
//            public void onChanged(@Nullable List<Movie> movies) {
//                //showFavouriteMovies(movies);
//                favouriteMoviesArrayList = movies;
//                //if(isFavourite)
//                //{
//                mAdapter = new MoviesAdapter(MainActivity.this, movies, MainActivity.this);
//                //mAdapter.moviesArrayList = movies;
//
//                Log.i("responseabc", "items: " + movies.size());
//                //mAdapter.notifyDataSetChanged();
//                //}
//            }
//        });
//    }

    public class GetFavouriteMovies extends AsyncTask<Void, Void, LiveData<List<Movie>>> {

        @Override
        protected LiveData<List<Movie>> doInBackground(Void... voids) {
            MovieDatabase movieDatabase = MovieDatabase.getInstance(MainActivity.this);

//            favouriteMoviesArrayList = movieDatabase.dao().getFavouriteMovies();

            //Log.i("responseabc", "size: " + favouriteMoviesArrayList.getValue().size());
            return null;
        }

        @Override
        protected void onPostExecute(LiveData<List<Movie>> movies) {
            super.onPostExecute(movies);
            favouriteViewModel = ViewModelProviders.of(MainActivity.this).get(FavouriteViewModel.class);
            favouriteViewModel.getFavouriteMovies().observe(MainActivity.this, new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    //showFavouriteMovies(movies);
                    favouriteMoviesArrayList = movies;
                    //if(isFavourite)
                    //{
                    mAdapter = new MoviesAdapter(MainActivity.this, movies, MainActivity.this);
                    //mAdapter.moviesArrayList = movies;

                    Log.i("responseabc", "items: " + movies.size());
                    //mAdapter.notifyDataSetChanged();
                    //}
                }
            });

        }
    }
}
