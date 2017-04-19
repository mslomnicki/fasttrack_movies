package net.slomnicki.udacity.popularmovies.api;

import java.util.List;

public class TmdbMovieReviewsResponse {

    private int id;
    private int page;
    private int totalPages;
    private List<TmdbMovieReview> results;
    private int totalResults;

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<TmdbMovieReview> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }
}