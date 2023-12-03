package algonquin.cst2335.cst2335_finalproject;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private Button lookupButton;
    private Button saveButton;
    private RecyclerView recyclerView;
    MainActivityViewModel viewModel;
    private RequestQueue requestQueue;
    private LocationAdapter locationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextLatitude = findViewById(R.id.editTextLatitude);
        EditText editTextLongitude = findViewById(R.id.editTextLongitude);
        lookupButton = findViewById(R.id.lookupButton);
        saveButton = findViewById(R.id.saveButton);
        recyclerView = findViewById(R.id.recyclerView);

//        // initialize viewModel
//        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
//
//        // initialize Room database and DAO
//        LocationDatabase locationDatabase = Room.databaseBuilder(
//                getApplicationContext(),
//                LocationDatabase.class,
//                "location-database"
//        ).fallbackToDestructiveMigration().build();
//
//        viewModel.init(locationDatabase);// pass the database to the viewModel

        requestQueue = Volley.newRequestQueue(this);
//        locationAdapter = new LocationAdapter(new ArrayList<>(), new LocationAdapter.LocationClickListener() {
//            @Override
//            public void onClick(FavoriteLocation location) {
//                viewModel.selectedLocation.setValue(location);
//                getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragmentLocation, new LocationDetailsFragment(location))
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(locationAdapter);

        lookupButton.setOnClickListener(new View.OnClickListener() {
                String latitude = editTextLatitude.getText().toString();
                String longitude = editTextLongitude.getText().toString();

        String apiUrl = "https://api.sunrisesunset.io/json?lat="
                + latitude + "&lng=" + longitude + "&timezone=ca&date=today";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
        new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String sunriseTime = response.getJSONObject("results").getString("sunrise");
                            String sunsetTime = response.getJSONObject("results").getString("sunset");

                            viewModel.selectedLocation.setValue(new FavoriteLocation(latitude, longitude, false));
                            viewModel.selectedLocation.getValue().setSunriseTime(sunriseTime);
                            viewModel.selectedLocation.getValue().setSunsetTime(sunsetTime);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        requestQueue.add(request);
    }

}
}



