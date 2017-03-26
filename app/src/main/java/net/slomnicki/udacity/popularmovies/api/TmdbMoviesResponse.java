package net.slomnicki.udacity.popularmovies.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TmdbMoviesResponse {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<TmdbMovie> results;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<TmdbMovie> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }
}