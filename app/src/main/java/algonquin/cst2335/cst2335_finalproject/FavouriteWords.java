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
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import algonquin.cst2335.cst2335_finalproject.database.DictionaryDao;
import algonquin.cst2335.cst2335_finalproject.database.DictionaryDatabase;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityFavouritelistingBinding;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
        return favouritelistingBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.help_menu_detail,menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favouritelistingBinding.rvWords.setLayoutManager(linearLayoutManager);
        dictionaryViewModel = new ViewModelProvider(this).get(DictionaryViewModel.class);
        dictionaryAdapter = new DictionaryAdapter(new ArrayList<>(),true);
        dictionaryAdapter.setOnItemClickListener(this::onItemClickListener);
        dictionaryAdapter.setOnDeleteClickListener(this::onDeleteClickListener);
        favouritelistingBinding.rvWords.setAdapter(dictionaryAdapter);
        dictionaryDatabase = Room.databaseBuilder(getContext(), DictionaryDatabase.class, API_KEYS.DATABASE_NAME).build();
        dictionaryDao = dictionaryDatabase.dictionaryDao();
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            dictionaryViewModel.dictionaryList.addAll(dictionaryDao.getWord());
            getActivity().runOnUiThread(() -> {
                dictionaryAdapter.setData(dictionaryViewModel.dictionaryList);
            });

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
    public void onUserConfirmation(Dictionary dictionary) {
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            dictionaryDao.deleteWord(dictionary.word_id);

            dictionaryViewModel.dictionaryList.remove(dictionary);

            getActivity().runOnUiThread(()->{
                dictionaryAdapter.notifyDataSetChanged();
            });

            MainActivity.snackBar(getContext(), getResources().getString(R.string.selected_record_deleted_successfully), favouritelistingBinding.getRoot());
            getActivity().getSupportFragmentManager().popBackStack();
        });
    }

    @Override
    public void onDeleteClickListener(Dictionary dictionary, int position) {
        MainActivity.showAlertDialog(getContext(), getResources().getString(R.string.do_you_want_delete_record) + position, getResources().getString(R.string.please_confirm), new String[]{getResources().getString(R.string.yes), getResources().getString(R.string.cancel)}, this, dictionary);
    }
}
