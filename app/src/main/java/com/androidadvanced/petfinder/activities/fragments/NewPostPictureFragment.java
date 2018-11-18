package com.androidadvanced.petfinder.activities.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.models.Pet;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.utils.Keys;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.myhexaville.smartimagepicker.ImagePicker;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPostPictureFragment extends Fragment {
    private static Gson gson = new Gson();
    private Post post;
    private FragmentListener mListener;
    private ImagePicker imagePicker;

    @BindView(R.id.new_post_image)
    ImageView postImage;

    public NewPostPictureFragment() {
    }

    public interface FragmentListener {
        void onPictureInteraction(Post post);
    }

    public static NewPostPictureFragment newInstance(Post post) {
        NewPostPictureFragment fragment = new NewPostPictureFragment();
        Bundle args = new Bundle();
        args.putString(Keys.FRAGMENT_POST_KEY, gson.toJson(post));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof FragmentListener)) {
            throw new RuntimeException(context.getClass() + " must implement FragmentListener");
        }
        mListener = (FragmentListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            post = gson.fromJson(getArguments().getString(Keys.FRAGMENT_POST_KEY), Post.class);
        }

        imagePicker = new ImagePicker(getActivity(), this, this::setPicture);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post_picture, container, false);
        ButterKnife.bind(this, view);
        if (post.getPet() != null && post.getPet().getPhotoUrl() != null)
            Glide.with(this).load(Uri.parse(post.getPet().getPhotoUrl())).into(postImage);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (post.getPet() != null && post.getPet().getPhotoUrl() != null) {
            mListener.onPictureInteraction(post);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.handleActivityResult(resultCode, requestCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.handlePermission(requestCode, grantResults);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick(R.id.open_camera)
    void openCamera() {
        imagePicker.openCamera();
    }

    @OnClick(R.id.open_gallery)
    void openGallery() {
        imagePicker.choosePicture(false);
    }

    private void setPicture(Uri uri) {
        if (post.getPet() == null) {
            post.setPet(new Pet());
        }

        post.getPet().setPhotoUrl(uri.toString());
        Glide.with(this).load(uri).into(postImage);
        mListener.onPictureInteraction(post);
    }
}
