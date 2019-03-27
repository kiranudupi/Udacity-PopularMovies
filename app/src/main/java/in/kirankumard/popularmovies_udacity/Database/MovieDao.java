package in.kirankumard.popularmovies_udacity.Database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import in.kirankumard.popularmovies_udacity.Model.Movie;

@Dao
public interface MovieDao {

    @Query("SELECT * FROM Movies")
    List<Movie> getFavouriteMovies();

    @Insert
    void insertMovie(Movie movie);

    @Delete
    void deleteMovie(Movie movie);

    @Query("SELECT * FROM Movies WHERE MOVIE_ID = :id")
    List<Movie> getMovieById(int id);

}
