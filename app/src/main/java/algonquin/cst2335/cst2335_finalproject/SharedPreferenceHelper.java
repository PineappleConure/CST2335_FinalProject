package algonquin.cst2335.cst2335_finalproject;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Dictionary Shared preference class
 * @author Linna Wang
 */
public class SharedPreferenceHelper {

    /**
     * Initialize the SharePreferences
     */
    private  static SharedPreferences sharedPreferences;
    /**
     * Initialize the SharedPreferences Editor
     */
    private  static SharedPreferences.Editor editor;

    /**
     * Set the context and editor of the SharePreferences object
     * @param context
     * @param preferenceName
     */
    public static void initialize(Context context,String preferenceName)
    {
        if(sharedPreferences==null)
        {
           sharedPreferences = context.getSharedPreferences(preferenceName,Context.MODE_PRIVATE);
           editor = sharedPreferences.edit();
        }
    }

    /**
     * Sets the String value of the SharedPreferences
     * @param key refers to the key of the saved String
     * @param value is the String being saved
     */
    public static void setStringValue(String key,String value)
    {
        editor.putString(key,value).commit();
    }

    /**
     * Returs the value of the sabved SharedPreferences
     * @param key of the saved String
     * @returns the saved String
     */
    public static  String getStringValue(String key)
    {
       return sharedPreferences.getString(key,null);
    }

}
