package in.kirankumard.popularmovies_udacity.Model;

public class Movie {
    int mId, mVoteCount;
    double mVoteAverage, mPopularity;
    String mPosterPath, mOriginalTitle, mOverview, mReleaseDate;

    public Movie(int mId, int mVoteCount, double mVoteAverage, double mPopularity, String mPosterPath, String mOriginalTitle, String mOverview, String mReleaseDate) {
        this.mId = mId;
        this.mVoteCount = mVoteCount;
        this.mVoteAverage = mVoteAverage;
        this.mPopularity = mPopularity;
        this.mPosterPath = mPosterPath;
        this.mOriginalTitle = mOriginalTitle;
        this.mOverview = mOverview;
        this.mReleaseDate = mReleaseDate;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
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
}
