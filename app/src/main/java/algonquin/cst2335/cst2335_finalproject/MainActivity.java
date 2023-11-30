package algonquin.cst2335.cst2335_finalproject;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.lookupButton.setOnClickListener(click -> {

            String latitude = binding.editTextLatitude.getText().toString();
            String longitude = binding.editTextLongitude.getText().toString();

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
                                    binding.sunrise.setText("The current sunrise time is " + sunrise);
                                    binding.sunrise.setVisibility(View.VISIBLE);

                                    binding.sunset.setText("The current sunset time is " + sunset);
                                    binding.sunset.setVisibility(View.VISIBLE);
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
    }
}



