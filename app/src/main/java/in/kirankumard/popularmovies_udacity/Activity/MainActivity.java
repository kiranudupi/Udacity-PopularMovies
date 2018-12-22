package in.kirankumard.popularmovies_udacity.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.kirankumard.popularmovies_udacity.R;
import in.kirankumard.popularmovies_udacity.Utils.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.pb_loading_movies) ProgressBar pbLoadingMovies;
    @BindView(R.id.ll_no_internet_warning)
    LinearLayout llNoInternetWarning;

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

    private void setupUi()
    {
        ButterKnife.bind(this);
        llNoInternetWarning.setOnClickListener(this);
    }

    private void loadMovies()
    {
        if(Utils.isConnectedToInternet(this))
        {
            llNoInternetWarning.setVisibility(View.GONE);
            pbLoadingMovies.setVisibility(View.VISIBLE);
        }
        else
        {
            llNoInternetWarning.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.ll_no_internet_warning:
                loadMovies();
                break;
        }
    }
}
