package algonquin.cst2335.cst2335_finalproject.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import algonquin.cst2335.cst2335_finalproject.Dictionary;

import java.util.List;

@Dao
public interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Dictionary dictionary);

    @Update
    void update(Dictionary dictionary);

    @Query("SELECT * FROM dictionary ORDER BY word_id ASC")
    List<Dictionary> getWord();

    @Query("SELECT * FROM dictionary WHERE word_id = :wordId")
    int deleteWord(long wordId);

    @Query("DELETE FROM dictionary")
    void deleteAll();
}
