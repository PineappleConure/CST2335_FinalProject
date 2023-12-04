package algonquin.cst2335.cst2335_finalproject_sunset_sunrise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing LocationData objects in an SQLite database.
 */
public class LocationDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    /**
     * Constructs a LocationDAO instance.
     * @param context   The context used to create the DatabaseHelper.
     */
    public LocationDAO(Context context){
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Saves a LocationData object to the database.
     * @param location  The LocationData object to be saved.
     * @return  The id of the inserted row.
     */
    public long saveLocation(LocationData location){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LATITUDE, location.getLatitude());
        values.put(DatabaseHelper.COLUMN_LONGITUDE, location.getLongitude());

//        long insertedId = database.insert(DatabaseHelper.TABLE_LOCATIONS, null, values);
//        return insertedId;
        return database.insert(DatabaseHelper.TABLE_LOCATIONS,null, values);
    }

    /**
     * Retrieves a list of all saved LocationData objects from the database.
     * @return  A list of LocationData objets.
     */
    public List<LocationData> getSavedLocations() {
        List<LocationData> locations = new ArrayList<>();

        int columnIndexId = -1;
        int columnIndexLatitude = -1;
        int columnIndexLongitude = -1;

        try(Cursor cursor = database.query(
                DatabaseHelper.TABLE_LOCATIONS,
            new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_LATITUDE, DatabaseHelper.COLUMN_LONGITUDE},
                    null, null, null, null, null)) {

            if (cursor.moveToFirst()) {
                columnIndexId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
                columnIndexLatitude = cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE);
                columnIndexLongitude = cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE);

                do{
                    int id = cursor.getInt(columnIndexId);
                    String latitude = cursor.getString(columnIndexLatitude);
                    String longitude = cursor.getString(columnIndexLongitude);

                    locations.add(new LocationData(id, latitude, longitude));
                } while (cursor.moveToNext());
            }
        }
        return locations;
    }

    /**
     * Deletes a location from the database based on its id.
     * @param locationId    The id of the location to be deleted.
     */
    public void deleteLocation(int locationId){
        database.delete(DatabaseHelper.TABLE_LOCATIONS, DatabaseHelper.COLUMN_ID + "=?",
                new String[]{String.valueOf(locationId)});
    }
}
