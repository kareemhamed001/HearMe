package com.example.hearme.Adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hearme.Data.PhotoSound;
import com.example.hearme.Listener.ItemOnClickListener;
import com.example.hearme.Listener.ItemOnLongClickListener;
import com.example.hearme.R;

import java.util.ArrayList;

public class PhotoSoundAdapter extends RecyclerView.Adapter<PhotoSoundAdapter.PhotoSoundViewHolder> {
    private ArrayList<PhotoSound> mItem;
    private ItemOnClickListener mitemOnClickListener;
    private ItemOnLongClickListener mitemOnLongClickListener;

    public PhotoSoundAdapter(ArrayList<PhotoSound> mItem, ItemOnClickListener mitemOnClickListener, ItemOnLongClickListener mitemOnLongClickListener) {
        this.mItem = mItem;
        this.mitemOnClickListener = mitemOnClickListener;
        this.mitemOnLongClickListener = mitemOnLongClickListener;
    }

    @NonNull
    @Override
    public PhotoSoundAdapter.PhotoSoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new PhotoSoundViewHolder(view,mitemOnLongClickListener,mitemOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoSoundAdapter.PhotoSoundViewHolder holder, @SuppressLint("RecyclerView") int position) {

        PhotoSound photoSound=mItem.get(position);
        holder.imageView.setImageURI(photoSound.getImage());
        holder.position=position;
    }

    @Override
    public int getItemCount() {
        return mItem.size();
    }

    static class PhotoSoundViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private int position;

        public PhotoSoundViewHolder(@NonNull View itemView,final ItemOnLongClickListener itemOnLongClickListener,final ItemOnClickListener itemOnClickListener) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_view_list_items);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemOnClickListener.onItemClickListener(position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    itemOnLongClickListener.onItemLongClickListener(position);
                    return true;
                }
            });
        }
    }
}
