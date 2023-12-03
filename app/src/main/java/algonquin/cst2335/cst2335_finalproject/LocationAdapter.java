package algonquin.cst2335.cst2335_finalproject;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<MyRowHolder> {
    @NonNull
    @Override
    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new MyRowHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRowHolder holder, int position){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        FavoriteLocation favoriteLocation = locations.get(position);
        holder.sunrise.setText("Sunrise: " + favoriteLocation.getSunrise());
        holder.sunset.setText("Sunset: " + favoriteLocation.getSunset());
        holder.itemView.setOnClickListener( click ->{
            deleteOrUpdate(position);
        });
    }


    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        FavoriteLocation location = locations.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewLocation;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewLocation = itemView.findViewById(R.id.textViewLocation);
            itemView.setOnClickListener(this);
        }

        public void bind(FavoriteLocation location) {
            textViewLocation.setText("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                clickListener.onClick(locations.get(position));
            }
        }
    }

    public interface LocationClickListener {
        void onClick(FavoriteLocation location);
    }
}

