package in.kirankumard.popularmovies_udacity.Asynctasks;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import in.kirankumard.popularmovies_udacity.Interfaces.GetMovieDataInterface;
import in.kirankumard.popularmovies_udacity.Utils.Utils;

public class GetMovieDataAsyncTask extends AsyncTask<Void, Void, Void> {

    private String mUrl;
    private GetMovieDataInterface mGetMovieDataInterface;
    private String mResponse;

    public GetMovieDataAsyncTask(String url, GetMovieDataInterface getMovieDataInterface)
    {
        this.mUrl = url;
        mGetMovieDataInterface = getMovieDataInterface;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL(mUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestMethod("POST");

            int statusCode = urlConnection.getResponseCode();

            if (statusCode == 200) {

                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                mResponse = Utils.convertInputStreamToString(inputStream);
                mGetMovieDataInterface.getMovieDataCompletionHandler(true, mResponse);
            } else {
                mGetMovieDataInterface.getMovieDataCompletionHandler(false, mResponse);
            }
        } catch (IOException e) {
            mGetMovieDataInterface.getMovieDataCompletionHandler(false, mResponse);
        }
        return null;
    }

}
