package com.epiccrown.me.note.noteme.Helpers;

public class Sound {
    private String mAssetPath;
    private String mName;
    private Integer mSoundId;

    public Sound(String mAssetPath) {
        this.mAssetPath = mAssetPath;
        String[] components = mAssetPath.split("/");
        String filename = components[components.length -1];
        mName = filename.replace(".wav","");
    }

    public String getmAssetPath() {
        return mAssetPath;
    }

    public String getmName() {
        return mName;
    }

    public Integer getmSoundId() {
        return mSoundId;
    }

    public void setmSoundId(Integer mSoundId) {
        this.mSoundId = mSoundId;
    }
}
