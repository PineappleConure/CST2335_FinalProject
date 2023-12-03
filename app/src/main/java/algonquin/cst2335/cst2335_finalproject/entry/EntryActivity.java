package algonquin.cst2335.cst2335_finalproject.entry;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import algonquin.cst2335.cst2335_finalproject.MainActivity;
import algonquin.cst2335.cst2335_finalproject.R;
import algonquin.cst2335.cst2335_finalproject.databinding.ActivityEntryBinding;


/**
 * This class shows four buttons that links to four different apps as well as icons
 * @author Linna Wang
 *
 */
public class EntryActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityEntryBinding activityEntryBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEntryBinding = DataBindingUtil.setContentView(this, R.layout.activity_entry);
        activityEntryBinding.setOnClick(this);
    }

    /**
     *
     * @param menu The options menu in which you place your items
     * @return each activity that is linked with the icons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_entry,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.menu_help)
        {
            MainActivity.showAlertDialog(this,getResources().getString(R.string.entry_help_title),getResources().getString(R.string.entry_help_detail),new String[]{getResources().getString(R.string.ok),getResources().getString(R.string.cancel)},null,null);
        } else if(item.getItemId()==R.id.menu_dictionary)
        {
            Intent intent = new Intent(EntryActivity.this, MainActivity.class);
            startActivity(intent);
        }
//        else if (item.getItemId()==R.id.menu_sunrise)
//        {
//            Intent intent = new Intent(EntryActivity.this, Start.class);
//            startActivity(intent);
//        }
//        else if (item.getItemId()==R.id.menu_recipe) {
//            Intent intent = new Intent(EntryActivity.this, CurrencyConverter.class);
//            startActivity(intent);
//        } else if (item.getItemId() == R.id.menu_deezer) {
//            Intent intent = new Intent(EntryActivity.this, Deezer.class);
//            startActivity(intent);
//        }

        return true;
    }

    /**
     *
     * @param view The view that was clicked.
     */
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btn_dictionary)
        {
            Intent intent = new Intent(EntryActivity.this, MainActivity.class);
            startActivity(intent);}
//        } else if (view.getId()==R.id.btn_sunset) {
//            Intent intent = new Intent(EntryActivity.this, Start.class);
//            startActivity(intent);
//        }
//        else if(view.getId()==R.id.btn_recipe)
//        {
//            Intent intent = new Intent(EntryActivity.this, CurrencyConverter.class);
//            startActivity(intent);
//        } else if(view.getId()==R.id.btn_deezer) {
//            Intent intent = new Intent(EntryActivity.this, Deezer.class);
//            startActivity(intent);
//        }


    }


}