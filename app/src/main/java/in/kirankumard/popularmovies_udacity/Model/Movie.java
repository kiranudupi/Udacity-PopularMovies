package in.kirankumard.popularmovies_udacity.Model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "Movies")
public class Movie implements Parcelable {
//    @PrimaryKey(autoGenerate = true)
//    public int id;

    @ColumnInfo(name = "MOVIE_ID")
    @PrimaryKey
    public int mId;

    @ColumnInfo(name = "VOTE_COUNT")
    public int mVoteCount;

    @ColumnInfo(name = "VOTE_AVERAGE")
    public double mVoteAverage;

    @ColumnInfo(name = "POPULARITY")
    public double mPopularity;

    @ColumnInfo(name = "POSTER_PATH")
    public String mPosterPath;

    @ColumnInfo(name = "ORIGINAL_TITLE")
    public String mOriginalTitle;

    @ColumnInfo(name = "OVERVIEW")
    public String mOverview;

    @ColumnInfo(name = "RELEASE_DATE")
    public String mReleaseDate;

    @ColumnInfo(name = "BACKDROP_PATH")
    public String mBackdropPath;

    public Movie(int mId, int mVoteCount, double mVoteAverage, double mPopularity, String mPosterPath, String mOriginalTitle, String mOverview, String mReleaseDate, String mBackdropPath) {
        this.mId = mId;
        this.mVoteCount = mVoteCount;
        this.mVoteAverage = mVoteAverage;
        this.mPopularity = mPopularity;
        this.mPosterPath = mPosterPath;
        this.mOriginalTitle = mOriginalTitle;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
        this.mBackdropPath = mBackdropPath;
    }

    private Movie(Parcel in)
    {
        mId = in.readInt();
        mVoteCount = in.readInt();
        mVoteAverage = in.readDouble();
        mPopularity = in.readDouble();
        mPosterPath = in.readString();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        mReleaseDate = in.readString();
        mBackdropPath = in.readString();
    }


    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public int getmId() {
        return mId;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public int getmVoteCount() {
        return mVoteCount;
    }

    public double getmVoteAverage() {
        return mVoteAverage;
    }

    public double getmPopularity() {
        return mPopularity;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    @Override
    public String toString()
    {
        return "ID: " +
                this.mId +
                "\t" +
                "title: " +
                this.mOriginalTitle +
                "\t" +
                "poster: " +
                this.mPosterPath +
                "\t" +
                "vote count: " +
                this.mVoteCount +
                "\t" +
                "average: " +
                this.mVoteAverage +
                "\t" +
                "popularity: " +
                this.mPopularity +
                "\t" +
                "overview: " +
                this.mOverview +
                "\t" +
                "release date: " +
                this.mReleaseDate +
                "\t";
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeInt(mVoteCount);
        parcel.writeDouble(mVoteAverage);
        parcel.writeDouble(mPopularity);
        parcel.writeString(mPosterPath);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mBackdropPath);
    }
}
