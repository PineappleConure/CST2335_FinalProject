package algonquin.cst2335.cst2335_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private List<LocationData> locations;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onItemClick(Object item);

        void onLongItemClick(View view, int position);
//        void onItemItemClick(View view, int position);
    }

    public LocationAdapter(List<LocationData> locations, OnItemClickListener listener){
        this.locations = locations;
        this.listener = listener;
    }

    public void updateLocations(List<LocationData> updatedLocations){
        locations.clear();
        locations.addAll(updatedLocations);
        notifyDataSetChanged();
    }


    public List<LocationData> getLocations(){
        return locations;
    }

    public LocationData getLocationAt(int position){
        return locations.get(position);
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(itemView, listener );
    }
    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position){
        LocationData location = locations.get(position);
        holder.bind(location);
    }
    @Override
    public int getItemCount(){
        return locations.size();
    }


    class LocationViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewLatitude;
        private TextView textViewLongitude;
        private OnItemClickListener listener;
//        private LocationAdapter locationAdapter;

//        private List<LocationData> locations;
        public LocationViewHolder(@NonNull View itemView, OnItemClickListener listener){
            super(itemView);
            this.listener = listener;
//            this.locationAdapter = locationAdapter;

            textViewLatitude = itemView.findViewById(R.id.textViewLatitude);
            textViewLongitude = itemView.findViewById(R.id.textViewLongitude);

            itemView.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();

                if(position != RecyclerView.NO_POSITION && this.listener != null){
                    this.listener.onItemClick(getLocationAt(position));
                }
            });
        }
        public void bind(LocationData location){
            textViewLatitude.setText("Latitude: " + location.getLatitude());
            textViewLongitude.setText("Longitude: " + location.getLongitude());
        }
    }
}
