package algonquin.cst2335.cst2335_finalproject;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import algonquin.cst2335.cst2335_finalproject.database.DictionaryDao;


/**
 * Dictionary view model factory
 * @author Linna Wang
 */
public class DictionaryViewModelFactory implements ViewModelProvider.Factory {
    private final DictionaryDao dictionaryDao;

    public DictionaryViewModelFactory(DictionaryDao dictionaryDao) {
        this.dictionaryDao = dictionaryDao;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(DictionaryViewModel.class)) {
            return (T) new DictionaryViewModel(dictionaryDao);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}
