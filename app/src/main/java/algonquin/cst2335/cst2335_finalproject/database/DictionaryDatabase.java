package algonquin.cst2335.cst2335_finalproject.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import algonquin.cst2335.cst2335_finalproject.Dictionary;

@Database(entities = {Dictionary.class}, version = 1, exportSchema = false)
public abstract class DictionaryDatabase extends RoomDatabase{
    public abstract DictionaryDao dictionaryDao();
}
