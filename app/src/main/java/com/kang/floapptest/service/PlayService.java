package com.kang.floapptest.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.kang.floapptest.common.CustomMediaPlayer;

public class PlayService extends Service {

    private static final String TAG = "PlayService";
    private CustomMediaPlayer mp;
    private final IBinder mBinder = new LocalBinder();

    public PlayService() {
    }

    public class LocalBinder extends Binder { //패키지가 달라서 public
        public PlayService getService() {
            return PlayService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = new CustomMediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public CustomMediaPlayer getMediaPlayer(){
        return mp;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}