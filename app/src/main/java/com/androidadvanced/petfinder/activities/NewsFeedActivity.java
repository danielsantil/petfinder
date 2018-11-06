package com.androidadvanced.petfinder.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.adapters.NewsFeedAdapter;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFeedActivity extends AppCompatActivity {

    @BindView(R.id.news_feed_recycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.my_news_feed);
        }

        List<Post> posts = getPosts();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setAdapter(new NewsFeedAdapter(this, posts));
    }

    private List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();

        String base64 = Constants.DUMMY_IMG;
        byte[] bytes = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
        base64 = Constants.DUMMY_IMG_3;
        byte[] bytes3 = android.util.Base64.decode(base64, android.util.Base64.DEFAULT);
        posts.add(new Post("Firulai", "Calle 1 esq Calle 4, D.N.", bytes, 10, 20));
        posts.add(new Post("Doggy", "Calle 1 esq Calle 4, Santiago", bytes3, 14, 153));
        posts.add(new Post("Catty", "Calle 1 esq Calle 4, D.N.", bytes3, 1523, 135));
        posts.add(new Post("Bobb", "Calle 1 esq Calle 4, Panama", bytes, 132, 5234));
        posts.add(new Post("Beto", "Calle 1 esq Calle 4, La Romana", bytes, 0, 0));
        posts.add(new Post("Croco", "Calle 1 esq Calle 4, Puerto Plata", bytes3, 14, 44));
        posts.add(new Post("Turtley", "Calle 1 esq Calle 4, Av. Churchill", bytes3, 5, 5));

        return posts;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
