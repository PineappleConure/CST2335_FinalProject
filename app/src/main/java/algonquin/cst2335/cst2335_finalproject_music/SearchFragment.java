package algonquin.cst2335.cst2335_finalproject_music;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import algonquin.cst2335.cst2335_finalproject_music.databinding.ActivitySearchFragmentBinding;
import algonquin.cst2335.cst2335_finalproject_music.databinding.ItemMusicBinding;

/**
 * A fragment that allows users to search for music artists and view their top tracks.
 */
public class SearchFragment extends Fragment {

        /**
         * The adapter for displaying search results.
         */
        private BindAdapter<ItemMusicBinding, MusicItem> adapter = new BindAdapter<ItemMusicBinding, MusicItem>() {

            /**
             * Creates a new instance of ItemMusicBinding to be used as a ViewHolder for the RecyclerView.
             * @param parent The parent ViewGroup into which the new View will be added.
             * @return A new instance of ItemMusicBinding.
             */
            @Override
            public ItemMusicBinding createHolder(ViewGroup parent) {
                return ItemMusicBinding.inflate(getLayoutInflater(), parent, false);
            }

            /**
             * Binds the data to the views within the ItemMusicBinding instance for the specified
             * position in the RecyclerView.
             * @param item The ViewBinding associated with the ViewHolder.
             * @param data The data item to bind.
             * @param position The position of the item within the adapter's data set.
             */
            @Override
            public void bind(ItemMusicBinding item, MusicItem data, int position) {
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
                                .hide(SearchFragment.this)
                                .show(fragment)
                                .commit();
                    }
                });
            }
        };

    /**
     * Creates and returns the view hierarchy associated with the fragment.
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
            ActivitySearchFragmentBinding binding = ActivitySearchFragmentBinding.inflate(getLayoutInflater());
            binding.rvList.setAdapter(adapter); // setting adapter for the RecyclerView

            // OnClick listener for "Collect" button
            binding.btnCollect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FragmentManager supportFragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = supportFragmentManager
                            .beginTransaction();
                    fragmentTransaction
                            .add(R.id.root, new CollectFragment())
                            .addToBackStack("CollectFragment")
                            .hide(SearchFragment.this)
                            .show(new CollectFragment())
                            .commit();
                }
            });

            // to store the search key
            SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

            // OnClick listener for "Search" button
            binding.btnSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String artName = binding.etSearch.getText().toString();
                    defaultSharedPreferences.edit().putString("searchKey",artName).apply();
                    RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.deezer.com/search/artist/?q=" + artName, response -> {
                        MusicLinkData data = new Gson().fromJson(response, MusicLinkData.class);
                        if (data != null && !data.data.isEmpty()) {
                            getMusicItems(data.data.get(0).tracklist);
                        }
                    }, error -> {

                    });
                    requestQueue.add(stringRequest);
                }
            });
            String searchKey = defaultSharedPreferences.getString("searchKey", "");
            if (!searchKey.isEmpty()) {
                binding.etSearch.setText(searchKey);
                binding.btnSearch.performClick();
            }

            return binding.getRoot();
        }

    /**
     * Fetches music items based on the provided tracklist URL and updates the RecyclerView adapter.
     * @param tracklist The URL of the tracklist.
     */
    private void getMusicItems(String tracklist) {
            RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, tracklist, response -> {
                MusicData data = new Gson().fromJson(response, MusicData.class);
                if (data != null && !data.data.isEmpty()) {
                    adapter.getData().clear();
                    adapter.getData().addAll(data.data);
                    adapter.notifyDataSetChanged();
                }
            }, error -> {
            });
            requestQueue.add(stringRequest);
        }


    }