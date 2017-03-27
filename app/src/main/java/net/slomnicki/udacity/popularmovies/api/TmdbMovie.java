package net.slomnicki.udacity.popularmovies.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TmdbMovie implements Parcelable {

    @SerializedName("overview")
    private String overview;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("video")
    private boolean video;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("id")
    private int id;

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("vote_count")
    private int voteCount;

    public String getOverview() {
        return overview;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public boolean isVideo() {
        return video;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getId() {
        return id;
    }

    public boolean isAdult() {
        return adult;
    }

    public int getVoteCount() {
        return voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(video ? 1 : 0);
        parcel.writeInt(adult ? 1 : 0);
        parcel.writeInt(id);
        parcel.writeInt(voteCount);
        parcel.writeDouble(popularity);
        parcel.writeDouble(voteAverage);
        parcel.writeString(overview);
        parcel.writeString(originalLanguage);
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeString(posterPath);
        parcel.writeString(backdropPath);
        parcel.writeString(releaseDate);
    }

    static final Parcelable.Creator<TmdbMovie> CREATOR = new Creator<TmdbMovie>() {
        @Override
        public TmdbMovie createFromParcel(Parcel parcel) {
            TmdbMovie retval = new TmdbMovie();
            retval.video = parcel.readInt() == 1;
            retval.adult = parcel.readInt() == 1;
            retval.id = parcel.readInt();
            retval.voteCount = parcel.readInt();
            retval.popularity = parcel.readDouble();
            retval.voteAverage = parcel.readDouble();
            retval.overview = parcel.readString();
            retval.originalLanguage = parcel.readString();
            retval.originalTitle = parcel.readString();
            retval.title = parcel.readString();
            retval.posterPath = parcel.readString();
            retval.backdropPath = parcel.readString();
            retval.releaseDate = parcel.readString();
            return retval;
        }

        @Override
        public TmdbMovie[] newArray(int size) {
            return new TmdbMovie[size];
        }
    };
}