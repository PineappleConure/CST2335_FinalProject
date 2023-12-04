package algonquin.cst2335.cst2335_finalproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import algonquin.cst2335.cst2335_finalproject.database.DictionaryDao;
import algonquin.cst2335.cst2335_finalproject.database.DictionaryDatabase;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityFavouritelistingBinding;

import java.util.ArrayList;

/**
 * Dictionary Favourite words class
 * @author Linna Wang
 */
public class FavouriteWords extends Fragment implements OnItemClickListener,OnDeleteClickListener, PositiveClickListener{
    private DictionaryAdapter dictionaryAdapter;
    private ActivityFavouritelistingBinding favouritelistingBinding;
    DictionaryDatabase dictionaryDatabase;
    DictionaryDao dictionaryDao;
    DictionaryViewModel dictionaryViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        favouritelistingBinding = DataBindingUtil.inflate(inflater, R.layout.activity_favouritelisting, container, false);
        setHasOptionsMenu(true);

        // Initialize the database and DAO
        dictionaryDatabase = Room.databaseBuilder(getContext(), DictionaryDatabase.class, API_KEYS.DATABASE_NAME).build();
        dictionaryDao = dictionaryDatabase.dictionaryDao();

        return favouritelistingBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favouritelistingBinding.rvWords.setLayoutManager(linearLayoutManager);

        // Provide the DAO to the ViewModel
        dictionaryViewModel = new ViewModelProvider(this, new DictionaryViewModelFactory(dictionaryDao)).get(DictionaryViewModel.class);
        dictionaryAdapter = new DictionaryAdapter(new ArrayList<>(), true);
        dictionaryAdapter.setOnItemClickListener(this::onItemClickListener);
        dictionaryAdapter.setOnDeleteClickListener(this::onDeleteClickListener);
        favouritelistingBinding.rvWords.setAdapter(dictionaryAdapter);

        // Observe changes in the database using LiveData
        dictionaryViewModel.getWords().observe(getViewLifecycleOwner(), words -> {
            dictionaryAdapter.setData(words);
        });
    }


    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_help)
        {
            MainActivity.showAlertDialog(getContext(),getResources().getString(R.string.favourite_help_title),getResources().getString(R.string.favourite_help_message),
                    new String[]{getResources().getString(R.string.ok),
                            getResources().getString(R.string.cancel)},null,null);
        }
        return true;
    }

    @Override
    public void onItemClickListener(Dictionary dictionary, int position) {

        Fragment fragment = new DictionaryDetail();
        Bundle bundle = new Bundle();
        bundle.putInt(API_KEYS.FRAGMENT_FAVOURITE,1);
        bundle.putParcelable(API_KEYS.WORD_DETAIL, dictionary);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(API_KEYS.WORD_DETAIL).commit();
    }

    @Override
    public void onDeleteClickListener(Dictionary dictionary, int position) {
        MainActivity.showAlertDialog(getContext(),
                getResources().getString(R.string.do_you_want_delete_record) + position,
                getResources().getString(R.string.please_confirm),
                new String[]{getResources().getString(R.string.yes), getResources().getString(R.string.cancel)},
                dict -> {
                    dictionaryViewModel.deleteWord(dictionary);
                    dictionaryAdapter.notifyItemRemoved(position); // Notify the adapter about the item removal
                },
                dictionary);
    }


    @Override
    public void onUserConfirmation(Dictionary dictionary) {
        dictionaryViewModel.deleteWord(dictionary);

        getActivity().runOnUiThread(() -> {
            MainActivity.snackBar(getContext(), getResources().getString(R.string.selected_record_deleted_successfully), favouritelistingBinding.getRoot());
            getActivity().getSupportFragmentManager().popBackStack();
            dictionaryAdapter.notifyDataSetChanged(); // Refresh adapter
        });
    }
}

