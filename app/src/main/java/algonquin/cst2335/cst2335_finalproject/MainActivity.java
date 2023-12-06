package algonquin.cst2335.cst2335_finalproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {



    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        queue = Volley.newRequestQueue(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.searchButton.setOnClickListener( click -> {
            String recipeTitle = URLEncoder.encode(binding.recipeTitleText.getText().toString());

            String url = "https://api.spoonacular.com/recipes/complexSearch?query="+ recipeTitle +"&apiKey=9af43493ab344fc8bbaee3b9aef17483";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                try{
                    JSONArray obj = response.getJSONArray("results");
                  int size = obj.length();
                  for(int i = 0; i < size; i++){
                      JSONObject recipe = obj.getJSONObject(i);
                      int id = recipe.getInt("id");
                      String title = recipe.getString("title");
                      String image = recipe.getString("image");

                      String recipeURL = "https://api.spoonacular.com/recipes/"+ id +"/information?apiKey=9af43493ab344fc8bbaee3b9aef17483";

                      JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, recipeURL, null,
                              response2 ->{
                                  try {
                                      String sourceURL = response2.getString("sourceUrl");
                                      String summary = response2.getString("summary");
                                  } catch (JSONException e) {
                                      throw new RuntimeException(e);
                                  }
                                  int j = 0;
                              },
                              error2 ->{ });
                      queue.add(request2);

                  }
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



