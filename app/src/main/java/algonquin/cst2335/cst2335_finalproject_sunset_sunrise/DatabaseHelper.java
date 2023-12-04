package algonquin.cst2335.cst2335_finalproject_sunset_sunrise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class for managing the SQLite database used to store location data.
 * Extends SQLiteOpenHelper to provide database creation and upgrade functionality.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    /** The name of the database. */
    public static final String DATABASE_NAME = "Location.db";

    /** The version of the database. */
    public static final int DATABASE_VERSION = 1;

    /** The name of the table storing location data. */
    public static final String TABLE_LOCATIONS = "locations";

    /** The column name for the unique identifier of each location. */
    public static final String COLUMN_ID = "id";

    /** The column name for storing latitude information. */
    public static String COLUMN_LATITUDE = "latitude";

    /** The column name for storing longitude information. */
    public static final String COLUMN_LONGITUDE = "longitude";

    /**
     *  SQL statment for creating the locations table.
     */
    public static final String DATABASE_CREATE =
            "CREATE TABLE " + TABLE_LOCATIONS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LATITUDE + " TEXT, " +
                    COLUMN_LONGITUDE + " TEXT);";

    /**
     * Constructs a new DatabaseHelper instance.
     * @param context The context in which the database helper will be used.
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time.
     * Executes the SQL statement to create the locations table.
     * @param db The database instance to be created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * Called when the database needs to be upgraded.
     * Drops the existing locations table if it exists and creates a new one.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
    }

}
