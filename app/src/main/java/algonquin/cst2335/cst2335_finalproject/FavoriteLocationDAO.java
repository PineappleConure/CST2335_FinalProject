package algonquin.cst2335.cst2335_finalproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoriteLocationDAO {
    @Insert
    void insertLocation(FavoriteLocation favoriteLocation);
    @Query("SELECT * FROM FavoriteLocation")
    List<FavoriteLocation> getAllFavoriteLocations();
    @Delete
    void deleteLocation(FavoriteLocation favoriteLocation);

}

