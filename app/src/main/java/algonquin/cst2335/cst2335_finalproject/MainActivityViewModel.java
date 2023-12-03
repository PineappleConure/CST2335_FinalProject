package algonquin.cst2335.cst2335_finalproject;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class MainActivityViewModel extends ViewModel {

    public MutableLiveData<ArrayList<FavoriteLocation>> locations = new MutableLiveData<>();
//    public MutableLiveData<FavoriteLocation> selectedLocation = new MutableLiveData<>();
//    FavoriteLocationDAO favoriteLocationDAO;
//    public void init(LocationDatabase database){
//        favoriteLocationDAO = database.favoriteLocationDAO();
//    }
//
//    public void insertLocation(FavoriteLocation favoriteLocation){
//        try{
//            favoriteLocationDAO.insertLocation(favoriteLocation);
//        }catch(Exception e){e.printStackTrace();}
//
//    }

}

