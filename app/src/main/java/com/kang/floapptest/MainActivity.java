package com.kang.floapptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kang.floapptest.adapter.SongAdapter;
import com.kang.floapptest.common.CustomMediaPlayer;
import com.kang.floapptest.common.SongEvent;
import com.kang.floapptest.model.Song;
import com.kang.floapptest.service.PlayService;
import com.kang.floapptest.viewmodel.MusicViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity2";
    private MainActivity mContext = MainActivity.this;
    //서비스 관련
    ServiceConnection connection;
    Intent musicIntent;
    //View 관련
    private RecyclerView rvMusicList;
    private MusicViewModel musicViewModel;
    private SongAdapter songAdapter;
    private PlayService playService;
    public CustomMediaPlayer mp;
    public ImageButton btnPlayGlobal;
    public SeekBar seekBar;
    public TextView tvTime;
    public int isPlaying = -1; // 1은 음악재생, -1은 음악멈춤
    // 쓰레드 관련
    public boolean threadStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) { //여기는 test2 branch
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        serviceConnect();// 서비스 바인딩 하기
        initView();
        initData();
        initObserve();
        listner();

        seekBarInit();

        btnPlayGlobal.setOnClickListener(v -> {
            if (isPlaying == 1) {
                Log.d(TAG, "onCreate: 글로벌 버튼 클릭되고 노래멈춤" + isPlaying);
                songPause();
            } else {
                Log.d(TAG, "onCreate: 노래시작" + isPlaying);
                EventBus.getDefault().post(new SongEvent("", isPlaying));
            }
        });


    }

    public void songPrepare(String songUrl) throws IOException {
        seekBarInit();

        Log.d(TAG, "playSong의 MainActivity: " + playService.getMainActivity());
        if (playService.getMainActivity() == null) {
            playService.setMainActivity(mContext);
        }

        if (!songUrl.equals(mp.getDatasource())) {

            Log.d(TAG, "songPlay: Song 시작");
            playService.onPrepared(songUrl);

        } else {
            Log.d(TAG, "songPrepare: 같은 노래 클릭");
            //playService.uiHandleThread.interrupt();
            isPlaying = isPlaying * -1;
            mp.reset();
            playService.onPrepared(songUrl);
            //EventBus.getDefault().post(new SongEvent(songUrl, isPlaying));

        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void songPlay(SongEvent event) {
        isPlaying = 1;
        btnPlayGlobal.setImageResource(android.R.drawable.ic_media_pause);
        mp.start();
        playService.seekBarUiHandle();
        Log.d(TAG, "eventSubscrive: event 받음" + event.songUrl + " isplaying: " + isPlaying);
    }

    private void listner() {

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                // 유저가 SeekBar를 클릭할 때
                if (fromUser) {
                    mp.seekTo(progress);
                }

                int m = progress / 60000;
                int s = (progress % 60000) / 1000;
                String strTime = String.format("%02d:%02d", m, s);
                tvTime.setText(strTime);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

        });

    }


    private void serviceConnect() {
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                PlayService.LocalBinder mb = (PlayService.LocalBinder) service;
                playService = mb.getService();
                mp = playService.getMediaPlayer();
                Log.d(TAG, "onServiceConnected: " + mp);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mp.stop();
                mp.release();
            }
        };
        musicIntent = new Intent(getApplicationContext(), PlayService.class);
        bindService(musicIntent, connection, BIND_AUTO_CREATE);

    }


    private void initView() {
        seekBar = findViewById(R.id.seekbar);
        tvTime = findViewById(R.id.currentPosTV);
        btnPlayGlobal = findViewById(R.id.btn_play_global);
        rvMusicList = findViewById(R.id.rv_music_list);
        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMusicList.setLayoutManager(layoutManager);
        songAdapter = new SongAdapter(mContext);
        rvMusicList.setAdapter(songAdapter);
    }


    private void initData() {
        musicViewModel.findAll();
    }

    private void initObserve() {
        musicViewModel.subscribe().observe(MainActivity.this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> musics) {
                songAdapter.setMusics(musics);
            }
        });
    }


    public void seekBarInit() {
        seekBar.setMax(100000);
        seekBar.setProgress(0);
    }

    public void songPause() {
        mp.pause();
        isPlaying = -1;
        btnPlayGlobal.setImageResource(android.R.drawable.ic_media_play);
    }

    public void songStop() {
        mp.reset();
        mp.seekTo(0);
        seekBar.setProgress(0);
        threadStatus = true;
        btnPlayGlobal.setImageResource(android.R.drawable.ic_media_play);
        isPlaying = -1;
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}