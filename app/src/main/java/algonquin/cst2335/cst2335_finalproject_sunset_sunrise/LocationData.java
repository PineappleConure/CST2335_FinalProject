package algonquin.cst2335.cst2335_finalproject_sunset_sunrise;

import android.os.Parcel;
import android.os.Parcelable;


public class LocationData implements Parcelable {
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

    // Parcelable implementation
    protected LocationData(Parcel in){
        id = in.readInt();
        latitude = in.readString();
        longitude = in.readString();
    }
    public static final Parcelable.Creator<LocationData> CREATOR = new Parcelable.Creator<LocationData>(){
        @Override
        public LocationData createFromParcel(Parcel in) {
            return new LocationData(in);
        }

        @Override
        public LocationData[] newArray(int size) {
            return new LocationData[size];
        }
    };
    @Override
    public int describeContents(){
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeInt(id);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }


}
