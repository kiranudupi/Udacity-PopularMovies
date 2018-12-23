package in.kirankumard.popularmovies_udacity.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import in.kirankumard.popularmovies_udacity.Constants.Constants;
import in.kirankumard.popularmovies_udacity.Model.Movie;

public class Utils {

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static String convertInputStreamToString(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static ArrayList<Movie> parseMovieJson(String movieJsonString)
    {
        ArrayList<Movie> moviesArrayList = new ArrayList<Movie>();
        try {
            JSONObject response = new JSONObject(movieJsonString);
            JSONArray results = response.getJSONArray(Constants.RESULT_KEY);
            for(int i = 0; i < results.length(); i++)
            {
                Movie movie = new Movie(results.getJSONObject(i).getInt(Constants.ID_KEY),results.getJSONObject(i).getInt(Constants.VOTE_COUNT_KEY),results.getJSONObject(i).getDouble(Constants.VOTE_AVERAGE_KEY),results.getJSONObject(i).getDouble(Constants.POPULARITY_KEY),results.getJSONObject(i).getString(Constants.POSTER_PATH_KEY),results.getJSONObject(i).getString(Constants.TITLE_KEY),results.getJSONObject(i).getString(Constants.OVERVIEW_KEY),results.getJSONObject(i).getString(Constants.RELEASE_DATE_KEY));
                moviesArrayList.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return moviesArrayList;
    }

}
