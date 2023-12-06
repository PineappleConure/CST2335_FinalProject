package algonquin.cst2335.cst2335_finalproject;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    Bitmap image;

    protected RequestQueue queue = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        queue = Volley.newRequestQueue(this);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.searchButton.setOnClickListener( click -> {
            String recipeTitle = URLEncoder.encode(binding.recipeTitleText.getText().toString());

            String url = "https://api.spoonacular.com/recipes/complexSearch?query="+ recipeTitle +"&apiKey=fedc0c01b27f454aaf939cdab2d0d287";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                try{
                    JSONArray obj = response.getJSONArray("results");
                  int size = obj.length();
                  for(int i = 0; i < size; i++){
                      JSONObject recipe = obj.getJSONObject(i);
                      int id = recipe.getInt("id");
                      String title = recipe.getString("title");
                      String imageName = recipe.getString("image");

                      String recipeURL = "https://api.spoonacular.com/recipes/"+ id +"/information?apiKey=fedc0c01b27f454aaf939cdab2d0d287";

                      JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.GET, recipeURL, null,
                              response2 ->{
                                  try {
                                      String sourceURL = response2.getString("sourceUrl");
                                      String summary = response2.getString("summary");

                                      runOnUiThread(()->{
                                          binding.sourceUrl.setText("The recipe's sourceUrl is: " + sourceURL);
                                          binding.sourceUrl.setVisibility(View.VISIBLE);

                                          binding.summary.setText("The recipe's summary is: " + summary);
                                          binding.summary.setVisibility(View.VISIBLE);
                                      });
                                  } catch (JSONException e) {
                                      throw new RuntimeException(e);
                                  }
                                  int j = 0;
                              },
                              error2 ->{ });
                      queue.add(request2);

                      runOnUiThread(()->{
                          binding.id.setText("The Recipe ID: " + id);
                          binding.id.setVisibility(View.VISIBLE);

                          binding.title.setText("The recipe title is: " + title);
                          binding.title.setVisibility(View.VISIBLE);

                          binding.image.setImageBitmap(image);
                          binding.image.setVisibility(View.VISIBLE);

                      });

                      String pathname = getFilesDir() + "/" + imageName + ".jpg";
                      File file = new File(pathname);
                      if(file.exists()){
                          image = BitmapFactory.decodeFile(pathname);
                      }else {
                          ImageRequest imgReq = new ImageRequest("https://spoonacular.com/recipeImages/" +
                                  imageName + ".jpg", new Response.Listener<Bitmap>() {
                              @Override
                              public void onResponse(Bitmap bitmap) {
                                  try {
                                      image = bitmap;
                                      image.compress(Bitmap.CompressFormat.JPEG, 100,
                                              MainActivity.this.openFileOutput(imageName + ".jpg", Activity.MODE_PRIVATE));
                                      binding.image.setImageBitmap(image);
                                  } catch (Exception e) {
                                      e.printStackTrace();
                                  }
                              }
                          }, 1024,1024, ImageView.ScaleType.CENTER, null, (error)->{
                              Toast.makeText(MainActivity.this, " "+ error, Toast.LENGTH_SHORT).show();
                          });
                          queue.add(imgReq);
                      }

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



