package net.slomnicki.udacity.popularmovies.utils;

public interface AsyncTaskCompleteListener<T> {
    void onTaskComplete(T result);
}
