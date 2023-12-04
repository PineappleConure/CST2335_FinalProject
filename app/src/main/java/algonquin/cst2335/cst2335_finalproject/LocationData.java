package algonquin.cst2335.cst2335_finalproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

public class LocationData {
    private int id;
    private String latitude;
    private String longitude;

    public LocationData(int id, String latitude, String longitude){
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId(){return id;}
    public String getLatitude(){return latitude;}
    public String getLongitude(){return longitude;}



}
