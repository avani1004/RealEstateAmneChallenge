package com.example.android.amne_challenge.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.example.android.amne_challenge.result.AgencyResult;
import com.example.android.amne_challenge.response.LocationResponse;
import com.example.android.amne_challenge.R;
import com.example.android.amne_challenge.api.LocationApi;
import com.example.android.amne_challenge.model.LocationModel;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


/**
 * Created by avaniarora on 10/21/17.
 */


public class AddressActivity extends Activity {

    int PLACE_AUTOCOMPLETE_REQUEST_CODE_ADDRESS_1 = 1;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE_ADDRESS_2 = 2;

    private EditText mAddress1;
    private EditText mAddress2;
    private Button mSearchButton;
    private Retrofit mRetrofit;
    private LatLng mAddress1LatLng;
    private LatLng mAddress2LatLng;
    private static LocationApi mLocationApi;
    private String mBaseUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
    private ListView mListView;

    private List<AgencyResult> mAgencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        mAddress1 = (EditText) findViewById(R.id.address_1);
        mAddress2 = (EditText) findViewById(R.id.address_2);
        mSearchButton = (Button) findViewById(R.id.search);

        mAgencyList = new ArrayList<AgencyResult>();
        mListView = (ListView) findViewById(R.id.agency_list);

        mAddress1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setCountry("US")
                            .build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .setFilter(typeFilter)
                                    .build(AddressActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_ADDRESS_1);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mAddress2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setCountry("US")
                            .build();

                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setFilter(typeFilter)
                                    .build(AddressActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE_ADDRESS_2);

                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new LocationSearchPostTask().execute("json?location=" + mAddress1LatLng.latitude + ", " + mAddress1LatLng.longitude + "&radius=16093.4&type=real_estate_agency&key=AIzaSyBH6PRnIHOpHbqbolQ3RuJPGybTjLRN1Vk");
                new LocationSearchPostTask().execute("json?location=" + mAddress2LatLng.latitude + ", " + mAddress2LatLng.longitude + "&radius=16093.4&type=real_estate_agency&key=AIzaSyBH6PRnIHOpHbqbolQ3RuJPGybTjLRN1Vk");
                sortAgencies();
            }
        });

        mRetrofit = new Retrofit.Builder()
                .baseUrl(mBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mLocationApi = mRetrofit.create(LocationApi.class);


    }

    private class LocationSearchPostTask extends AsyncTask<String, Integer, List<LocationModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<LocationModel> doInBackground(String... params) {

            LocationResponse response = null;
            try {
                response = runLocation(params[0]);

                return response.locationList;

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<LocationModel> locationList) {
            super.onPostExecute(locationList);

            for (int i = 0; i < locationList.size(); i++) {

                LatLng obj = new LatLng(Double.parseDouble(locationList.get(i).geometry.location.lat), Double.parseDouble(locationList.get(i).geometry.location.lng));

                double distance = computeDistance(obj, mAddress1LatLng) + computeDistance(obj, mAddress2LatLng);
                mAgencyList.add(new AgencyResult(locationList.get(i).name, distance));


            }


        }
    }

    public static LocationResponse runLocation(String latlon) throws IOException {
        Call<LocationResponse> call = mLocationApi.getLocation(latlon);
        LocationResponse response = call.execute().body();
        return response;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_ADDRESS_1) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);

                mAddress1LatLng = place.getLatLng();
                mAddress1.setText(place.getName());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                Toast.makeText(getApplicationContext(), PlaceAutocomplete.getStatus(this, data).toString(), Toast.LENGTH_LONG).show();

            }
        }
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE_ADDRESS_2) {
            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);

                mAddress2LatLng = place.getLatLng();
                mAddress2.setText(place.getName());

                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), PlaceAutocomplete.getStatus(this, data).toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public Double computeDistance(LatLng latLngA, LatLng latLngB) {
        Location locationA = new Location("Address 1");
        locationA.setLatitude(latLngA.latitude);
        locationA.setLongitude(latLngA.longitude);

        Location locationB = new Location("Address 2");
        locationB.setLatitude(latLngB.latitude);
        locationB.setLongitude(latLngB.longitude);

        double distance = locationA.distanceTo(locationB);
        //Log.d("distance", distance + "");

        return distance;

    }

    public void sortAgencies() {

        double temp;

        for (int i = 0; i < mAgencyList.size(); i++) {
            for (int j = 1; j < mAgencyList.size() - 1; j++) {
                if(mAgencyList.get(j-1).distance>mAgencyList.get(j).distance){
                temp = mAgencyList.get(j - 1).distance;
                mAgencyList.get(j - 1).setDistance(mAgencyList.get(j).distance);
                mAgencyList.get(j).setDistance(temp);

            }}
        }

        Set<String> names = new LinkedHashSet<String>();
        for (int i = 0; i < mAgencyList.size(); i++) {
            names.add(mAgencyList.get(i).agency);
            //Log.d("name and distance", mAgencyList.get(i).agency + " " + mAgencyList.get(i).distance);
        }
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.addAll(names);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        mListView.setAdapter(adapter);
    }

}

