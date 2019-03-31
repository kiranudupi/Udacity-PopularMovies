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

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public void setmBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public int getmVoteCount() {
        return mVoteCount;
    }

    public void setmVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public double getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public double getmPopularity() {
        return mPopularity;
    }

    public void setmPopularity(double mPopularity) {
        this.mPopularity = mPopularity;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public void setmOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append("ID: ")
                .append(this.mId)
                .append("\t")
                .append("title: ")
                .append(this.mOriginalTitle)
                .append("\t")
                .append("poster: ")
                .append(this.mPosterPath)
                .append("\t")
                .append("vote count: ")
                .append(this.mVoteCount)
                .append("\t")
                .append("average: ")
                .append(this.mVoteAverage)
                .append("\t")
                .append("popularity: ")
                .append(this.mPopularity)
                .append("\t")
                .append("overview: ")
                .append(this.mOverview)
                .append("\t")
                .append("release date: ")
                .append(this.mReleaseDate)
                .append("\t").toString();
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
