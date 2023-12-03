package algonquin.cst2335.cst2335_finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import algonquin.cst2335.cst2335_finalproject.databinding.WordListItemBinding;
import algonquin.cst2335.cst2335_finalproject.databinding.WordListItemWithDeleteBinding;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Dictionary Adapter
 * @author Linna Wang
 */
public class DictionaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Dictionary> data;
    Boolean isFavourite;
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public DictionaryAdapter(List<Dictionary>data, boolean isFavourite)
    {
        this.data = data;
        this.isFavourite = isFavourite;
    }

    public void setFavourite(Boolean favourite) {
        isFavourite = favourite;
    }

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
        Set<String> uniqueWords = new HashSet<>();
        for (Dictionary item : dictionary) {
            if (!uniqueWords.contains(item.getWord())) {
                uniqueWords.add(item.getWord());
                this.data.add(item);
            }
        }
        notifyDataSetChanged();
    }


    static class DictionaryViewHolder extends  RecyclerView.ViewHolder {
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

    static class DictionaryViewHolderWithDelete extends  RecyclerView.ViewHolder {
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
