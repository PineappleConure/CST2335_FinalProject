package algonquin.cst2335.cst2335_finalproject;


import android.content.Context;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextLatitude = findViewById(R.id.editTextLatitude);
        editTextLongitude = findViewById(R.id.editTextLongitude);
        lookupButton = findViewById(R.id.lookupButton);
        saveButton = findViewById(R.id.saveButton);
        sunriseTextView = findViewById(R.id.sunrise);
        sunsetTextView = findViewById(R.id.sunset);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        recyclerView = findViewById(R.id.recyclerView);

        locationDAO = new LocationDAO(this);
        List<LocationData> savedLocations = locationDAO.getSavedLocations();

        locationAdapter = new LocationAdapter(savedLocations, new LocationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object item) {
                if (item instanceof LocationData) {
                    LocationData locationData = (LocationData) item;
                    onLocationItemClick(locationData);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        });

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
        });


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                this, recyclerView,
                new LocationAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(Object item) {
                        if(item instanceof LocationData){
                            LocationData locationData = (LocationData) item;
                            showLocationDetailFragment(locationData);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position){
                    }
                }
        ));
    }
    private void onLocationItemClick(LocationData location) {
        Toast.makeText(this, "Clicked on location: " + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
/*
        // display a confirmation dialog to the user
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to delete this location?" + location.getLatitude() + "," + location.getLongitude())
                .setTitle("Delete Location")
                .setNegativeButton("No", (dialog, cl) -> {
                })
                .setPositiveButton("Yes", (dialog, cl) -> {
                    int position = locationAdapter.getLocations().indexOf(location);

                    // get the removed location
                    LocationData removedLocation = locationAdapter.getLocationAt(position);

                    //update the list in LocationAdapter
                    locationDAO.deleteLocation(location.getId());
                    List<LocationData> updatedLocations = locationDAO.getSavedLocations();
                    locationAdapter.updateLocations(updatedLocations);

                    final List<LocationData> finalUpdatedLocations = updatedLocations;
                    Snackbar.make(recyclerView, "Deleted Location #" + (position + 1), Snackbar.LENGTH_LONG)
                            .setAction("Undo", undoclk -> {
                                locationDAO.saveLocation(removedLocation);
//                                updatedLocations = locationDAO.getSavedLocations();
                                locationAdapter.updateLocations(updatedLocations);
                            })
                            .addCallback(new Snackbar.Callback() {
                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    // update the adapter after the Snackbar is dismissed
                                    if (event != DISMISS_EVENT_ACTION) {
                                        locationAdapter.updateLocations(finalUpdatedLocations);
                                    }
                                }
                            })
                            .show();
                })
                .create().show();

 */
    }

        private void showLocationDetailFragment(LocationData locationData){
        LocationDetailFragment fragment = LocationDetailFragment.newInstance(locationData);
        fragment.show(getSupportFragmentManager(), "locationDetailFragment");
        }

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







