package com.kang.floapptest.viewmodel;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kang.floapptest.model.Song;
import com.kang.floapptest.model.ResponseDto;
import com.kang.floapptest.model.repository.SongRepository;
import com.kang.floapptest.service.MusicApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicViewModel extends ViewModel {

    private static final String TAG = "MusicViewModel";
    private MutableLiveData<List<Song>> mtSongList = new MutableLiveData<>();
    private SongRepository sr = new SongRepository(mtSongList);

    public MusicViewModel() {
        List<Song> musics = new ArrayList<>();
        mtSongList.setValue(musics);
    }

    public MutableLiveData<List<Song>> subscribe(){
        return mtSongList;
    }

    //뭐지 왜 브랜치가 동기화되지?


    public void findAll(){
        sr.networkConnect();
    }




}
