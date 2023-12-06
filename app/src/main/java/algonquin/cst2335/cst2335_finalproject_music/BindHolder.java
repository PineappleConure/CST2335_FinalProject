package algonquin.cst2335.cst2335_finalproject_music;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * ViewHolder class for a RecyclerView that uses ViewBinding.
 * @param <VB> The type of ViewBinding associated with the ViewHolder.
 */
public class BindHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
    /**
     * The ViewBinding associated with the ViewHolder.
     */
    private VB vb;

    /**
     * Constructs a new BindHolder with the given ViewBinding.
     * @param vb The ViewBinding associated with the ViewHolder.
     */
    public BindHolder(VB vb) {
        super(vb.getRoot());
        this.vb = vb;
    }

    /**
     * Gets the ViewBinding associated with the ViewHolder.
     * @return The ViewBinding associated with the ViewHolder.
     */
    public VB getVb() {
        return vb;
    }
}

