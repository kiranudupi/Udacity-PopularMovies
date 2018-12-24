package in.kirankumard.popularmovies_udacity.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.kirankumard.popularmovies_udacity.Interfaces.MovieClickListerner;
import in.kirankumard.popularmovies_udacity.Model.Movie;
import in.kirankumard.popularmovies_udacity.R;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private ArrayList<Movie> moviesArrayList;
    private Context mContext;
    final MovieClickListerner movieClickListerner;

    public MoviesAdapter(Context context, ArrayList<Movie> moviesArrayList, MovieClickListerner movieClickListerner)
    {
        this.mContext = context;
        this.moviesArrayList = moviesArrayList;
        this.movieClickListerner = movieClickListerner;
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView ivMoviePoster;
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            movieClickListerner.onMovieClick(getAdapterPosition());
        }


    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(this.mContext).inflate(R.layout.movie_item,viewGroup,false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(frameLayout);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int i) {
        Picasso.with(this.mContext).load(this.mContext.getString(R.string.movie_poster_url) + this.moviesArrayList.get(i).getmPosterPath()).into(movieViewHolder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return moviesArrayList.size();
    }
}
