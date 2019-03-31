package in.kirankumard.popularmovies_udacity.Viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import in.kirankumard.popularmovies_udacity.Database.MovieDatabase;
import in.kirankumard.popularmovies_udacity.Model.Movie;

public class FavouriteViewModel extends AndroidViewModel {

    private LiveData<List<Movie>> favouriteMovies;

    public FavouriteViewModel(@NonNull Application application) {
        super(application);
        MovieDatabase movieDatabase = MovieDatabase.getInstance(this.getApplication());
        Log.i("responseabc","retrieving from viewmodel");
        favouriteMovies = movieDatabase.dao().getFavouriteMovies();
    }

    public LiveData<List<Movie>> getFavouriteMovies() {
        return favouriteMovies;
    }
}
