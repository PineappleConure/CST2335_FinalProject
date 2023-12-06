package algonquin.cst2335.cst2335_finalproject_music;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.List;

/**
 * A class representing a music item with title, album information, duration, and contributors.
 */
public class MusicItem implements Serializable {

    /**
     * The title of the music item.
     */
    public String title;

    /**
     * The album information associated with the music item.
     */
    public Album album;

    /**
     * The duration of the music item in seconds.
     */
    public int duration;

    /**
     * The list of contributors (artists) associated with the music item.
     */
    public List<Contributor> contributors;

    /**
     * A static nested class representing the album information.
     */
    public static class Album implements Serializable{
        /**
         * The cover image URL of the album.
         */
        public String cover;

        /**
         * The title of the album.
         */
        public String title;
    }

    /**
     * A static nested class representing a contributor (artist) with name and picture information.
     */
    public static class Contributor implements Serializable{

        /**
         * The name of the contributor.
         */
        public String name;

        /**
         * The URL of the contributor's picture (big).
         */
        @SerializedName("picture_big")
        public String picture;
    }
}
