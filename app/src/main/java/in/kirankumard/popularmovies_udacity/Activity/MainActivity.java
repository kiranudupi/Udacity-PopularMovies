package in.kirankumard.popularmovies_udacity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.Asynctasks.GetMovieDataAsyncTask;
import in.kirankumard.popularmovies_udacity.Interfaces.GetMovieDataInterface;
import in.kirankumard.popularmovies_udacity.R;
import in.kirankumard.popularmovies_udacity.Utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GetMovieDataInterface {

    @BindView(R.id.pb_loading_movies)
    ProgressBar pbLoadingMovies;
    @BindView(R.id.ll_error_message_parent)
    LinearLayout llErrorMessageParent;
    @BindView(R.id.tv_error_message)
    TextView tvErrorMessage;
    GetMovieDataInterface mGetMovieDataInterface;

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

    private void setupUi() {
        ButterKnife.bind(this);
        llErrorMessageParent.setOnClickListener(this);
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

    private void showErrorMessage(String errorMessage)
    {
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
        if(success)
        {
            llErrorMessageParent.setVisibility(View.GONE);
        }
        else
        {
            showErrorMessage(getString(R.string.failed_to_retrieve));
        }
    }
}
