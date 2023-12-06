package algonquin.cst2335.cst2335_finalproject_music;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;

import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivitySearchFragmentBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.FragmentCollectBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.ItemMusicBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.ItemMusicCollectBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * A fragment that displays a collection of music items.
 */
public class CollectFragment extends Fragment {
        /**
         * The adapter for the RecyclerView displaying the collected music items.
         */
        private BindAdapter<ItemMusicCollectBinding, MusicItem> adapter = new BindAdapter<ItemMusicCollectBinding, MusicItem>() {

        /**
         * Creates a new instance of the ViewBinding associated with the ViewHolder.
         * @param parent The parent ViewGroup into which the new View will be added.
         * @return A new instance of the ViewBinding associated with the ViewHolder.
         */
        @Override
        public ItemMusicCollectBinding createHolder(ViewGroup parent) {
            return ItemMusicCollectBinding.inflate(getLayoutInflater(), parent, false);
        }

        /**
         * Binds the data from a MusicItem to the associated ViewBinding in the RecyclerView.
         * @param item The ViewBinding associated with the ViewHolder.
         * @param data The data item to bind.
         * @param position The position of the item within the adapter's data set.
         */
        @Override
        public void bind(ItemMusicCollectBinding item, MusicItem data, int position) {
            item.tvArt.setText(data.contributors.get(0).name);
            item.tvName.setText(data.title);
            Glide.with(item.tvName).load(data.album.cover).into(item.ivImage);
            item.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DetailFragment fragment = DetailFragment.newInstance(data);
                    FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();

                    FragmentTransaction fragmentTransaction = supportFragmentManager
                            .beginTransaction();
                    fragmentTransaction
                            .add(R.id.root, fragment)
                            .addToBackStack("DetailFragment")
                            .hide(CollectFragment.this)
                            .show(fragment)
                            .commit();
                }
            });
            item.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(requireActivity())
                            .setTitle("Message")
                            .setMessage("Do you want to delete this collection")
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Database database = new Database(requireActivity());
                                    database.deleteCollect(data);
                                    MusicItem remove = getData().remove(position);
                                    notifyItemRemoved(position);
                                    Snackbar.make(requireView(), "delete success", Snackbar.LENGTH_LONG)
                                            .setAction("undo", new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    database.addCollect(remove);
                                                    getData().add(position, remove);
                                                    notifyItemInserted(position);
                                                }
                                            }).show();
                                }
                            }).setPositiveButton("Cancel", null).show();

                }
            });
        }
    };


    /**
     * Called to create the view hierarchy associated with the fragment.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return The View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentCollectBinding binding = FragmentCollectBinding.inflate(getLayoutInflater());
        adapter.getData().addAll(new Database(requireContext()).getCollect());
        binding.rvList.setAdapter(adapter);

        // Enable the options menu in the Toolbar
        setHasOptionsMenu(true);

        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.my_menu_music, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    /**
     * This hook is called whenever an item in your options menu is selected.
     * @param item The menu item that was selected.
     *
     * @return False to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.item_1) {
            // Handle the add to favorite click
            goToFavoriteDialog();
            return true;
        } else if (itemId == R.id.item_2) {
            // Handle the about item click
            displayInfo();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Displays a confirmation dialog for deleting the entire collection.
     */
    private void goToFavoriteDialog() {

        new AlertDialog.Builder(requireActivity())
                .setTitle("Notice")
                .setMessage("Do you want to see your favorite list?")
                .setNegativeButton("Yes", (dialog, which) -> {

                    // Notify the adapter about the data change
                    adapter.notifyDataSetChanged();

                    Snackbar.make(requireView(), "Your Favorite List", Snackbar.LENGTH_LONG)
                            .setAction("Undo", v -> {
                                // Notify the adapter about the data change
                                adapter.notifyDataSetChanged();
                            }).show();
                })
                .setPositiveButton("Cancel", null)
                .show();
    }

    private void displayInfo() {

        new AlertDialog.Builder(requireActivity())
                .setTitle("About APP")
                .setMessage("This is the application for search for music with artists names.")
                .setNegativeButton("Close", null)
                .show();


    }

}


