package algonquin.cst2335.cst2335_finalproject_sunset_sunrise;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.volley.RequestQueue;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import algonquin.cst2335.cst2335_finalproject.R;

/**
 * A DialogFragment for displaying details of a specific location, including latitude and longitude.
 */
public class LocationDetailFragment extends DialogFragment {
    private LocationData locationData;
    private RequestQueue queue;
    private TextView sunriseTextView;
    private TextView sunsetTextView;
    private MainActivity activity;

    /**
     * Called when the fragment is attached to an activity.
     * Checks if the parent activity is an instance of MainActivity and sets the activity accordingly.
     * @param context   The context to which the fragment is attached.
     */
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof MainActivity){
            activity = (MainActivity) context;
        }else{
            throw new ClassCastException("Activity must implement MainActivity interface");
        }
    }

    /**
     * Creates a new instance of LocationDetailFragment with the provided LocationData.
     * @param locationData  The LocationData to display details for.
     * @return  A new instance of LocationDetailFragment.
     */
    public static LocationDetailFragment newInstance(LocationData locationData){
        LocationDetailFragment fragment = new LocationDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("locationData", locationData);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Inflates the fragment's layout, initializes UI components, and sets up event listeners.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The inflated View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_location_detail,container, false);
        locationData = getArguments().getParcelable("locationData");

        TextView textViewLatitude = rootView.findViewById(R.id.textViewLatitude);
        TextView textViewLongitude = rootView.findViewById(R.id.textViewLongitude);
        Button deleteButton = rootView.findViewById(R.id.deleteButton);
//        Button searchButton = rootView.findViewById(R.id.searchButton);
//        sunriseTextView = rootView.findViewById(R.id.sunriseTextView);
//        sunsetTextView = rootView.findViewById(R.id.sunsetTextView);

        textViewLatitude.setText("Latitude: " + locationData.getLatitude());
        textViewLongitude.setText("Longitude: " + locationData.getLongitude());

        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog());
//        searchButton.setOnClickListener(v -> performSearchAction());

        return rootView;
    }

    /**
     * Display a confirmation dialog for deleting the current location.
     */
    private void showDeleteConfirmationDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Do you want to delete this location?" + locationData.getLatitude() + "," + locationData.getLongitude())
                .setTitle("Delete Location")
                .setNegativeButton("No", (dialog,cl)->{})
                .setPositiveButton("Yes", (dialog,cl)->{
                    int position = activity.locationAdapter.getLocations().indexOf(locationData);

                    // get the removed location
                    LocationData removedLocation = activity.locationAdapter.getLocationAt(position);

                    // remove the location from the list and update the list in LocationAdapter
                    activity.locationDAO.deleteLocation(locationData.getId());
                    List<LocationData> updatedLocations = activity.locationDAO.getSavedLocations();
                    activity.locationAdapter.updateLocations(updatedLocations);

//                    final List<LocationData> finalUpdatedLocations = updatedLocations;
                    Snackbar.make(activity.recyclerView, "Deleted Location #" + (position+1), Snackbar.LENGTH_LONG)
                            .setAction("Undo", undoclk -> {
                                activity.locationDAO.saveLocation(removedLocation);
//                              updatedLocations = activity.locationDAO.getSavedLocations();
                                activity.locationAdapter.updateLocations(updatedLocations);
                            })
                            .addCallback(new Snackbar.Callback(){
                                @Override
                                public void onDismissed(Snackbar snackbar, int event){
                                    // update the adapter after the Snackbar is dismissed
                                    if (event != DISMISS_EVENT_ACTION) {
                                        activity.locationAdapter.updateLocations(updatedLocations);
                                    }
                                }
                            })
                            .show();
                })
                .create().show();

    }

//    private void performSearchAction(){
//        String latitude = activity.editTextLatitude.getText().toString();
//        String longitude = activity.editTextLongitude.getText().toString();
//
//        String url = "https://api.sunrisesunset.io/json?lat="
//                + latitude + "&lng="
//                + longitude + "&timezone=ca&date=today";
//
//        queue = Volley.newRequestQueue(activity);
//
//        // send network request
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                response ->{
//                    try{
//                        JSONObject obj = response.getJSONObject("results");
//                        String sunrise = obj.getString("sunrise");
//                        String sunset = obj.getString("sunset");
//
//                        // find the sunrise and sunset TextView within the Fragment
//
//                        TextView sunriseTextView = activity.findViewById(R.id.sunrise);
//                        TextView sunsetTextView = activity.findViewById(R.id.sunset);
//
//                        activity.runOnUiThread(()->{
//                            sunriseTextView.setText("The current sunrise time is " + sunrise);
//                            sunriseTextView.setVisibility(View.VISIBLE);
//
//                            sunsetTextView.setText("The current sunset time is " + sunset);
//                            sunsetTextView.setVisibility(View.VISIBLE);
//                        });
//
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                },
//                error -> {
//                    int i = 0;
//                });
//        queue.add(request);
//    }
}

