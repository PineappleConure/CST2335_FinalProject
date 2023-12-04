package algonquin.cst2335.cst2335_finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LocationDAO {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public LocationDAO(Context context){
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public long saveLocation(LocationData location){
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_LATITUDE, location.getLatitude());
        values.put(DatabaseHelper.COLUMN_LONGITUDE, location.getLongitude());

        long insertedId = database.insert(DatabaseHelper.TABLE_LOCATIONS, null, values);
        return insertedId;

    }

    public List<LocationData> getSavedLocations() {
        List<LocationData> locations = new ArrayList<>();

        Cursor cursor = database.query(
                DatabaseHelper.TABLE_LOCATIONS,
                new String[]{DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_LATITUDE, DatabaseHelper.COLUMN_LONGITUDE},
                null,null,null,null,null);
        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID));
            String latitude = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LATITUDE));
            String longitude = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LONGITUDE));

            locations.add(new LocationData(id, latitude, longitude));
        }
        cursor.close();
        return locations;
    }
}
