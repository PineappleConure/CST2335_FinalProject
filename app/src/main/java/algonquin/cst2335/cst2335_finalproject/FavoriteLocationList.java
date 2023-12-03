package algonquin.cst2335.cst2335_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.ActivityFavoriteLocationListBinding;

public class FavoriteLocationList extends AppCompatActivity {
    private ActivityFavoriteLocationListBinding binding;
    private FavoriteLocationDAO favoriteLocationDAO;
    private ArrayList<FavoriteLocation> favoriteLocations = new ArrayList<>();
    private RecyclerView.Adapter<MyRowHolder> myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        binding = ActivityFavoriteLocationListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // initialize Room database and DAO
        LocationDatabase db = Room.databaseBuilder(getApplicationContext(),LocationDatabase.class, "location-database").fallbackToDestructiveMigration().build();
        favoriteLocationDAO = db.favoriteLocationDAO();

        // set up RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new FavoriteLocationAdapter();
        binding.recyclerView.setAdapter(myAdapter);

        String latitude = getIntent().getStringExtra("latitude");
        String longitude = getIntent().getStringExtra("longitude");
        String sunrise = getIntent().getStringExtra("sunrise");
        String sunset = getIntent().getStringExtra("sunset");

        Button backTo = findViewById(R.id.backTo);
        backTo.setOnClickListener(click ->{
            Intent toMain = new Intent(this,MainActivity.class);
            startActivity(toMain);
        });

        FavoriteLocation favoriteLocation = new FavoriteLocation(latitude,longitude,sunrise,sunset);
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(()->{
            favoriteLocationDAO.insertLocation(favoriteLocation);

            List<FavoriteLocation> allLocations = favoriteLocationDAO.getAllFavoriteLocations();
            runOnUiThread(() ->{
                favoriteLocations.clear();
                favoriteLocations.addAll(allLocations);
                myAdapter.notifyDataSetChanged();
            });
        });
    }

    // recyclerView Adapter
    class FavoriteLocationAdapter extends RecyclerView.Adapter<MyRowHolder>{
        @NonNull
        @Override
        public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
            return new MyRowHolder(itemView);
        }
        @Override
        public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
            FavoriteLocation favoriteLocation = favoriteLocations.get(position);
            holder.sunrise.setText("Sunrise: " + favoriteLocation.getSunrise());
            holder.sunset.setText("Sunset: " + favoriteLocation.getSunset());
            holder.itemView.setOnClickListener( click ->{
                deleteLocation(position);
            });
        }
        @Override
        public int getItemCount() {
            return favoriteLocations.size();
        }
    }

    class MyRowHolder extends RecyclerView.ViewHolder{
        public TextView sunrise;
        public TextView sunset;
        public MyRowHolder(@NonNull View itemView){
            super(itemView);
            sunrise = itemView.findViewById(R.id.sunrise);
            sunset = itemView.findViewById(R.id.sunset);
        }
    }
    private void deleteLocation(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete location");
        builder.setMessage("Do you want to delete the location? ");
        builder.setPositiveButton("Delete", (dialog, which) -> {
            delete(position);
        });
        builder.show();
    }

    private void delete(int position){
        FavoriteLocation deletedLocation = favoriteLocations.get(position);
        favoriteLocations.remove(position);
        myAdapter.notifyItemRemoved(position);

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() ->{
            favoriteLocationDAO.delete(deletedLocation);
        });
    }


}
