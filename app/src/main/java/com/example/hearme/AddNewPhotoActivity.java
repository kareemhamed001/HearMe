package com.example.hearme;

import static com.example.hearme.Constants.extra_photo_uri;
import static com.example.hearme.Constants.extra_sound_uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.net.URI;

public class AddNewPhotoActivity extends AppCompatActivity {

    ImageView mNewPhotoIV;
    ImageView mNewSoundIV;

    private static final int Read_photo_from_gallery_permition = 130;
    private static final int PICK_IMAGE = 120;
    private static final int Read_Sound_from_gallery_permition = 131;
    private static final int PICK_SOUND = 121;
    Uri mselectedPhotoURI;
    Uri mselectedSoundURI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_photo);
        mNewPhotoIV = findViewById(R.id.imageView_NewPhoto);
        mNewSoundIV = findViewById(R.id.imageView_soundAded);
        Button selectPhotoBT = findViewById(R.id.button_select_photo);
        Button selectsoundBT = findViewById(R.id.button_select_sound);
        Button submitBT = findViewById(R.id.button_submit);
        selectPhotoBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });

        selectsoundBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectSound();
            }
        });

        submitBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == Read_photo_from_gallery_permition) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                firePickPhotoIntent();
            } else {
                Toast.makeText(this, R.string.Read_permition_needed_access_files, Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == Read_Sound_from_gallery_permition) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                firePickSoundIntent();
            } else {
                Toast.makeText(this, R.string.Read_permition_needed_access_files, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE) {
            if (data != null && data.getData() != null) {
                setSelectedPhoto(data.getData());

                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this, "فشل في الوصول الي الصوره", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == PICK_SOUND) {
            if (resultCode == RESULT_OK && data != null && data.getData() != null) {
                setSelectedSound(data.getData());
                getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                Toast.makeText(this, "فشل في الوصول الي الصوت", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectPhoto() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_photo_from_gallery_permition);
        } else {
            firePickPhotoIntent();
        }

    }

    private void setSelectedPhoto(Uri data) {
        mNewPhotoIV.setImageURI(data);
        mselectedPhotoURI = data;
    }


    private void firePickPhotoIntent() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_picture)), PICK_IMAGE);
    }


    private void selectSound() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Read_Sound_from_gallery_permition);
        } else {
            firePickSoundIntent();
        }
    }

    private void setSelectedSound(Uri data) {
        mNewSoundIV.setVisibility(View.VISIBLE);
        mselectedSoundURI = data;
    }

    private void firePickSoundIntent() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("audio/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_Sound)), PICK_SOUND);
    }

    private void submit() {
        if (mselectedPhotoURI != null || mselectedSoundURI != null) {

            Intent intent = new Intent();
            intent.putExtra(Constants.extra_photo_uri, mselectedPhotoURI);
            intent.putExtra(Constants.extra_sound_uri, mselectedSoundURI);
            setResult(RESULT_OK, intent);
            finish();

        } else {
            Toast.makeText(this, R.string.select_picture_and_sound, Toast.LENGTH_LONG).show();
        }
    }
}