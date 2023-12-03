package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {FavoriteLocation.class}, version=2)

public abstract class LocationDatabase extends RoomDatabase {
    public abstract FavoriteLocationDAO favoriteLocationDAO();

}

