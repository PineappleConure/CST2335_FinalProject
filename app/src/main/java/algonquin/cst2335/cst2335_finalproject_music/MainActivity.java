package algonquin.cst2335.cst2335_finalproject_music;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import algonquin.cst2335.cst2335_finalproject_music.databinding.ItemMusicCollectBinding;

/**
 * The main activity of the application responsible for displaying the initial fragment
 * and managing back stack navigation.
 */
public class MainActivity extends AppCompatActivity {



    /**
     * Called when the activity is starting.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.root, new SearchFragment())
                .commit();
    }

    /**
     * Called when the activity has detected the user's press of the back key.
     */
    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count >= 2) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }

    }
}