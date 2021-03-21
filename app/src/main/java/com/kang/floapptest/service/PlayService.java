package com.kang.floapptest.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.SeekBar;

import com.kang.floapptest.MainActivity;
import com.kang.floapptest.common.CustomMediaPlayer;

import java.io.IOException;

import lombok.Data;
import lombok.SneakyThrows;

@Data
public class PlayService extends Service  {

    private static final String TAG = "PlayService";
    private CustomMediaPlayer mp;
    private final IBinder mBinder = new LocalBinder();

    private MainActivity mainActivity;
    private Handler handler = new Handler();
    private Thread uiHandleThread;
    private boolean threadStatus = false;


    public PlayService() {
    }

    public CustomMediaPlayer getMediaPlayer(){
        return mp;
    }

    public void setMainActivity(MainActivity mainActivity){
        Log.d(TAG, "setMainActivity: 실행됨");
        this.mainActivity = mainActivity;
    }

    public void onPrepared(String songUrl) throws IOException {

        mp.reset();
        MediaPlayer mp2 = (MediaPlayer)mp; //Custom이라서 안 먹음거임?
        mp2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                seekBarUiHandle();
            }
        });
        mp2.setDataSource(songUrl);
        mp2.prepareAsync();

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



    public void seekBarUiHandle(){
        uiHandleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (mainActivity.isPlaying == 1 ) {

                    handler.post(new Runnable() {// runOnUiThread랑 같음, 대신 이렇게 쓰면 uiHandleThread 쓰레드를 원하는데서 참조가능
                        @Override //UI 변경하는 애만 메인 스레드에게 메시지를 전달
                        public void run() {
                            mainActivity.seekBar.setProgress(mp.getCurrentPosition());

                            if (mp.getCurrentPosition() >= mp.getDuration()) {
                                mainActivity.musicStop();
                            }
                        }
                    });

                    try {
                        Thread.sleep(1000);
                        if(threadStatus){
                            uiHandleThread.interrupt(); //그 즉시 스레드 종료시키기 위해(강제종료), sleep을 무조건 걸어야 된다. 스레드가 조금이라도 쉬어야 동작함
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        uiHandleThread.start();

    }


}