package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import algonquin.cst2335.cst2335_finalproject.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

/**
 * Dictionary Main activity class
 * @author Linna Wang
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding activityMainBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        // Only add the MainFragment without adding it to the back stack
        getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).commit();
    }


    public static void createToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        // Check if there's anything in the back stack before popping
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            // If no entries are left in the back stack, finish the activity
            finish();
        }
    }


    public static void snackBar(Context context, String message, View view) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void showAlertDialog(Context context, String title, String message, String[] buttonTitles, PositiveClickListener positiveClickListener, Dictionary dictionary) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonTitles[0], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (positiveClickListener != null) {
                            positiveClickListener.onUserConfirmation(dictionary);
                        }
                    }
                })
                .setNegativeButton(buttonTitles[1], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Handle negative button click here if necessary
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}