package com.androidadvanced.petfinder.utils;

import com.androidadvanced.petfinder.models.Post;

import java.util.ArrayList;
import java.util.List;

public class Dummy {

    public static List<Post> getPosts() {
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
}
