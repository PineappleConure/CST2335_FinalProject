package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.android.volley.toolbox.Volley;
import algonquin.cst2335.cst2335_finalproject.database.DictionaryDao;
import algonquin.cst2335.cst2335_finalproject.database.DictionaryDatabase;
import algonquin.cst2335.cst2335_finalproject.databinding.FragmentMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author Linna Wang
 * @version 1.0
 */
public class MainFragment extends Fragment implements OnItemClickListener {

    /**
     * Initialize DictionaryAdapter
     */
    private DictionaryAdapter dictionaryAdapter;
    /**
     * Initialize FragmentMainBinding
     */
    private FragmentMainBinding fragmentMainBinding;
    /**
     * Initialize RequestQueue object
     */
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
            String airportCode = fragmentMainBinding.editSearch.getText().toString();


            if (TextUtils.isEmpty(airportCode)) {
                MainActivity.showAlertDialog(getActivity(), getResources().getString(R.string.hint_word), getResources().getString(R.string.word_warning), new String[]{getResources().getString(R.string.ok),getResources().getString(R.string.cancel)},null,null);
            } else {
                if (isNetworkConnected()) {
                    fragmentMainBinding.pbLoader.setVisibility(View.VISIBLE);
                    fetchDataFromAPI(airportCode);
                } else {
                    MainActivity.createToast(getActivity(), getResources().getString(R.string.no_internet));
                }
            }
        });
    }


    /**
     * This method checks the network connectivity
     * @return connection status
     */
    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    /**
     * This method fetches data from the API
     * @param word is used to check the word details
     */
    private void fetchDataFromAPI(String word) {
        String apiUrl = API_KEYS.API_URL+ word;

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                response -> {
                    fragmentMainBinding.pbLoader.setVisibility(View.GONE);
                    Log.d("API_RESPONSE", response.toString());
                    List<Dictionary> dictionary = parseDictionaryData(response);
                    if (dictionary.size()>0) {
                        dictionaryAdapter.setData(dictionary);
                        SharedPreferenceHelper.setStringValue(API_KEYS.WORD,word);
                    } else {
                        MainActivity.createToast(getContext(), getResources().getString(R.string.no_word_found));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        fragmentMainBinding.pbLoader.setVisibility(View.GONE);
                        // Log the error
                        Log.e("API_ERROR", "Error occurred while fetching data: " + error.getMessage());
                        MainActivity.createToast(getContext(), getResources().getString(R.string.api_server_error) + error.getMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Populates the ArrayList with list of word details
     * @param response
     * @return
     */
    private List<Dictionary> parseDictionaryData(JSONObject response) {
        List<Dictionary> dictionaries = new ArrayList<>();
        try {
            JSONArray data = response.getJSONArray(API_KEYS.WORD);
            for (int i = 0; i < data.length(); i++) {
                JSONObject dictionaryObject = data.getJSONObject(i);
                Dictionary dictionary = new Dictionary().convertJsonToDictionary(dictionaryObject);
                dictionaries.add(dictionary);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dictionaries;
    }


    @Override
    public void onItemClickListener(Dictionary dictionary, int position) {

        Fragment fragment = new DictionaryDetail();
        Bundle bundle = new Bundle();
        bundle.putParcelable(API_KEYS.DEFINITION,dictionary);
        fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment).addToBackStack(API_KEYS.DEFINITION).commit();


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
