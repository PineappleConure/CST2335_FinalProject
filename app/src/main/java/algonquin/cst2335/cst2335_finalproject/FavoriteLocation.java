package algonquin.cst2335.cst2335_finalproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

@Entity
public class FavoriteLocation{

        @ColumnInfo(name = "latitude")
        protected String latitude;
        @ColumnInfo(name = "longitude")
        protected String longitude;

        @ColumnInfo(name = "sunrise")
        protected String sunrise;

        @ColumnInfo(name = "sunset")
        protected String sunset;

//        @ColumnInfo(name="save")
//        protected boolean isFavorite;
//
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        public int id;
        public FavoriteLocation(String latitude, String longitude, String sunrise, String sunset) {
                this.latitude = latitude;
                this.longitude = longitude;
//                this.isFavorite = isFavorite;
                this.sunrise = sunrise;
                this.sunset = sunset;
        }

        public int getId(){return id;}
        public void setId(int id){this.id = id;}
        public String getLatitude() {
        return latitude;
}
        public String getLongitude() {
        return longitude;
}
//        public boolean getIsFavorite(){return isFavorite;}
//        public void setIsFavorite(boolean isFavorite){this.isFavorite = isFavorite;}
        public String getSunrise(){return sunrise;}
        public void setSunrise(String sunrise){this.sunrise = sunrise;}
        public String getSunset(){return sunset;}
        public void setSunset(String sunset){this.sunset = sunset;}

}
