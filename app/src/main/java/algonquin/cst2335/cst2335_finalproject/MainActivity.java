package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText editTextLatitude;
    private EditText editTextLongitude;
    private Button lookupButton;
    private Button saveButton;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private TextView sunrise;
    private TextView sunset;
    private RecyclerView recyclerView;
    private LocationAdapter locationAdapter;
    private LocationDAO locationDAO;

    RequestQueue queue = null;

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

        locationAdapter = new LocationAdapter(savedLocations, this::onLocationItemClick);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(locationAdapter);

        lookupButton.setOnClickListener(click -> {

            String latitude = editTextLatitude.getText().toString();
            String longitude = editTextLongitude.getText().toString();

            String url = "https://api.sunrisesunset.io/json?lat="
                    + latitude + "&lng="
                    + longitude + "&timezone=CA&date=today";

            queue = Volley.newRequestQueue(this);

            // 发送网络请求
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



    }
    private void onLocationItemClick(LocationData location){
        Toast.makeText(this, "Clicked on location: " + location.getLatitude() + "," + location.getLongitude(), Toast.LENGTH_SHORT).show();
    }
}




