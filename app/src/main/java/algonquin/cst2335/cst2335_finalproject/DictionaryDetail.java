package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import algonquin.cst2335.cst2335_finalproject.database.DictionaryDao;
import algonquin.cst2335.cst2335_finalproject.database.DictionaryDatabase;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityWordDetailBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Dictionary Detail
 * @author Linna Wang
 */
public class DictionaryDetail extends Fragment implements View.OnClickListener{
    ActivityWordDetailBinding wordDetailBinding;
    DictionaryDatabase dictionaryDatabase;
    DictionaryDao dictionaryDao;
    Dictionary dictionary;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        wordDetailBinding = ActivityWordDetailBinding.inflate(inflater, container, false);
        dictionaryDatabase = Room.databaseBuilder(getActivity(), DictionaryDatabase.class, API_KEYS.DATABASE_NAME).build();
        dictionaryDao = dictionaryDatabase.dictionaryDao();

        wordDetailBinding.btnAdd.setOnClickListener(this);

        if (getArguments() != null) {
            dictionary = getArguments().getParcelable(API_KEYS.DEFINITION);
            fillWordDetail(dictionary);
        }
        return wordDetailBinding.getRoot();
    }


    void fillWordDetail(Dictionary dictionary) {
        if (dictionary != null) {
            wordDetailBinding.txtWordValue.setText(dictionary.getWord());
            wordDetailBinding.txtDefinitionValue.setText(dictionary.getDefinition());
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_add && dictionary != null) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(() -> {
                try {
                    dictionaryDao.insert(dictionary); // Insert into database
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Word added to favorites", Toast.LENGTH_SHORT).show());
                } catch (Exception e) {
                    e.printStackTrace(); // Log the error
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Insert failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                } finally {
                    executorService.shutdown(); // Shutdown the executor service
                }
            });
        } else {
            Toast.makeText(getContext(), "Dictionary is null or button ID mismatch", Toast.LENGTH_LONG).show();
        }
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.menu_help)
        {
            MainActivity.showAlertDialog(getContext(),getResources().getString(R.string.detail_help_title),getResources().getString(R.string.detail_help_message),
                    new String[]{getResources().getString(R.string.ok),
                            getResources().getString(R.string.cancel)},null,null);
        }
        return true;
    }
}
