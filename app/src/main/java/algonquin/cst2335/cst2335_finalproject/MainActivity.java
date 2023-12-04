package algonquin.cst2335.cst2335_finalproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

/**
 * The main activity of the application responsible for handling user interactions,
 * displaying sunrise and sunset informatin, managing saved locations, and more.
 */

public class MainActivity extends AppCompatActivity {
    public EditText editTextLatitude;
    public EditText editTextLongitude;
    private Button lookupButton;
    private Button saveButton;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView sunrise;
    private TextView sunset;
    public RecyclerView recyclerView;
    public LocationAdapter locationAdapter;
    public LocationDAO locationDAO;
    RequestQueue queue = null;
    ActivityMainBinding binding;

    /**
     * Called when the activity is first created.
     * Responsible for initializing UI components, setting up event listeners,
     * and loading saved locations from SharedPreferences.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize UI components and set up event listeners
        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        lookupButton = findViewById(R.id.lookupButton);
        saveButton = findViewById(R.id.saveButton);
        sunriseTextView = findViewById(R.id.sunrise);
        sunsetTextView = findViewById(R.id.sunset);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        recyclerView = findViewById(R.id.recyclerView);

        // load saved locations from SharedPreferences
        loadFromSharedPreferencs();

        // initialize LocationDAO and retrieve saved locations
        locationDAO = new LocationDAO(this);
        List<LocationData> savedLocations = locationDAO.getSavedLocations();

        // initialize LocationAdapter with saved locations
        locationAdapter = new LocationAdapter(savedLocations, new LocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                if (item instanceof LocationData) {
                    LocationData locationData = (LocationData) item;
                    onLocationItemClick(locationData);
                }
            }
            @Override
            public void onLongItemClick(View view, int position) {}
        });

        // set up RecyclerView with LinearLayoutManager and LocationAdapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(locationAdapter);

        lookupButton.setOnClickListener(click -> {
            String latitude = editTextLatitude.getText().toString();
            String longitude = editTextLongitude.getText().toString();

            String url = "https://api.sunrisesunset.io/json?lat="
                    + latitude + "&lng="
                    + longitude + "&timezone=CA&date=today";

            queue = Volley.newRequestQueue(this);

            // send network request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response ->{
                try{
                    JSONObject obj = response.getJSONObject("results");
                    String sunrise = obj.getString("sunrise");
                    String sunset = obj.getString("sunset");

                    runOnUiThread(()->{
                        sunriseTextView.setText("The current sunrise time is " + sunrise);
                        sunriseTextView.setVisibility(View.VISIBLE);
                        sunsetTextView.setText("The current sunset time is " + sunset);
                        sunsetTextView.setVisibility(View.VISIBLE);
                    });
                    saveToSharedPreferences(latitude, longitude);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                },
                    error -> {
                int i = 0;
            });
            queue.add(request);
        });

        saveButton.setOnClickListener(click ->{
            String latitude = editTextLatitude.getText().toString();
            String longitude = editTextLongitude.getText().toString();

            long id = locationDAO.saveLocation(new LocationData(-1, latitude,longitude));

            LocationData locationData = new LocationData((int)id, latitude, longitude);

            List<LocationData> updatedLocations = locationDAO.getSavedLocations();

            locationAdapter.updateLocations(updatedLocations);

            saveToSharedPreferences(latitude, longitude);
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView, new LocationAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Object item) {
                        if(item instanceof LocationData){
                            LocationData locationData = (LocationData) item;
                            showLocationDetailFragment(locationData);
                        }
                    }
                    @Override
                    public void onLongItemClick(View view, int position){}
                }
                )
        );
    }

    /**
     * Load saved latitude and longitude from SharedPreferences and set them to the corresponding EditTexts.
     */
    private void loadFromSharedPreferencs() {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        String savedLatitude = preferences.getString("saved_latitude","");
        String savedLongitude = preferences.getString("saved_longitude","");

        editTextLatitude.setText(savedLatitude);
        editTextLongitude.setText(savedLongitude);
    }

    /**
     * Save the given latitude and longitude to SharedPreferences.
     * @param latitude      The latitude to be saved.
     * @param longitude     The longitude to be saved.
     */
    private void saveToSharedPreferences(String latitude, String longitude) {
        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("saved_latitude", latitude);
        editor.putString("saved_longitude", longitude);
        editor.apply();
    }

    /**
     * Handle click events on saved locations in the RecyclerView.
     * @param location  The clicked LocationData.
     */
    private void onLocationItemClick(LocationData location) {
        Toast.makeText(this, "Clicked on location: " + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }

    /**
     * Show the LocationDetailsFragment for the given LocationData.
     * @param locationData  The LocationData for which to show the detail fragment.
     */
    private void showLocationDetailFragment(LocationData locationData){
        LocationDetailFragment fragment = LocationDetailFragment.newInstance(locationData);
        fragment.show(getSupportFragmentManager(), "locationDetailFragment");
    }

    /**
     * Custom RecyclerView.OnItemTouchListener for handling item click and long click events.
     */
    public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener{
        private LocationAdapter.OnItemClickListener listener;
        private GestureDetector gestureDetector;
        public RecyclerItemClickListener(Context context, RecyclerView recyclerView, LocationAdapter.OnItemClickListener listener) {
            this.listener = listener;
            this.gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && listener != null) {
                        listener.onLongItemClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }
        @Override
        public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && gestureDetector.onTouchEvent(e)) {
                int position = rv.getChildAdapterPosition(child);
                listener.onItemClick(locationAdapter.getLocationAt(position));
                return true;
            }
            return false;
        }
        @Override
        public void onTouchEvent(@Nullable RecyclerView rv, @NonNull MotionEvent e) {}
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
    }
}







