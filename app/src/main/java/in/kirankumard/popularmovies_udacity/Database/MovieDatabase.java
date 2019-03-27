package in.kirankumard.popularmovies_udacity.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import in.kirankumard.popularmovies_udacity.Model.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static MovieDatabase sInstance;

    public abstract MovieDao dao();

    public static MovieDatabase getInstance(Context ctx)
    {
        if(sInstance == null)
        {
            synchronized (LOCK)
            {
                sInstance = Room.databaseBuilder(ctx.getApplicationContext(),MovieDatabase.class,"movie_database").build();
            }
        }
        return sInstance;
    }

}
