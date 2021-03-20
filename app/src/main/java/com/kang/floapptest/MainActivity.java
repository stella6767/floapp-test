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
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.kang.floapptest.adapter.MusicAdapter;
import com.kang.floapptest.common.CustomMediaPlayer;
import com.kang.floapptest.model.Song;
import com.kang.floapptest.service.PlayService;
import com.kang.floapptest.viewmodel.MusicViewModel;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity2";

    private MainActivity mContext = MainActivity.this;

    private RecyclerView rvMusicList;
    private MusicViewModel musicViewModel;
    private MusicAdapter musicAdapter;
    private PlayService playService;
    public CustomMediaPlayer mp;
    private ImageButton btnPlay;
    public SeekBar seekBar;
    public TextView tvTime;
    public int isPlaying = -1; // 1은 음악재생, -1은 음악멈춤
    // 쓰레드 관련
    public Handler handler = new Handler();
    public Thread uiHandleThread;
    public boolean threadStatus = false;


    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            PlayService.LocalBinder mb = (PlayService.LocalBinder) service;
            playService = mb.getService();
            mp = playService.getMediaPlayer();
            Log.d(TAG, "onServiceConnected: "+mp);


        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mp.stop();
            mp.release();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekbar);
        tvTime = findViewById(R.id.currentPosTV);
        btnPlay = findViewById(R.id.btn_play);


        rvMusicList = findViewById(R.id.rv_music_list);
        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMusicList.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(mContext);
        rvMusicList.setAdapter(musicAdapter);

        // 서비스 바인딩 하기
        Intent musicIntent = new Intent(getApplicationContext(), PlayService.class);
        bindService(musicIntent, connection, BIND_AUTO_CREATE);


        //뭔데 이거 여기는 test2 branch
        initView();
        initData();
        initObserve();
        seekBarInit();






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


    private void initView(){

    }


    private void initData() {
        musicViewModel.findAll();
    }

    private void initObserve() {
        musicViewModel.subscribe().observe(MainActivity.this, new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> musics) {
                Log.d(TAG, "onChanged: musics"+musics);
                musicAdapter.setMusics(musics);
            }
        });
    }

    public void playSong(String musicUrl) throws IOException {
        btnPlay.setImageResource(android.R.drawable.ic_media_pause);

        if (!musicUrl.equals(mp.getDatasource())){
            mp.reset();
            mp.setDataSource(musicUrl);
            mp.prepare(); // might take long! (for buffering, etc)
            mp.start();
        }else{
            mp.start();
        }

    }


    public void seekBarInit(){
        seekBar.setMax(100000);
        seekBar.setProgress(0);
    }

    public void musicPause(){
        mp.pause();
        btnPlay.setImageResource(android.R.drawable.ic_media_play);
    }

    public void musicStop(){
        mp.reset();
        mp.seekTo(0);
        seekBar.setProgress(0);
        threadStatus = true;
        btnPlay.setImageResource(android.R.drawable.ic_media_play);
    }





}