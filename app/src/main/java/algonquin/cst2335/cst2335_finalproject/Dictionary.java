package algonquin.cst2335.cst2335_finalproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.versionedparcelable.ParcelField;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(tableName = "dictionary")
public class Dictionary implements Parcelable{

    public String word, definition;

    @PrimaryKey(autoGenerate = true)
    @NonNull
    public Long word_id;

    public Dictionary(String word, String definition) {
        this.word = word;
        this.definition = definition;
    }

    public Dictionary(Long id, String word, String definition) {
        this.word_id = id;
        this.word = word;
        this.definition = definition;
    }

    public Dictionary() {

    }

    protected Dictionary(Parcel in) {
        word=in.readString();
        definition=in.readString();
        if (in.readByte() == 0 ){
            word_id = null;
        } else {
            word_id = in.readLong();
        }
    }

    public static final Creator<Dictionary> CREATOR = new Creator<Dictionary>() {
        @Override
        public Dictionary createFromParcel(Parcel in) {return new Dictionary(in);}
        @Override
        public Dictionary[] newArray(int size) {return new Dictionary[size];}
    };

//    @NonNull
//    public Dictionary convertJsonToDictionary(JSONObject jsonObject) throws JSONException {
//        JSONObject word_detail = jsonObject.getJSONObject(API_KEYS.WORD);
//        JSONObject definition_detail = jsonObject.getJSONObject(API_KEYS.MEANING);
//        String word = word_detail.getString(API_KEYS.WORD);
//        String definition = definition_detail.getString(API_KEYS.DEFINITION);
//        return new Dictionary(word, definition);
//    }

    @NonNull
    public Dictionary convertJsonToDictionary(JSONObject jsonObject) throws JSONException {
        String word = jsonObject.getString(API_KEYS.WORD);
        String definition = jsonObject.getJSONObject(API_KEYS.MEANING).getString(API_KEYS.DEFINITION);
        return new Dictionary(word, definition);
    }

    public String getWord() {return word;}
    public String getDefinition() {return definition;}

    @Override
    public int describeContents() {return 0;}

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(word);
        parcel.writeString(definition);
        if (word_id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(word_id);
        }
    }

}
