package com.androidadvanced.petfinder.activities.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.utils.Keys;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;

public class NewPostDetailsFragment extends Fragment {
    private static Gson gson = new Gson();
    private Post post;
    private FragmentListener mListener;

    @BindView(R.id.new_post_pet_name)
    EditText petName;
    @BindView(R.id.new_post_pet_description)
    EditText petDescription;

    public NewPostDetailsFragment() {
    }

    public interface FragmentListener {
        void onDetailsInteraction(Post post);

        void onDetailsUnlock();
    }

    public static NewPostDetailsFragment newInstance(Post post) {
        NewPostDetailsFragment fragment = new NewPostDetailsFragment();
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post_details, container, false);
        ButterKnife.bind(this, view);
        petName.setText(post.getPet().getName());
        petDescription.setText(post.getDescription());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        checkUnlock();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnTextChanged(R.id.new_post_pet_name)
    void onNameChanged(CharSequence text) {
        post.getPet().setName(text.toString());
        mListener.onDetailsInteraction(post);
        checkUnlock();
    }

    @OnTextChanged(R.id.new_post_pet_description)
    void onDescriptionChanged(CharSequence text) {
        post.setDescription(text.toString());
        mListener.onDetailsInteraction(post);
        checkUnlock();
    }

    private void checkUnlock() {
        if (!StringUtils.isBlank(post.getDescription()) && !StringUtils.isBlank(post.getPet()
                .getName())) {
            mListener.onDetailsUnlock();
        }
    }
}
