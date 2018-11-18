package com.androidadvanced.petfinder.activities.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidadvanced.petfinder.R;
import com.androidadvanced.petfinder.models.Post;
import com.androidadvanced.petfinder.utils.Keys;
import com.androidadvanced.petfinder.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewPostLocationFragment extends Fragment implements OnMapReadyCallback {
    private static final int LOCATION_CODE_REQUEST = 12;
    private static final float DEFAULT_ZOOM_LEVEL = 8;
    private static Gson gson = new Gson();

    private Post post;
    private FragmentListener mListener;
    private FusedLocationProviderClient locationClient;
    @BindView(R.id.new_post_address)
    TextView petAddress;
    @BindView(R.id.new_post_map)
    MapView mapView;
    GoogleMap map;

    public NewPostLocationFragment() {
    }

    public interface FragmentListener {
        void onLocationInteraction(Post post);
    }

    public static NewPostLocationFragment newInstance(Post post) {
        NewPostLocationFragment fragment = new NewPostLocationFragment();
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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_post_location, container, false);
        ButterKnife.bind(this, view);
        if (!StringUtils.isBlank(post.getPet().getLastSeenAddress())) {
            petAddress.setText(post.getPet().getLastSeenAddress());
        }

        mapView.onCreate(savedInstanceState);
        mapView.setOnTouchListener((v, e) -> {
            if (mapView.hasFocus()) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (e.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_SCROLL:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
            }
            return false;
        });
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!StringUtils.isBlank(post.getPet().getLastSeenAddress())) {
            mListener.onLocationInteraction(post);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_CODE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager
                    .PERMISSION_GRANTED) {
                requestLocation();
            } else {
                Utils.alert(getActivity(), "C'mon, you should give us some access!");
            }
        }
    }

    @OnClick(R.id.new_post_current_location_btn)
    void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission
                .ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_CODE_REQUEST);
            return;
        }
        if (locationClient == null) {
            locationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        }
        locationClient.getLastLocation()
                .addOnSuccessListener(this::setAddress)
                .addOnFailureListener(e -> Utils.alert(getActivity(), e.getMessage()));
    }

    private void setAddress(Location location) {
        if (location == null) {
            locationNotAvailable();
            return;
        }
        setAddress(new LatLng(location.getLatitude(), location.getLongitude()), false);
    }

    private void setAddress(LatLng loc, boolean keepZoom) {
        // Setting address in text field and post entity
        String lastAddress = getReadableAddress(new LatLng(loc.latitude, loc.longitude));
        post.getPet().setLastSeenAddress(lastAddress);
        petAddress.setText(lastAddress);
        // Saving lat/long in entity
        String latLong = loc.latitude + "," + loc.longitude;
        post.getPet().setLatLong(latLong);

        // Sending entity to parent activity
        mListener.onLocationInteraction(post);

        // Showing marker in map view
        if (map != null) setCoordenates(keepZoom);
    }

    public void setCoordenates(boolean keepZoom) {
        String[] coords = post.getPet().getLatLong().split(Pattern.quote(","));
        double lat = Double.parseDouble(coords[0]);
        double lng = Double.parseDouble(coords[1]);
        LatLng latLong = new LatLng(lat, lng);
        map.clear();
        map.addMarker(new MarkerOptions().position(latLong).title(post.getPet().getLastSeenAddress()));

        float zoom = keepZoom ? map.getCameraPosition().zoom : DEFAULT_ZOOM_LEVEL;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, zoom));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapLongClickListener(loc -> setAddress(loc, true));
        if (StringUtils.isBlank(post.getPet().getLatLong())) {
            return;
        }
        setCoordenates(false);
    }

    private String getReadableAddress(LatLng loc) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        String address = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(loc.latitude, loc.longitude,
                    1);
            if (!addressList.isEmpty()) {
                address = addressList.get(0).getAddressLine(0);
            } else {
                locationNotAvailable();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    private void locationNotAvailable() {
        Utils.alert(getActivity(), "Couldn't retrieve your location. Please, set it on the map");
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
