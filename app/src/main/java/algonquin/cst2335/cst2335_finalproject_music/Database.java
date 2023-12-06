package algonquin.cst2335.cst2335_finalproject_music;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * A SQLite database helper class for managing the collection of MusicItem objects.
 */
public class Database extends SQLiteOpenHelper {
    /**
     * Constructs a new Database helper with the specified context.
     * @param context The context to use for database creation.
     */
    public Database(@Nullable Context context) {
        super(context, "name", null, 1);
    }

    /**
     * Called when the database is created for the first time.
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists collect(id integer primary key autoincrement" +
                ",json text)");
    }

    /**
     * Called when the database needs to be upgraded.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * Adds a MusicItem to the collection in the database.
     * @param musicItem The MusicItem to be added.
     */
    public void addCollect(MusicItem musicItem) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("json", new Gson().toJson(musicItem));
        writableDatabase.insert("collect", null, values);
    }

    /**
     * Retrieves the list of MusicItem objects from the collection in the database.
     * @return A list of MusicItem objects stored in the database.
     */
    public List<MusicItem> getCollect() {
        ArrayList<MusicItem> musicItems = new ArrayList<>();
        SQLiteDatabase writableDatabase = getWritableDatabase();
        Cursor cursor = writableDatabase.rawQuery("select * from collect", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String json = cursor.getString(cursor.getColumnIndex("json"));
            musicItems.add(new Gson().fromJson(json, MusicItem.class));
        }
        return musicItems;
    }

    /**
     * Deletes a MusicItem from the collection in the database.
     * @param data The MusicItem to be deleted.
     */
    public void deleteCollect(MusicItem data) {
        SQLiteDatabase writableDatabase = getWritableDatabase();

        writableDatabase.delete("collect", "json = ?", new String[]{new Gson().toJson(data)});
    }
}
