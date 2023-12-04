package algonquin.cst2335.cst2335_finalproject_sunset_sunrise;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;

/**
 * Adapter class for managing the data set of LocationData objects and binding them to RecyclerView items.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {
    private List<LocationData> locations;
    private OnItemClickListener listener;

    /**
     *  Interface definition for a callback to be invoked when an item in the adapter is clicked or long-clicked.
     */
    public interface OnItemClickListener{
        /**
         * Called when an item is clicked.
         * @param item The clicked item.
         */
        void onItemClick(Object item);

        /**
         * Called when an item is long-clicked.
         * @param view  The View that was clicked.
         * @param position  The position of the clicked item.
         */
        void onLongItemClick(View view, int position);
    }

    /**
     * Constructs a LocationAdapter with the provided list of locations and item click listener.
     * @param locations The list of LocationData objects to be displayed.
     * @param listener  The listener for item click events.
     */
    public LocationAdapter(List<LocationData> locations, OnItemClickListener listener){
        this.locations = locations;
        this.listener = listener;
    }

    /**
     * Updates the list of locations in the adapter and notifies observers of the data set change.
     * @param updatedLocations The updated list of LocationData objects.
     */
    public void updateLocations(List<LocationData> updatedLocations){
        locations.clear();
        locations.addAll(updatedLocations);
        notifyDataSetChanged();
    }

    /**
     * Gets the list of locations in the adapter.
     * @return The list of LocationData objects.
     */
    public List<LocationData> getLocations(){
        return locations;
    }

    /**
     * Gets the LocationData object at the specified position in the adapter.
     * @param position  The position of the desired item.
     * @return  The LocationData object at the specified position.
     */
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

    /**
     * ViewHolder class for representing individual items in the RecyclerView.
     */
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

        /**
         * Binds the data of a LocationData object to the ViewHolder.
         * @param location  The LocationData object to bind.
         */
        public void bind(LocationData location){
            textViewLatitude.setText("Latitude: " + location.getLatitude());
            textViewLongitude.setText("Longitude: " + location.getLongitude());
        }
    }
}
