package com.kang.floapptest.viewmodel;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kang.floapptest.model.Music;
import com.kang.floapptest.model.ResponseDto;
import com.kang.floapptest.service.MusicApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicViewModel extends ViewModel {
    private static final String TAG = "MusicViewModel";
    MediaPlayer mp = new MediaPlayer();
    private MutableLiveData<List<Music>> mtMusic = new MutableLiveData<>();

    public MusicViewModel() {
        List<Music> musics = new ArrayList<>();
        mtMusic.setValue(musics);
    }

    public MutableLiveData<List<Music>> subscribe(){
        return mtMusic;
    }

    public void findAll(){
        Call<ResponseDto<List<Music>>> call = MusicApi.retrofit.create(MusicApi.class).findAll();

        call.enqueue(new Callback<ResponseDto<List<Music>>>() {
            @Override
            public void onResponse(Call<ResponseDto<List<Music>>> call, Response<ResponseDto<List<Music>>> response) {
                Log.d(TAG, "onResponse: 성공");
                ResponseDto<List<Music>> result = response.body();
                Log.d(TAG, "onResponse: result: "+result);
                mtMusic.setValue(result.getData());
                
            }

            @Override
            public void onFailure(Call<ResponseDto<List<Music>>> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
    }


//    public void playMusic() {
//
//        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        mp.setDataSource(url);
//        mp.prepare();
//        mp.start();
//
//
//    }

}
