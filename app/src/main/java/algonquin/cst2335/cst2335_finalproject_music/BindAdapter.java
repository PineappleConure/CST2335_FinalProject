package algonquin.cst2335.cst2335_finalproject_music;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic RecyclerView Adapter that uses ViewBinding to bind data to ViewHolder.
 * @param <VB> The type of ViewBinding associated with the ViewHolder.
 * @param <Data> The type of data to be displayed in the RecyclerView.
 */
public abstract class BindAdapter<VB extends ViewBinding, Data> extends RecyclerView.Adapter<BindHolder<VB>> {
    /**
     * The list of data items to be displayed in the RecyclerView.
     */
    private List<Data> data = new ArrayList<>();

    /**
     * Gets the list of data items.
     * @return The list of data items.
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public BindHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BindHolder<>(createHolder(parent));
    }

    /**
     * Creates a new ViewBinding associated with the ViewHolder.
     * @param parent The parent ViewGroup into which the new View will be added.
     * @return The created ViewBinding.
     */
    public abstract VB createHolder(ViewGroup parent);

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *        item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull BindHolder<VB> holder, int position) {
        Data d = data.get(position);
        bind(holder.getVb(), d, position);
    }

    /**
     * Binds the data to the ViewBinding in the ViewHolder.
     * @param vb The ViewBinding associated with the ViewHolder.
     * @param data The data item to bind.
     * @param position The position of the item within the adapter's data set.
     */
    public abstract void bind(VB vb, Data data, int position);

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }
}


