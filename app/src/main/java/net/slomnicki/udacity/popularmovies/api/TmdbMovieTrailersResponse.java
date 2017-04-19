package net.slomnicki.udacity.popularmovies.api;

import java.util.List;

public class TmdbMovieTrailersResponse {
    private int id;
    private List<TmdbMovieTrailer> results;

    public int getId() {
        return id;
    }

    public List<TmdbMovieTrailer> getResults() {
        return results;
    }
}