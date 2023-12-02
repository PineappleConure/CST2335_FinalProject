package algonquin.cst2335.cst2335_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.cst2335_finalproject.databinding.WordListItemBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.WordListItemWithDeleteBinding;

import java.util.List;

/**
 * @author Linna Wang
 * @version 1.0
 */
public class DictionaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * This saves the list of dictionary type objects
     */
    private List<Dictionary> data;
    /**
     * This variable denotes if a dictionary is a favourite or not
     */
    Boolean isFavourite;
    /**
     * Initialize OnItemClickListener
     */
    private OnItemClickListener onItemClickListener;
    /**
     * Initialize OnDeleteClickListener
     */
    private OnDeleteClickListener onDeleteClickListener;

    /**
     * Deletes data from the dictionary list
     * @param onDeleteClickListener
     */
    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    /**
     * Saves a dictionary into favourites
     * @param data is the list of dictionary
     * @param isFavourite if added
     */
    public DictionaryAdapter(List<Dictionary>data, boolean isFavourite)
    {
        this.data = data;
        this.isFavourite = isFavourite;
    }

    /**
     * This methods sets if a dictionary as favourite
     * @param favourite
     */
    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

    /**
     * Sets the attribute of a dictionary
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(isFavourite){
            WordListItemWithDeleteBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.word_list_item_with_delete,parent,false);
            return new DictionaryViewHolderWithDelete(view);
        }
        else
        {
            WordListItemBinding view = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),R.layout.word_list_item,parent,false);
            return new DictionaryViewHolder(view);
        }

    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) { // int position originally has red
        Dictionary dictionary = data.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClickListener(dictionary,position);
                }
            }
        });
        if(holder instanceof DictionaryViewHolder)
        {
            ((DictionaryViewHolder)holder).onBind(dictionary);
        }
        else
        {


            ((DictionaryViewHolderWithDelete)holder).onBind(dictionary);
            ((DictionaryViewHolderWithDelete)holder).wordListItemWithDeleteBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onDeleteClickListener!=null)
                    {
                        onDeleteClickListener.onDeleteClickListener(dictionary,position);
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Dictionary> dictionary) {
        this.data.clear();
        this.data=dictionary;
        notifyDataSetChanged();
    }

    static class DictionaryViewHolder extends  RecyclerView.ViewHolder
    {
        WordListItemBinding wordListItemBinding;
        public DictionaryViewHolder(@NonNull WordListItemBinding wordListItemBinding) {
            super(wordListItemBinding.getRoot());
            this.wordListItemBinding =wordListItemBinding;

        }
        void onBind(Dictionary dictionary)
        {
            wordListItemBinding.txtWordValue.setText(dictionary.getWord());
        }

    }

    static class DictionaryViewHolderWithDelete extends  RecyclerView.ViewHolder
    {
        WordListItemWithDeleteBinding wordListItemWithDeleteBinding;
        public DictionaryViewHolderWithDelete(@NonNull WordListItemWithDeleteBinding wordListItemWithDeleteBinding) {
            super(wordListItemWithDeleteBinding.getRoot());
            this.wordListItemWithDeleteBinding =wordListItemWithDeleteBinding;

        }
        void onBind(Dictionary dictionary)
        {
            wordListItemWithDeleteBinding.txtWordValue.setText(dictionary.getWord());
        }

    }

}
