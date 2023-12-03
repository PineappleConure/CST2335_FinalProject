package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.databinding.DetailsLayoutBinding;

public class LocationDetailsFragment extends Fragment {
    FavoriteLocation selected;

    public LocationDetailsFragment(FavoriteLocation favoriteLocation){selected = favoriteLocation;}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreateView (inflater, container, savedInstanceState);

        DetailsLayoutBinding binding =  DetailsLayoutBinding.inflate(inflater);

        binding.textViewLatitude.setText(selected.latitude);
        binding.textViewLongitude.setText(selected.longitude);
        binding.databaseText.setText("id = " + selected.id);

        return binding.getRoot();
    }
}
