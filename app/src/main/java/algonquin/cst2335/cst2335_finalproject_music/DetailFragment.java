package algonquin.cst2335.cst2335_finalproject_music;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import algonquin.cst2335.cst2335_finalproject_music.databinding.FragmentDetailBinding;

/**
 * A fragment that displays detailed information about a MusicItem.
 */
public class DetailFragment extends Fragment {

    /**
     * The MusicItem associated with this details.
     */
    private MusicItem musicItem;

    /**
     * Default constructor for DetailFragment.
     */
    public DetailFragment() {
    }

    /**
     * Creates a new instance of DetailFragment with the specified MusicItem.
     * @param musicItem The MusicItem to display details for.
     * @return A new instance of DetailFragment.
     */
    public static DetailFragment newInstance(MusicItem musicItem) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("musicItem", musicItem);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Called to do initial creation of the fragment.
     * @param savedInstanceState If the fragment is being re-created from a previous saved state,
     *                           this is the state.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            musicItem = (MusicItem) getArguments().getSerializable("musicItem");
        }
    }

    /**
     * The ViewBinding for this fragment.
     */
    private FragmentDetailBinding binding;

    /**
     * Called to have the fragment instantiate its user interface view.
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        binding = FragmentDetailBinding.bind(view);
        return view;
    }

    /**
     * Called after the fragment's view has been created.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Glide.with(binding.tvArt).load(musicItem.album.cover).into(binding.ivImage);
        binding.tvAlbum.setText("Album:" + musicItem.album.title);
        binding.tvArt.setText("Contributor:" + musicItem.contributors.get(0).name);
        binding.tvTitle.setText("Title:" + musicItem.title);
        binding.tvDuration.setText("Duration:" + String.format("%02d:%02d", (musicItem.duration / 60), (musicItem.duration % 60)));
        binding.btnCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(requireActivity()).addCollect(musicItem);
                Toast.makeText(requireActivity(), "Collected!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}