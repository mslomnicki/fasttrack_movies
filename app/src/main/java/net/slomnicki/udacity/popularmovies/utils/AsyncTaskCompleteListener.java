package net.slomnicki.udacity.popularmovies.utils;

public interface AsyncTaskCompleteListener<T> {
    public void onTaskComplete(T result);
}
