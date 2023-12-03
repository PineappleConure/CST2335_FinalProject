package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import algonquin.cst2335.cst2335_finalproject.database.DictionaryDao;
import algonquin.cst2335.cst2335_finalproject.database.DictionaryDatabase;
import algonquin.cst2335.cst2335_finalproject.databinding.FragmentMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Dictioonary main fragment class
 * @author Linna Wang
 */
public class MainFragment extends Fragment implements OnItemClickListener {
    private DictionaryAdapter dictionaryAdapter;
    private FragmentMainBinding fragmentMainBinding;
    private RequestQueue requestQueue;
    DictionaryDatabase dictionaryDatabase;
    DictionaryDao dictionaryDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentMainBinding = FragmentMainBinding.inflate(inflater,container,false);
        return fragmentMainBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view1, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view1, savedInstanceState);
        SharedPreferenceHelper.initialize(getActivity(),API_KEYS.PREFERENCE_NAME);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragmentMainBinding.rvDictionary.setLayoutManager(linearLayoutManager);
        fragmentMainBinding.pbLoader.setVisibility(View.GONE);
        dictionaryAdapter = new DictionaryAdapter(new ArrayList<>(),false);
        dictionaryAdapter.setOnItemClickListener(this::onItemClickListener);
        fragmentMainBinding.rvDictionary.setAdapter(dictionaryAdapter);
        dictionaryDatabase = Room.databaseBuilder(getActivity(),DictionaryDatabase.class,API_KEYS.DATABASE_NAME).build();
        dictionaryDao = dictionaryDatabase.dictionaryDao();
        setHasOptionsMenu(true);
        // Initialize the RequestQueue (Consider using a singleton for RequestQueue)
        requestQueue = Volley.newRequestQueue(getContext());
        if(SharedPreferenceHelper.getStringValue(API_KEYS.WORD)!=null)
        {
            String word = SharedPreferenceHelper.getStringValue(API_KEYS.WORD);
            fragmentMainBinding.editSearch.setText(word);
            if (isNetworkConnected()) {
                fragmentMainBinding.pbLoader.setVisibility(View.VISIBLE);
                fetchDataFromAPI(word);
            } else {
                MainActivity.createToast(getActivity(), getResources().getString(R.string.no_internet));
            }
        }
        fragmentMainBinding.btnSearch.setOnClickListener(view -> {
            String word = fragmentMainBinding.editSearch.getText().toString();


            if (TextUtils.isEmpty(word)) {
                MainActivity.showAlertDialog(getActivity(), getResources().getString(R.string.hint_word), getResources().getString(R.string.word_warning), new String[]{getResources().getString(R.string.ok),getResources().getString(R.string.cancel)},null,null);
            } else {
                if (isNetworkConnected()) {
                    fragmentMainBinding.pbLoader.setVisibility(View.VISIBLE);
                    fetchDataFromAPI(word);
                } else {
                    MainActivity.createToast(getActivity(), getResources().getString(R.string.no_internet));
                }
            }
        });
    }


    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    private void fetchDataFromAPI(final String word) {
        String apiUrl = API_KEYS.API_URL + Uri.encode(word);

        // Use StringRequest instead of JsonObjectRequest to get the response as a string
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                response -> onApiResponse(response, word),
                this::onApiError);

        requestQueue.add(stringRequest);
    }

    private void onApiResponse(String response, String word) {
        try {
            // Convert the response string to a JSONArray
            JSONArray responseArray = new JSONArray(response);

            // Handle the JSONArray response
            List<Dictionary> dictionaryList = parseDictionaryData(responseArray);
            if (!dictionaryList.isEmpty()) {
                dictionaryAdapter.setData(dictionaryList);
                dictionaryAdapter.notifyDataSetChanged(); // Make sure to notify the adapter
                SharedPreferenceHelper.setStringValue(API_KEYS.WORD, word);
            } else {
                MainActivity.createToast(getContext(), getResources().getString(R.string.no_word_found));
            }
        } catch (JSONException e) {
            Log.e("API_ERROR", "Error occurred while parsing the response: " + e.getMessage());
            MainActivity.createToast(getContext(), getResources().getString(R.string.api_server_error));
        }
        fragmentMainBinding.pbLoader.setVisibility(View.GONE);
    }

    private void onApiError(VolleyError error) {
        fragmentMainBinding.pbLoader.setVisibility(View.GONE);
        Log.e("API_ERROR", "Error occurred while fetching data: " + error.toString());
        MainActivity.createToast(getContext(), getResources().getString(R.string.api_server_error) + error.toString());
    }

    private List<Dictionary> parseDictionaryData(JSONArray responseArray) {
        List<Dictionary> dictionaries = new ArrayList<>();
        Set<String> addedWords = new HashSet<>(); // Set to keep track of added words
        try {
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject wordObject = responseArray.getJSONObject(i);
                String word = wordObject.getString("word");

                // Check if the word has already been added
                if (!addedWords.contains(word)) {
                    addedWords.add(word); // Add the word to the set
                    JSONArray meaningsArray = wordObject.getJSONArray("meanings");

                    for (int j = 0; j < meaningsArray.length(); j++) {
                        JSONObject meaningsObject = meaningsArray.getJSONObject(j);
                        JSONArray definitionsArray = meaningsObject.getJSONArray("definitions");

                        for (int k = 0; k < definitionsArray.length(); k++) {
                            JSONObject definitionObject = definitionsArray.getJSONObject(k);
                            String definition = definitionObject.getString("definition");

                            Dictionary dictionary = new Dictionary(word, definition);
                            dictionaries.add(dictionary);
                        }
                    }
                }
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", "Error parsing JSON data", e);
        }
        return dictionaries;
    }




    @Override
    public void onItemClickListener(Dictionary dictionary, int position) {
        DictionaryDetail fragment = new DictionaryDetail();
        Bundle bundle = new Bundle();
        bundle.putParcelable(API_KEYS.DEFINITION, dictionary);
        fragment.setArguments(bundle);

        // Begin a transaction and add it to the back stack
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null) // 'null' or a name for this back stack state
                .commit();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.help_menu_main,menu);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.menu_help)
        {
            MainActivity.showAlertDialog(getActivity(),getResources().getString(R.string.main_help_title),getResources().getString(R.string.main_help_message),
                    new String[]{getResources().getString(R.string.ok),
                            getResources().getString(R.string.cancel)},null,null);
        }
        else if(item.getItemId()==R.id.menu_favourite)
        {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(()->{
                if(dictionaryDao.getWord().size()>0)
                {
                    getActivity().runOnUiThread(()-> {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new FavouriteWords()).addToBackStack(API_KEYS.FRAGMENT_FAVOURITE).commit();
                    });
                }
                else
                { getActivity().runOnUiThread(()-> {
                        MainActivity.createToast(getActivity(),getActivity().getResources().getString(R.string.no_record_found));
                     });
                }
            });

        }
        return true;
    }

}
