package algonquin.cst2335.cst2335_finalproject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import algonquin.cst2335.cst2335_finalproject.database.DictionaryDao;

/**
 * Dictionary view model
 * @author Linna Wang
 */
public class DictionaryViewModel extends ViewModel {

    private final MutableLiveData<List<Dictionary>> dictionaryListLiveData;
    private final DictionaryDao dictionaryDao;

    public DictionaryViewModel(DictionaryDao dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
        dictionaryListLiveData = new MutableLiveData<>();
        fetchWordsFromDatabase();
    }

    public LiveData<List<Dictionary>> getWords() {
        fetchWordsFromDatabase();
        return dictionaryListLiveData;
    }

    private void fetchWordsFromDatabase() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            List<Dictionary> words = dictionaryDao.getWord();
            dictionaryListLiveData.postValue(words);
            executorService.shutdown();
        });
    }
    public void deleteWord(Dictionary dictionary) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            dictionaryDao.deleteWord(dictionary.word_id);
            fetchWordsFromDatabase(); // Refresh the list after deletion
            executorService.shutdown();
        });
    }
}

