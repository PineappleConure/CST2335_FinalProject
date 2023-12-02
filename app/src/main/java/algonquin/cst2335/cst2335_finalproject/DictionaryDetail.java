package algonquin.cst2335.cst2335_finalproject;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import algonquin.cst2335.cst2335_finalproject.database.DictionaryDao;
import algonquin.cst2335.cst2335_finalproject.database.DictionaryDatabase;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityWordDetailBinding;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class DictionaryDetail extends Fragment implements View.OnClickListener{

    ActivityWordDetailBinding wordDetailBinding;
    DictionaryDatabase dictionaryDatabase;
    DictionaryDao dictionaryDao;
    Dictionary dictionary;

    @Nullable
//    @Override
    public View OnCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        wordDetailBinding = ActivityWordDetailBinding.inflate(inflater, container, false);
        return wordDetailBinding.getRoot();
    }

//    @Override
    public void onCreateOptionMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.help_menu_detail, menu);
    }

    void fillWordDetail(Dictionary dictionary){
        wordDetailBinding.txtWordValue.setText(dictionary.getWord());
        wordDetailBinding.txtDefinitionValue.setText(dictionary.getDefinition());
    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btn_add)
        {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->{
                dictionaryDao.insert(dictionary);
                getActivity().runOnUiThread(
                        ()->MainActivity.snackBar(getContext(),getResources().getString(R.string.add_into_favourite),wordDetailBinding.getRoot()));
                // Adds to database
                getActivity().getSupportFragmentManager().popBackStack();
            });
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
