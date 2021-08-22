package com.example.hearme.Data;

import android.net.Uri;

public class PhotoSound {
    private Uri image;
    private Uri sound;

    public PhotoSound(Uri image, Uri sound) {
        this.image = image;
        this.sound = sound;
    }

    public Uri getImage() {
        return image;
    }

    public Uri getSound() {
        return sound;
    }
}
