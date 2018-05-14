package com.epiccrown.me.note.noteme.Helpers;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class SoundPlayer {
    public static final String TAG = "NoteMePlayer";
    public static final String SOUNDS_FOLDER = "Sounds";
    public static final int MAX_SOUNDS = 1;

    public AssetManager assetManager;
    public ArrayList<Sound> mSounds = new ArrayList<>();
    public SoundPool soundPool;

    public SoundPlayer(Context context) {
        this.assetManager = context.getAssets();
        soundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC,0);
        loadSounds();
    }

    private void loadSounds() {
        String[] soundNames;
        try {
            soundNames = assetManager.list(SOUNDS_FOLDER);
            Log.e(TAG, "Found " + soundNames.length + " sounds");
        } catch (IOException ioe) {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for(String filename : soundNames){
            String assetPath = SOUNDS_FOLDER+"/"+filename;
            Sound sound = new Sound(assetPath);
            loadSound(sound);
            mSounds.add(sound);
        }
    }

    public ArrayList<Sound> getmSounds() {
        return mSounds;
    }

    public void loadSound(Sound sound){
        try{
            AssetFileDescriptor afd = assetManager.openFd(sound.getmAssetPath());
            int soundId = soundPool.load(afd,1);
            sound.setmSoundId(soundId);
        }catch (Exception ex){ex.printStackTrace();}
    }

    public void play(Sound sound) {
        Integer soundId = sound.getmSoundId();
        if (soundId == null) {
            return;
        }
        soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public Sound getSoundByName(String name){
        for(Sound sound : mSounds){
            if(sound.getmName().equals(name)) return sound;
        }
        return null;
    }

    public void release(){
        soundPool.release();
    }

}
