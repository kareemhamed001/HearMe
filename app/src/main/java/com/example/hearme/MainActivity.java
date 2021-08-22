package com.example.hearme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.hearme.Adapter.PhotoSoundAdapter;
import com.example.hearme.Data.PhotoSound;
import com.example.hearme.Listener.ItemOnClickListener;
import com.example.hearme.Listener.ItemOnLongClickListener;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<PhotoSound> mItems;
    private PhotoSoundAdapter mAdapter;
    private RecyclerView.LayoutManager mlinear;
    private RecyclerView.LayoutManager mgrid;
    private static final int ADD_PHOTO = 145;
    Menu mMenu;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        mItems = new ArrayList<PhotoSound>();
        mAdapter = new PhotoSoundAdapter(mItems, new ItemOnClickListener() {
            @Override
            public void onItemClickListener(int position) {

                playSound(position);
            }
        }, new ItemOnLongClickListener() {
            @Override
            public void onItemLongClickListener(int position) {

                deleteItem(position);
            }
        });
        mlinear = new LinearLayoutManager(this);
        mgrid = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mlinear);
        recyclerView.setAdapter(mAdapter);
        findViewById(R.id.floating_button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startaddnewphotoActivity();
            }
        });
    }

    public void startaddnewphotoActivity() {
        Intent intent = new Intent(MainActivity.this, AddNewPhotoActivity.class);
        startActivityForResult(intent, ADD_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PHOTO) {
            if (data != null && resultCode == RESULT_OK) {
                Uri soundUri = data.getParcelableExtra(Constants.extra_sound_uri);
                Uri photoUri = data.getParcelableExtra(Constants.extra_photo_uri);

                PhotoSound photoSound = new PhotoSound(photoUri, soundUri);
                addItem(photoSound);

            } else {
                Toast.makeText(this, "لم تقم باختيار صوره", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addItem(PhotoSound photoSound) {
        mItems.add(photoSound);
        mAdapter.notifyItemInserted(mItems.size() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mMenu = menu;
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_grid) {

            recyclerView.setLayoutManager(mgrid);
            item.setVisible(false);
            mMenu.findItem(R.id.action_list).setVisible(true);
            return true;
        } else if (item.getItemId() == R.id.action_list) {
            recyclerView.setLayoutManager(mlinear);
            item.setVisible(false);
            mMenu.findItem(R.id.action_grid).setVisible(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void playSound(int position) {
        PhotoSound photoSound = mItems.get(position);

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, photoSound.getSound());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.couldnt_play_sound, Toast.LENGTH_SHORT).show();

        }
    }

    ;

    private void deleteItem(final int position) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(this).setMessage(R.string.delete_item_confirmation).setPositiveButton(R.string.Confrim_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mItems.remove(position);
                        mAdapter.notifyItemRemoved(position);
                    }
                }).setNegativeButton(R.string.cancel_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}