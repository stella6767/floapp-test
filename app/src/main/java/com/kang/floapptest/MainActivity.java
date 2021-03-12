package com.kang.floapptest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;

import com.kang.floapptest.adapter.MusicAdapter;
import com.kang.floapptest.model.Music;
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
    private MediaPlayer mp;
    private Button btnPlay;


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



        rvMusicList = findViewById(R.id.rv_music_list);
        musicViewModel = new ViewModelProvider(this).get(MusicViewModel.class);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMusicList.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(mContext);
        rvMusicList.setAdapter(musicAdapter);

        // 서비스 바인딩 하기
        Intent musicIntent = new Intent(getApplicationContext(), PlayService.class);
        bindService(musicIntent, connection, BIND_AUTO_CREATE);


        initData();
        initObserve();

    }

    private void initData() {
        musicViewModel.findAll();
    }

    private void initObserve() {
        musicViewModel.subscribe().observe(MainActivity.this, new Observer<List<Music>>() {
            @Override
            public void onChanged(List<Music> musics) {
                Log.d(TAG, "onChanged: musics"+musics);
                musicAdapter.setMusics(musics);
            }
        });
    }

    public void playSong(String musicUrl) throws IOException {
        mp.reset();
        mp.setDataSource(musicUrl);
        mp.prepare(); // might take long! (for buffering, etc)
        mp.start();
    }




}