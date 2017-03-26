package net.slomnicki.udacity.popularmovies.movieposters;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.slomnicki.udacity.popularmovies.R;
import net.slomnicki.udacity.popularmovies.api.MovieDatabaseApiStub;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mPostersRecyclerView;
    private PostersAdapter mPostersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPostersRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);

        int spanCount = 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mPostersRecyclerView.setLayoutManager(layoutManager);
        mPostersRecyclerView.setHasFixedSize(true);
        mPostersAdapter = new PostersAdapter(new MovieDatabaseApiStub());
        mPostersRecyclerView.setAdapter(mPostersAdapter);

    }
}
